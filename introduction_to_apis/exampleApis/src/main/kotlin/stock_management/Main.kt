package stock_management

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*

import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import stock_management.daos.*
import stock_management.routers.*

/**
 * Data entities
 */
@Serializable
data class Warehouse(val id: String, val name: String)
@Serializable
data class StockLine(val id: String, val name: String)
@Serializable
data class StockInstance(val id: String, val stockLineId: String, val warehouseId: String, val itemCount: Int)

fun main() {
    val warehouseDao = WarehouseDao()
    val stockLineDao = StockLineDao()
    val stockInstanceDao = StockInstanceDao()

    addStartingData(warehouseDao, stockLineDao, stockInstanceDao)

    val app = embeddedServer(Netty, port = 7070) {
        routing {
            warehouseRouter(warehouseDao, stockInstanceDao)
            stockLineRouter(stockLineDao, stockInstanceDao)
            stockInstanceRouter(stockInstanceDao)
        }

        install(ContentNegotiation) {
            json()
        }
    }

    app.start(wait = true)
}
