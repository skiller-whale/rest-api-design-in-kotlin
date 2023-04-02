package stock_management.REST.representations

import stock_management.ROOT_URL
import stock_management.REST.daos.StockLine
import stock_management.REST.daos.StockLineAttributes

fun stockLineRepresentation(stockLine: StockLine): Any {
    return object {
        val id = stockLine.id
        val attributes = StockLineAttributes(name = stockLine.name)
        val links = object {
            val self = "$ROOT_URL/rest/stock-lines/$id"
        }
    }
}