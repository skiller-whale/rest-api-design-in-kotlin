package stock_management.routers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import stock_management.daos.StockInstanceDao
import stock_management.StockLine
import stock_management.daos.StockLineDao

fun Routing.stockLineRouter(
    stockLineDao: StockLineDao,
    stockInstanceDao: StockInstanceDao
) {
    route("/stock-lines") {
        get {
            val stockLines = stockLineDao.getAll()
            call.respond(stockLines)
        }

        get("/{stockLine-id}") {
            val id = call.parameters["stockLine-id"]!!

            val stockLine = stockLineDao.getById(id)
            if (stockLine != null) {
                call.respond(stockLine)
                call.response.status(HttpStatusCode.OK)
            } else {
                call.response.status(HttpStatusCode.NotFound)
            }
        }

        put("/{stockLine-id}") {
            val id = call.parameters["stockLine-id"]!!

            val existingStockLine = stockLineDao.getById(id)
            val stockLine = call.receive<StockLine>()
            if (existingStockLine != null) {
                call.respond(stockLineDao.update(stockLine))
                call.response.status(HttpStatusCode.OK)
            } else {
                call.respond(stockLineDao.create(stockLine))
                call.response.status(HttpStatusCode.Created)
            }
        }

        delete("/{stockLine-id}") {
            val id = call.parameters["stockLine-id"]!!

            val stockLine = stockLineDao.getById(id)
            if (stockLine != null) {

                //Delete all stock for this stock line.
                val stockInstances = stockInstanceDao.getForStockLine(id)
                for (stockInstance in stockInstances) {
                    stockInstanceDao.delete(stockInstance)
                }

                stockLineDao.delete(stockLine)

                call.respond(stockLine)
                call.response.status(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}
