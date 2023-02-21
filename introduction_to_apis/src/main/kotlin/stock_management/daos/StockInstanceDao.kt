package stock_management.daos

import stock_management.StockInstance
import java.util.concurrent.atomic.AtomicInteger

/**
 * The Data Access Object (DAO) for StockInstances - In real life this would be the layer that interacted with the database,
 * for example it might contain SQL queries. For this example, however, it stores data in a hashmap in memory.
 */
class StockInstanceDao {
    private val stockInstances = hashMapOf<Int, StockInstance>()

    private var lastId: AtomicInteger = AtomicInteger(stockInstances.size - 1)

    fun create(stockLineId: Int, warehouseId: Int, itemCount: Int): StockInstance {
        val id = lastId.incrementAndGet()
        val newStockInstance = StockInstance(id, stockLineId, warehouseId, itemCount)
        stockInstances[id] = newStockInstance
        return newStockInstance
    }

    fun getForWarehouse(warehouseId: Int): List<StockInstance> {
        return stockInstances.values.filter { it.warehouseId == warehouseId }
    }

    fun getForStockLine(stockLineId: Int): List<StockInstance> {
        return stockInstances.values.filter { it.stockLineId == stockLineId }
    }

    fun getForWarehouseAndStockLine(warehouseId: Int, stockLineId: Int): StockInstance? {
        return stockInstances.values.find { it.warehouseId == warehouseId && it.stockLineId == stockLineId }
    }

    fun update(stockInstance: StockInstance) {
        stockInstances[stockInstance.id] = stockInstance
    }

    fun delete(stockInstance: StockInstance) {
        stockInstances.remove(stockInstance.id)
    }
}
