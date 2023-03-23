package stock_management.routers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import stock_management.TemplateProperty
import stock_management.Warehouse
import stock_management.WarehouseAttributes
import stock_management.daos.StockInstanceDao
import stock_management.daos.WarehouseDao
import stock_management.representations.warehouseRepresentation
import stock_management.response

fun Routing.warehouseRouter(
    warehouseDao: WarehouseDao,
    stockInstanceDao: StockInstanceDao
) {
    route("/warehouses") {
        get {
            val warehouses = warehouseDao.getAll()

            call.response.header("Allow", "GET, POST")
            call.respond(
                message = object {
                    val data = warehouses.map(::warehouseRepresentation)
                },
                status = HttpStatusCode.OK
            )
        }

        post {
            val warehouseAttributes: WarehouseAttributes
            try {
                warehouseAttributes = call.receive<WarehouseAttributes>()
            } catch (e: BadRequestException) {
                call.response.header("Allow", "GET, POST")
                call.respond(
                    message = object {
                        val error = "Invalid warehouse data in request body."
                    },
                    status = HttpStatusCode.UnprocessableEntity
                )
                return@post
            }

            val createdWarehouse = warehouseDao.create(warehouseAttributes)
            call.response.header("Allow", "GET, POST")
            call.respond(
                message = object {
                    val data = listOf(createdWarehouse).map(::warehouseRepresentation)
                },
                status = HttpStatusCode.OK
            )
        }

        route("/{warehouse-id}") {

            /**
             * Helper function for defining allowed Methods
             */
            fun allowedMethods(warehouseId: Int) =
                if (stockInstanceDao.isWarehouseEmpty(warehouseId)) "GET, PUT, DELETE" else "GET, PUT"

            get {
                val id = call.parameters["warehouse-id"]!!.toInt()

                val warehouse = warehouseDao.getById(id)
                if (warehouse != null) {
                    call.response.header("Allow", allowedMethods(id))
                    call.respond(
                        message = object {
                            val data = listOf(warehouse).map(::warehouseRepresentation)
                        },
                        status = HttpStatusCode.OK
                    )
                } else {
                    call.response.status(HttpStatusCode.NotFound)
                    call.response.header("Allow", "")
                }
            }

            put {
                val id = call.parameters["warehouse-id"]!!.toInt()

                val existingWarehouse = warehouseDao.getById(id)
                if (existingWarehouse == null) {
                    call.response.status(HttpStatusCode.NotFound)
                    call.response.header("Allow", "")
                    return@put
                }

                val warehouseAttributes: WarehouseAttributes
                try {
                    warehouseAttributes = call.receive<WarehouseAttributes>()
                } catch (e: BadRequestException) {
                    call.response.header("Allow", allowedMethods(id))
                    call.respond(
                        message = object {
                            val error = "Invalid warehouse data in request body."
                        },
                        status = HttpStatusCode.UnprocessableEntity
                    )
                    return@put
                }

                val warehouse = Warehouse(id, warehouseAttributes)
                val updatedWarehouse = warehouseDao.update(warehouse)
                call.response.header("Allow", allowedMethods(id))
                call.respond(
                    message = object {
                        val data = listOf(warehouse).map(::warehouseRepresentation)
                    },
                    status = HttpStatusCode.OK
                )
            }

            delete {
                val id = call.parameters["warehouse-id"]!!.toInt()

                val warehouse = warehouseDao.getById(id)
                if (warehouse != null) {
                    warehouseDao.delete(id)
                    call.response.status(HttpStatusCode.NoContent)
                    return@delete
                } else {
                    call.response.status(HttpStatusCode.NotFound)
                }
            }

            route("/stock-lines") {
                get {
                    val warehouseId = call.parameters["warehouse-id"]!!.toInt()
        
                    val warehouse = warehouseDao.getById(warehouseId)
                    if (warehouse == null) {
                        call.response.status(HttpStatusCode.NotFound)
                        return@get
                    }
        
                    val stockInstances = stockInstanceDao.getForWarehouse(warehouseId)
                    call.response.status(HttpStatusCode.OK)
                    call.respond(stockInstances)
                }
        
                route("/{stockLine-id}") {
                    get {
                        val warehouseId = call.parameters["warehouse-id"]!!.toInt()
        
                        val warehouse = warehouseDao.getById(warehouseId)
                        if (warehouse == null) {
                            call.response.status(HttpStatusCode.NotFound)
                            return@get
                        }
        
                        val stockInstances = stockInstanceDao.getForWarehouse(warehouseId)
        
                        val id = call.parameters["stockLine-id"]!!.toInt()
        
                        val stockInstance = stockInstances.find { it.id == id }
                        if (stockInstance != null) {
                            call.respond(stockInstance)
                            call.response.status(HttpStatusCode.OK)
                        } else {
                            call.response.status(HttpStatusCode.NotFound)
                        }
                    }
                }
            }
        }
    }
}
