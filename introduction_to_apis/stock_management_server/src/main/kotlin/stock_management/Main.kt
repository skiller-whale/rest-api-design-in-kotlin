package stock_management

import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*

import io.ktor.server.plugins.contentnegotiation.*

import stock_management.daos.*
import stock_management.routers.*

/**
 * Data entities
 */
data class Warehouse(val id: Int?, val name: String)
data class StockLine(val id: Int?, val name: String)
data class StockInstance(val id: Int, val stockLineId: Int, val warehouseId: Int, val itemCount: Int)

fun main() {
    val app = embeddedServer(Netty, port = 7070) {

        val warehouseDao = WarehouseDao()
        val stockLineDao = StockLineDao()
        val stockInstanceDao = StockInstanceDao()

        addStartingData(warehouseDao, stockLineDao, stockInstanceDao)

        routing {
            warehouseRouter(warehouseDao, stockInstanceDao)
            stockLineRouter(stockLineDao, stockInstanceDao)
        }

        install(ContentNegotiation) {
            jackson()
        }
    }

    app.start(wait = true)
}
