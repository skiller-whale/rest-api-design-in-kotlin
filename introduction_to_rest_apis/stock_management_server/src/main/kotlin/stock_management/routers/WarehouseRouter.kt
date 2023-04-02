package stock_management.routers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import stock_management.daos.Warehouse
import stock_management.daos.WarehouseAttributes
import stock_management.daos.StockInstanceDao
import stock_management.daos.StockLineDao
import stock_management.daos.WarehouseDao
import stock_management.representations.stockInstanceRepresentation

fun Routing.warehouseRouter(
    warehouseDao: WarehouseDao,
    stockInstanceDao: StockInstanceDao,
    stockLineDao: StockLineDao
) {
    route("/warehouses") {
        get {
            val warehouses = warehouseDao.getAll()

            // call.response.header("Cache-Control", "")
            call.respond(
                message = object {
                    val data = warehouses
                },
                status = HttpStatusCode.OK
            )
        }

        route("/{warehouse-id}") {
            get {
                val id = call.parameters["warehouse-id"]!!.toInt()

                val warehouse = warehouseDao.getById(id)
                if (warehouse != null) {
                    // call.response.header("Cache-Control", "")
                    call.respond(
                        message = object {
                            val data = listOf(warehouse)
                        },
                        status = HttpStatusCode.OK
                    )
                } else {
                    call.response.status(HttpStatusCode.NotFound)
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
                call.respond(
                    message = object {
                        val data = listOf(updatedWarehouse)
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

                    // call.response.header("Cache-Control", "")
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
