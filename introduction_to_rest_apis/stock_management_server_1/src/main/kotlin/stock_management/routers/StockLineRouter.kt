package stock_management.routers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import stock_management.daos.StockLine
import stock_management.daos.StockLineAttributes
import stock_management.daos.StockLineDao

fun Routing.stockLineRouter(
    stockLineDao: StockLineDao
) {
    route("/stock-lines") {
        get {
            val stockLines = stockLineDao.getAll()

            // call.response.header("Cache-Control", "")
            call.respond(
                message = object {
                    val data = stockLines
                },
                status = HttpStatusCode.OK
            )
        }

        route("/{stockLine-id}") {
            get {
                val id = call.parameters["stockLine-id"]!!.toInt()

                val stockLine = stockLineDao.getById(id)
                if (stockLine != null) {
                    // call.response.header("Cache-Control", "")
                    call.respond(
                        message = object {
                            val data = listOf(stockLine)
                        },
                        status = HttpStatusCode.OK
                    )
                } else {
                    call.response.status(HttpStatusCode.NotFound)
                }
            }
        }
    }
}
