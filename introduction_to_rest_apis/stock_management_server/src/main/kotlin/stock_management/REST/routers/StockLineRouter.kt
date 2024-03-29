package stock_management.REST.routers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import stock_management.REST.daos.StockLine
import stock_management.REST.daos.StockLineAttributes
import stock_management.REST.daos.StockLineDao
import stock_management.REST.representations.stockLineRepresentation

fun Routing.stockLineRouter(
    stockLineDao: StockLineDao
) {
    route("/rest/stock-lines") {
        get {
            val stockLines = stockLineDao.getAll()

            call.response.header("Allow", "GET")
            call.respond(
                message = object {
                    val data = stockLines.map(::stockLineRepresentation)
                },
                status = HttpStatusCode.OK
            )
        }

        route("/{stockLine-id}") {
            get {
                val id = call.parameters["stockLine-id"]!!.toInt()

                val stockLine = stockLineDao.getById(id)
                if (stockLine != null) {
                    call.response.header("Allow", "GET")
                    call.respond(
                        message = object {
                            val data = listOf(stockLine).map(::stockLineRepresentation)
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
