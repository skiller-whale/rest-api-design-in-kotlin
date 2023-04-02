package stock_management.REST.routers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import stock_management.ROOT_URL
import stock_management.REST.daos.WarehouseDao
import stock_management.REST.daos.StockInstanceDao
import stock_management.REST.daos.StockLineDao
import stock_management.REST.routers.warehouseRouter
import stock_management.REST.routers.stockLineRouter

fun Routing.indexRouter() {
    get ("/rest") {
        call.respond(object {
            val data = listOf(object {
                val links = object {
                    val warehouses = "${ROOT_URL}/rest/warehouses"
                    val stockLines = "${ROOT_URL}/rest/stock-lines"
                }
            })
        })
    }
}
