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
import stock_management.routers.stockLineRouter
import stock_management.routers.warehouseRouter
import stock_management.REST.daos.StockInstanceDao as RestStockInstanceDao
import stock_management.REST.daos.StockLineDao as RestStockLineDao
import stock_management.REST.daos.WarehouseDao as RestWarehouseDao
import stock_management.REST.routers.indexRouter as restIndexRouter
import stock_management.REST.routers.stockLineRouter as restStockLineRouter
import stock_management.REST.routers.warehouseRouter as restWarehouseRouter

const val PORT = 7070
const val ROOT_URL = "http://stock-management-server:${PORT}"

fun main() {
    val app = embeddedServer(Netty, port = PORT) {

        // less RESTful data
        val warehouseDao = WarehouseDao()
        val stockLineDao = StockLineDao()
        val stockInstanceDao = StockInstanceDao()

        warehouseDao.addStartingData()
        stockLineDao.addStartingData()
        stockInstanceDao.addStartingData()

        // more RESTful data
        val restWarehouseDao = RestWarehouseDao()
        val restStockLineDao = RestStockLineDao()
        val restStockInstanceDao = RestStockInstanceDao()

        restWarehouseDao.addStartingData()
        restStockLineDao.addStartingData()
        restStockInstanceDao.addStartingData()

        routing {
            // less RESTful routers
            warehouseRouter(warehouseDao, stockInstanceDao, stockLineDao)
            stockLineRouter(stockLineDao)

            // more RESTful routers
            restIndexRouter()
            restWarehouseRouter(restWarehouseDao, restStockInstanceDao, restStockLineDao)
            restStockLineRouter(restStockLineDao)
        }

        install(ContentNegotiation) {
            jackson {
                configure(SerializationFeature.INDENT_OUTPUT, true)
            }
        }
    }

    app.start(wait = true)
}
