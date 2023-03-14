package stock_management.representations

import stock_management.ROOT_URL
import stock_management.daos.StockInstance
import stock_management.daos.StockLine

fun stockInstanceRepresentation(stockInstance: StockInstance, stockLine: StockLine): Any {
    return object {
        val id = stockInstance.id
        val attributes = object {
            val stockLine = stockLine.name
            val amount = stockInstance.itemCount
        }
        val links = object {
            val warehouse = "$ROOT_URL/warehouses/${stockInstance.warehouseId}"
            val stockLine = "$ROOT_URL/stock-lines/${stockInstance.stockLineId}"
        }
    }
}