package stock_management.daos

import java.util.concurrent.atomic.AtomicInteger

data class StockInstance(val id: Int, val stockLineId: Int, val warehouseId: Int, val itemCount: Int) {
    constructor(id: Int, attributes: StockInstanceAttributes) : this(id, attributes.stockLineId, attributes.warehouseId, attributes.itemCount)
}

data class StockInstanceAttributes(val stockLineId: Int, val warehouseId: Int, val itemCount: Int)

/**
 * The Data Access Object (DAO) for StockInstances - In real life this would be the layer that interacted with the database,
 * for example it might contain SQL queries. For this example, however, it stores data in a hashmap in memory.
 */
class StockInstanceDao {
    private val stockInstances = hashMapOf<Int, StockInstance>()

    private var lastId: AtomicInteger = AtomicInteger(0)

    fun create(attributes: StockInstanceAttributes): StockInstance {
        val id = lastId.incrementAndGet()
        val newStockInstance = StockInstance(id, attributes)
        stockInstances[id] = newStockInstance
        return newStockInstance
    }

    fun getForWarehouse(warehouseId: Int): List<StockInstance> {
        return stockInstances.values.filter { s -> s.warehouseId == warehouseId }
    }

    fun isWarehouseEmpty(warehouseId: Int): Boolean {
        return stockInstances.values.find { s -> s.warehouseId == warehouseId } == null
    }

    fun getForWarehouseAndStockLine(warehouseId: Int, stockLineId: Int): StockInstance? {
        return stockInstances.values.find { s -> s.warehouseId == warehouseId && s.stockLineId == stockLineId }
    }

    fun addStartingData() {
        val stockInstances = arrayOf(
            Triple(1, 12, 150),
            Triple(1, 1, 10),
            Triple(1, 2, 25),
            Triple(1, 4, 50),
            Triple(1, 5, 10),
            Triple(1, 6, 150),
            Triple(2, 1, 100),
            Triple(2, 6, 100),
            Triple(2, 11, 25),
            Triple(3, 8, 150),
            Triple(4, 1, 50),
            Triple(4, 8, 100),
            Triple(5, 4, 150),
            Triple(5, 6, 50),
            Triple(5, 7, 1),
            Triple(6, 1, 150),
            Triple(6, 4, 50),
            Triple(7, 4, 1),
            Triple(7, 13, 100),
            Triple(8, 1, 25),
            Triple(8, 9, 100),
            Triple(8, 11, 10),
            Triple(9, 2, 50),
            Triple(9, 6, 50),
            Triple(10, 3, 100),
            Triple(10, 10, 200),
            Triple(12, 3, 150),
            Triple(12, 8, 1),
            Triple(12, 12, 200),
            Triple(13, 5, 200)
        )

        for ((warehouseId, stockLineId, itemCount) in stockInstances) {
            this.create(StockInstanceAttributes(warehouseId, stockLineId, itemCount))
        }
    }
}



