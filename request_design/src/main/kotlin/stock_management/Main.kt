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

/**
 * TODO - Exercises:
 * Inline the response method.
 * Split into 2 APIs, a simple and a RESTful one.
 * Delete unneeded functionality - Including things only needed because there's a POST endpoint.
 *
 * simple one will not need allowed headers - will be used for caching.
 *
 * Caching - uses simple one
 * RESTfyl - uses both - add a warehouse attribute.
 * HATEOAS - uses REST-y one - make allowed headers dependent on deletion
 */

/**
 * TODO: Decisions:
 * Should we have a setHeaders(Warehouse)? No - overly abstracted, and it depends on the route. allowedMethods is as far as we'll go.
 * Should we copy/paste the high level format, or leave it as a function - leave it factored out for the playground, and inline it for the exercises.
 * Could we extend respond method to add a nice headers params - no, making our own framework - we'd do it in real life, but overcomplicating for learners.
 *
 * Do we need documentation for the simple API? No - the first module will have introduced, and they'll have the code - CATEOAS
 * How do we want to display the stockinstance in the RESTful version? Just showing the ids is not the most helpful.
 *      But I feel like the solution is relations and included, but that's too much for this.
 *      I feel like denormalising data into single API calls is a separate question. Also, if the stockLines are all cached, why repeat them for every stockInstance?
 *
 * What links on the stock instances? I feel like the warehouse is pointless because it's part of the URL - the stock line is useful though.
 */

/**
 * TODO: Introduction to APIS
 * Add the query endpoints in.
 */

const val PORT = 7070
const val ROOT_URL = "http://localhost:${PORT}"

/**
 * Data entities
 * TODO - We could change these from linking via id to linking via the full object? This gives a denormalised deserialisation.
 */
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
    /* DO NOT add code before this embeddedServer call.
     * It will not only fail to auto-reload, but will stop auto-reload working elsewhere.*/
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
            /*Having the routers in different files makes it easier for us to manage different versions of
            routers (i.e. an example bad set of routes, and good set of routes) but keeping one file to manage which
            version we're currently using.*/
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
