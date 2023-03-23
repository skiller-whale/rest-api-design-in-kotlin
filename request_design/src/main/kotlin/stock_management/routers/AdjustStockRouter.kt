package stock_management.routers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import stock_management.StockInstance
import stock_management.daos.StockInstanceDao

/**
 * I think in its current state this is a good example of bad design - Writing it without having these as proper entities with ids
 * is death by 1000 cuts. The endpoints are "nouns", but they're still being treated as verbs.
 * The attempt to hide StockInstance as an implementation detail is something I'm not sure about either.
 */

/**
 * Error objects
 */
private const val INSUFFICIENT_STOCK_ERROR = "Insufficient stock"

data class StockTransfer(val stockLineId: Int, val warehouseFromId: Int, val warehouseToId: Int, val itemCount: Int)
data class Delivery(val stockLineId: Int, val warehouseToId: Int, val itemCount: Int)
data class WriteOff(val stockLineId: Int, val warehouseFromId: Int, val itemCount: Int)

fun Routing.adjustStockRouter(stockInstanceDao: StockInstanceDao) {
    route("/adjust-stock") {
        //TODO: Add entities (extensions of a StockTransfer superclass?) for all these endpoints to have a history and record extra info.
        //TODO: Should these all be one stockTransfer endpoint?
        //TODO: Make this deal with non-existent warehouses and stock lines with something other than "Insufficient Stock"?

        post("/delivery") {
            val (stockLineId, warehouseToId, itemCount) = call.receive<Delivery>()

            //TODO: factor out adjust stock method.
            val stockInstance = stockInstanceDao.getForWarehouseAndStockLine(warehouseToId, stockLineId)
            if (stockInstance == null) {
                //TODO - should definitely check the warehouse and stockline exists here.
                stockInstanceDao.create(stockLineId, warehouseToId, itemCount)
            } else {
                //TODO: Possibly overkill creating a new object just for the update - could just have updateCount in the stockInstanceDao.
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
            //TODO: Make the stock change more transactional, less vulnerable to discrepancies caused by errors partway through a transfer
            //TODO: Should we remove stockInstances of 0?
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
