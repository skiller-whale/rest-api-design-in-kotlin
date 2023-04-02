package stock_management.REST.representations

import stock_management.ROOT_URL
import stock_management.REST.daos.StockInstance
import stock_management.REST.daos.StockLine

fun stockInstanceRepresentation(stockInstance: StockInstance, stockLine: StockLine): Any {
    return object {
        val id = stockInstance.id
        val attributes = object {
            val stockLine = stockLine.name
            val amount = stockInstance.itemCount
        }
        val links = object {
            val warehouse = "$ROOT_URL/rest/warehouses/${stockInstance.warehouseId}"
            val stockLine = "$ROOT_URL/rest/stock-lines/${stockInstance.stockLineId}"
        }
    }
}