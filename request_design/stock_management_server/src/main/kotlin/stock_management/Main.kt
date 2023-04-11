package stock_management

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import stock_management.daos.StockInstanceDao
import stock_management.daos.StockLineDao
import stock_management.daos.WarehouseDao
import stock_management.routers.adjustStockRouter
import stock_management.routers.stockLineRouter
import stock_management.routers.warehouseRouter

const val PORT = 7070
const val ROOT_URL = "http://localhost:${PORT}"

data class Warehouse(val id: Int, val name: String, val address: String) {
    constructor(id: Int, attributes: WarehouseAttributes) : this(
        id = id,
        name = attributes.name,
        address = attributes.address
    )
}

data class WarehouseAttributes(val name: String, val address: String)

data class StockLine(val id: Int, val name: String) {
    constructor(id: Int, attributes: StockLineAttributes) : this(id = id, name = attributes.name)
}

data class StockLineAttributes(val name: String)

data class StockInstance(val id: Int, val stockLineId: Int, val warehouseId: Int, val itemCount: Int) {
    constructor(id: Int, attributes: StockInstanceAttributes) : this(
        id = id,
        stockLineId = attributes.stockLineId,
        warehouseId = attributes.warehouseId,
        itemCount = attributes.itemCount
    )
}

data class StockInstanceAttributes(val stockLineId: Int, val warehouseId: Int, val itemCount: Int)

fun main() {
    val app = embeddedServer(Netty, port = PORT) {

        val warehouseDao = WarehouseDao()
        val stockLineDao = StockLineDao()
        val stockInstanceDao = StockInstanceDao()

        addStartingData(warehouseDao, stockLineDao, stockInstanceDao)

        routing {
            get {
                call.respond(object {
                    val links = object {
                        val warehouses = "${ROOT_URL}/warehouses"
                        val stockLines = "${ROOT_URL}/stock-lines"
                    }
                })
            }
            warehouseRouter(warehouseDao, stockInstanceDao)
            stockLineRouter(stockLineDao, stockInstanceDao)
            adjustStockRouter(stockInstanceDao)
        }

        install(ContentNegotiation) {
            jackson {
                configure(SerializationFeature.INDENT_OUTPUT, true)
            }
        }
    }

    app.start(wait = true)
}
