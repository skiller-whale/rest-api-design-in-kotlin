package stock_management.routers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import stock_management.StockInstance
import stock_management.daos.StockInstanceDao
import java.util.*

/**
 * Errors
 */
private const val INSUFFICIENT_STOCK_ERROR = "Insufficient stock"

@Serializable
data class Delivery(val stockLineId: String, val warehouseToId: String, val itemCount: Int)

@Serializable
data class WriteOff(val stockLineId: String, val warehouseFromId: String, val itemCount: Int)

fun Routing.stockInstanceRouter(stockInstanceDao: StockInstanceDao) {
    route("/stock-instances") {
        post("/delivery") {
            val (stockLineId, warehouseToId, itemCount) = call.receive<Delivery>()

            val stockInstance = stockInstanceDao.getForWarehouseAndStockLine(warehouseToId, stockLineId)
            if (stockInstance == null) {
                val id = UUID.randomUUID().toString()
                stockInstanceDao.create(StockInstance(id, stockLineId, warehouseToId, itemCount))
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