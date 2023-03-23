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
        return stockInstances.values.filter { s -> s.warehouseId == warehouseId }
    }

    fun isWarehouseEmpty(warehouseId: Int): Boolean {
        //TODO - this relies on StockInstances of size 0 being removed, which doesn't currently happen.
        return stockInstances.values.find { s -> s.warehouseId == warehouseId } == null
    }

    fun getForStockLine(stockLineId: Int): List<StockInstance> {
        return stockInstances.values.filter { s -> s.stockLineId == stockLineId }
    }

    fun doesStockExist(stockLineId: Int): Boolean {
        //TODO - this relies on StockInstances of size 0 being removed, which doesn't currently happen.
        return stockInstances.values.find { s -> s.stockLineId == stockLineId } == null
    }

    fun getForWarehouseAndStockLine(warehouseId: Int, stockLineId: Int): StockInstance? {
        val stockInstances =
            stockInstances.values.filter { s -> s.warehouseId == warehouseId && s.stockLineId == stockLineId }

        if (stockInstances.size == 1) {
            return stockInstances[0]
        } else if (stockInstances.size > 1) {
            throw Exception("Too many stock instances ${stockInstances.size} for warehouse $warehouseId and stock line $stockLineId") //TODO: read up on exceptions in Kotlin, because this is just written like javascript, in the hope it works nicely.
        } else {
            return null
        }
    }

    fun update(stockInstance: StockInstance) {
        stockInstances[stockInstance.id] = stockInstance
    }

    fun delete(id: Int) {
        stockInstances.remove(id)
    }

    fun delete(stockInstance: StockInstance) {
        stockInstances.remove(stockInstance.id)
    }
}



