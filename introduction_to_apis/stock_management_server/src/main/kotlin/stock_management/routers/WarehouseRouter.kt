package stock_management.routers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import stock_management.StockInstance
import stock_management.Warehouse
import stock_management.daos.StockInstanceDao
import stock_management.daos.WarehouseDao

private const val INSUFFICIENT_STOCK_ERROR = "Insufficient stock"

data class Delivery(val stockLineId: Int, val itemCount: Int)

data class WriteOff(val stockLineId: Int, val itemCount: Int)

fun Routing.warehouseRouter(
    warehouseDao: WarehouseDao,
    stockInstanceDao: StockInstanceDao
) {
    route("/warehouses") {
        get {
            val warehouses = warehouseDao.getAll()
            call.respond(warehouses)
        }

        post {
            val warehouse = call.receive<Warehouse>()

            val createdWarehouse = warehouseDao.create(warehouse.name)
            call.respond(createdWarehouse)
            call.response.status(HttpStatusCode.Created)
        }

        route ("/{warehouse-id}") {
            get {
                val id = call.parameters["warehouse-id"]!!.toInt()

                val warehouse = warehouseDao.getById(id)
                if (warehouse != null) {
                    call.respond(warehouse)
                    call.response.status(HttpStatusCode.OK)
                } else {
                    call.response.status(HttpStatusCode.NotFound)
                }
            }

            put {
                val id = call.parameters["warehouse-id"]!!.toInt()

                val existingWarehouse = warehouseDao.getById(id)
                val warehouse = call.receive<Warehouse>()
                if (existingWarehouse != null) {
                    call.respond(warehouseDao.update(warehouse))
                    call.response.status(HttpStatusCode.OK)
                } else {
                    call.response.status(HttpStatusCode.NotFound)
                }
            }

            delete {
                val id = call.parameters["warehouse-id"]!!.toInt()

                val warehouse = warehouseDao.getById(id)
                if (warehouse != null) {

                    //Delete all stock in the warehouse.
                    val stockInstances = stockInstanceDao.getForWarehouse(id)
                    for (stockInstance in stockInstances) {
                        stockInstanceDao.delete(stockInstance)
                    }

                    warehouseDao.delete(warehouse)
                    call.respond(warehouse)
                    call.response.status(HttpStatusCode.OK)

                } else {
                    call.response.status(HttpStatusCode.NotFound)
                }
            }

            route("/stock-instances") {
                get {
                    val warehouseId = call.parameters["warehouse-id"]!!.toInt()

                    val warehouse = warehouseDao.getById(warehouseId)
                    if (warehouse != null) {
                        val stockInstances = stockInstanceDao.getForWarehouse(warehouseId)

                        call.respond(stockInstances)
                        call.response.status(HttpStatusCode.OK)
                    } else {
                        call.response.status(HttpStatusCode.NotFound)
                    }
                }
            }

            route ("/delivery") {
                post {
                    val warehouseId = call.parameters["warehouse-id"]!!.toInt()
                    val (stockLineId, itemCount) = call.receive<Delivery>()

                    val stockInstance = stockInstanceDao.getForWarehouseAndStockLine(warehouseId, stockLineId)
                    if (stockInstance == null) {
                        stockInstanceDao.create(stockLineId, warehouseId, itemCount)
                    } else {
                        stockInstanceDao.update(
                            StockInstance(
                                stockInstance.id,
                                stockLineId,
                                warehouseId,
                                stockInstance.itemCount + itemCount
                            )
                        )
                    }

                    call.respond(HttpStatusCode.OK)
                }
            }

            route ("/write-off") {
                post("/write-off") {
                    val warehouseId = call.parameters["warehouse-id"]!!.toInt()
                    val (stockLineId, itemCount) = call.receive<WriteOff>()

                    val stockInstanceFrom = stockInstanceDao.getForWarehouseAndStockLine(warehouseId, stockLineId)

                    if (stockInstanceFrom == null || stockInstanceFrom.itemCount < itemCount) {
                        call.response.status(HttpStatusCode.UnprocessableEntity)
                        call.respond(INSUFFICIENT_STOCK_ERROR)
                    } else {
                        stockInstanceDao.update(
                            StockInstance(
                                stockInstanceFrom.id,
                                stockLineId,
                                warehouseId,
                                stockInstanceFrom.itemCount - itemCount,
                            ),
                        )
                        call.respond(HttpStatusCode.OK)
                    }
                }
            }
        }
    }
}
