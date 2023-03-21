package stock_management.representations

import stock_management.daos.StockInstance
import stock_management.daos.StockLine

fun stockInstanceRepresentation(stockInstance: StockInstance, stockLine: StockLine): Any {
    return object {
        val id = stockInstance.id
        val stockLine = stockLine
        val amount = stockInstance.itemCount
    }
}