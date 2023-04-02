package stock_management.REST.routers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import stock_management.REST.daos.Warehouse
import stock_management.REST.daos.WarehouseAttributes
import stock_management.REST.daos.StockInstanceDao
import stock_management.REST.daos.StockLineDao
import stock_management.REST.daos.WarehouseDao
import stock_management.REST.representations.stockInstanceRepresentation
import stock_management.REST.representations.warehouseRepresentation

fun Routing.warehouseRouter(
    warehouseDao: WarehouseDao,
    stockInstanceDao: StockInstanceDao,
    stockLineDao: StockLineDao
) {
    route("/rest/warehouses") {
        get {
            val warehouses = warehouseDao.getAll()

            call.response.header("Allow", "GET")
            call.respond(
                message = object {
                    val data = warehouses.map(::warehouseRepresentation)
                },
                status = HttpStatusCode.OK
            )
        }

        route("/{warehouse-id}") {

            fun allowedMethods(warehouseId: Int): String {
                // if (stockInstanceDao.isWarehouseEmpty(warehouseId)) {
                    return "GET, PUT, DELETE"
                // } else {
                //     return "GET, PUT"
                // }
            }

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
                    return@put
                }

                val warehouseAttributes: WarehouseAttributes
                try {
                    warehouseAttributes = call.receive<WarehouseAttributes>()
                } catch (e: BadRequestException) {
                    call.response.header("Allow", allowedMethods(id))
                    call.respond(
                        message = object {
                            val errors = listOf("Invalid warehouse data in request body.")
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
                        val data = listOf(updatedWarehouse).map(::warehouseRepresentation)
                    },
                    status = HttpStatusCode.OK
                )
            }

            delete {
                val id = call.parameters["warehouse-id"]!!.toInt()

                val warehouse = warehouseDao.getById(id)
                if (warehouse != null) {
                    // if (stockInstanceDao.isWarehouseEmpty(id)) {
                        warehouseDao.delete(id)
                        call.response.status(HttpStatusCode.NoContent)
                        return@delete
                    // } else {
                    //     call.response.status(HttpStatusCode.MethodNotAllowed)
                    //     call.response.header("Allow", allowedMethods(id))
                    //     call.respond(
                    //         object {
                    //             val errors = listOf("You can only delete empty warehouses.")
                    //         }
                    //     )
                    // }
                } else {
                    call.response.status(HttpStatusCode.NotFound)
                }
            }

            route("/stock-instances") {
                get {
                    val warehouseId = call.parameters["warehouse-id"]!!.toInt()

                    val warehouse = warehouseDao.getById(warehouseId)
                    if (warehouse == null) {
                        call.response.status(HttpStatusCode.NotFound)
                        return@get
                    }

                    val stockInstances = stockInstanceDao.getForWarehouse(warehouseId)

                    call.respond(
                        message = object {
                            val data = stockInstances.map {
                                val stockLine = stockLineDao.getById(it.stockLineId)!!
                                return@map stockInstanceRepresentation(it, stockLine)
                            }
                        },
                        status = HttpStatusCode.OK
                    )
                }
            }
        }
    }
}
