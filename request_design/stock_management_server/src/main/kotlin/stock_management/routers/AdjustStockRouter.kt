package stock_management.routers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import stock_management.StockInstance
import stock_management.daos.StockInstanceDao

/**
 * Error objects
 */
private const val INSUFFICIENT_STOCK_ERROR = "Insufficient stock"

data class StockTransfer(val stockLineId: Int, val warehouseFromId: Int, val warehouseToId: Int, val itemCount: Int)
data class Delivery(val stockLineId: Int, val warehouseToId: Int, val itemCount: Int)
data class WriteOff(val stockLineId: Int, val warehouseFromId: Int, val itemCount: Int)

fun Routing.adjustStockRouter(stockInstanceDao: StockInstanceDao) {
    route("/adjust-stock") {
        post("/delivery") {
            val (stockLineId, warehouseToId, itemCount) = call.receive<Delivery>()

            val stockInstance = stockInstanceDao.getForWarehouseAndStockLine(warehouseToId, stockLineId)
            if (stockInstance == null) {
                stockInstanceDao.create(stockLineId, warehouseToId, itemCount)
            } else {
                stockInstanceDao.update(
                    StockInstance(
                        stockInstance.id,
                        stockLineId,
                        warehouseToId,
                        stockInstance.itemCount + itemCount
                    )
                )
            }

            call.respond(HttpStatusCode.OK)
        }

        post("/stock-transfer") stockTransfer@{
            val (stockLineId, warehouseFromId, warehouseToId, itemCount) = call.receive<StockTransfer>()

            val stockInstanceFrom = stockInstanceDao.getForWarehouseAndStockLine(warehouseFromId, stockLineId)

            if (stockInstanceFrom == null || stockInstanceFrom.itemCount < itemCount) {
                call.response.status(HttpStatusCode.UnprocessableEntity)
                call.respond(INSUFFICIENT_STOCK_ERROR)
                return@stockTransfer
            }

            val newFromItemCount = stockInstanceFrom.itemCount - itemCount
            stockInstanceDao.update(
                stockInstanceFrom.copy(itemCount = newFromItemCount)
            )

            val stockInstanceTo = stockInstanceDao.getForWarehouseAndStockLine(warehouseToId, stockLineId)
            if (stockInstanceTo == null) {
                stockInstanceDao.create(stockLineId, warehouseToId, itemCount)
            } else {
                val newToItemCount = stockInstanceTo.itemCount + itemCount
                stockInstanceDao.update(
                    stockInstanceTo.copy(itemCount = newToItemCount)
                )
            }

            call.respond(HttpStatusCode.OK)
        }

        post("/write-off") {
            val (stockLineId, warehouseFromId, itemCount) = call.receive<WriteOff>()

            val stockInstanceFrom = stockInstanceDao.getForWarehouseAndStockLine(warehouseFromId, stockLineId)

            if (stockInstanceFrom == null || stockInstanceFrom.itemCount < itemCount) {
                call.response.status(HttpStatusCode.UnprocessableEntity)
                call.respond(INSUFFICIENT_STOCK_ERROR)
            } else {
                stockInstanceDao.update(
                    StockInstance(
                        stockInstanceFrom.id,
                        stockLineId,
                        warehouseFromId,
                        stockInstanceFrom.itemCount - itemCount,
                    ),
                )
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
