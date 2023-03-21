package stock_management

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import stock_management.daos.StockInstanceDao
import stock_management.daos.StockLineDao
import stock_management.daos.WarehouseDao
import stock_management.routers.indexRouter
import stock_management.routers.stockLineRouter
import stock_management.routers.warehouseRouter

const val PORT = 7072
const val ROOT_URL = "http://stock_management_server_2:${PORT}"

fun main() {
    val app = embeddedServer(Netty, port = PORT) {

        val warehouseDao = WarehouseDao()
        val stockLineDao = StockLineDao()
        val stockInstanceDao = StockInstanceDao()

        warehouseDao.addStartingData()
        stockLineDao.addStartingData()
        stockInstanceDao.addStartingData()

        routing {
            indexRouter()
            warehouseRouter(warehouseDao, stockInstanceDao, stockLineDao)
            stockLineRouter(stockLineDao)
        }

        install(ContentNegotiation) {
            jackson {
                configure(SerializationFeature.INDENT_OUTPUT, true)
            }
        }
    }

    app.start(wait = true)
}
