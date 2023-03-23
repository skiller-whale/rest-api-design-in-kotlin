package stock_management.representations

import stock_management.ROOT_URL
import stock_management.StockInstance
import stock_management.StockInstanceAttributes

fun stockInstanceRepresentation(stockInstance: StockInstance): Any {
    return object {
        val id = stockInstance
        val attributes = StockInstanceAttributes(
            stockLineId = stockInstance.stockLineId,
            warehouseId = stockInstance.warehouseId,
            itemCount = stockInstance.itemCount
        )
        val links = object {
            val self = "$ROOT_URL/warehouses/${stockInstance.warehouseId}/stock-lines/${stockInstance.stockLineId}"
            val stockLine = "$ROOT_URL/stock-lines/${stockInstance.stockLineId}"
        }
    }
}