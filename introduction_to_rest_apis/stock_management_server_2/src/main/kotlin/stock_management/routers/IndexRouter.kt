package stock_management.routers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import stock_management.ROOT_URL

fun Routing.indexRouter() {
    get {
        call.respond(object {
            val data = listOf(object {
                val links = object {
                    val warehouses = "${ROOT_URL}/warehouses"
                    val stockLines = "${ROOT_URL}/stock-lines"
                }
            })
        })
    }
}
