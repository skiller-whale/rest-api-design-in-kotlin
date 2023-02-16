package stock_management.daos

import stock_management.StockInstance

/**
 * The Data Access Object (DAO) for StockInstances - In real life this would be the layer that interacted with the database,
 * for example it might contain SQL queries. For this example, however, it stores data in a hashmap in memory.
 */
class StockInstanceDao {
    private val stockInstances = hashMapOf<String, StockInstance>()

    fun create(stockInstance: StockInstance): StockInstance {
        stockInstances[stockInstance.id] = stockInstance
        return stockInstance
    }

    fun getForWarehouse(warehouseId: String): List<StockInstance> {
        return stockInstances.values.filter { it.warehouseId == warehouseId }
    }

    fun getForStockLine(stockLineId: String): List<StockInstance> {
        return stockInstances.values.filter { it.stockLineId == stockLineId }
    }

    fun getForWarehouseAndStockLine(warehouseId: String, stockLineId: String): StockInstance? {
        return stockInstances.values.find { it.warehouseId == warehouseId && it.stockLineId == stockLineId }
    }

    fun update(stockInstance: StockInstance) {
        stockInstances[stockInstance.id] = stockInstance
    }

    fun delete(stockInstance: StockInstance) {
        stockInstances.remove(stockInstance.id)
    }
}
