package stock_management.representations

import stock_management.ROOT_URL
import stock_management.StockLine
import stock_management.StockLineAttributes

fun stockLineRepresentation(stockLine: StockLine): Any {
    return object {
        val id = stockLine.id
        val attributes = StockLineAttributes(name = stockLine.name)
        val links = object {
            val self = "$ROOT_URL/stock-lines/$id"
        }
    }
}