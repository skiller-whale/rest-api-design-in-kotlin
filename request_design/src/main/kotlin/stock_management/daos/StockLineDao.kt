package stock_management.daos

import stock_management.StockLine
import stock_management.StockLineAttributes
import java.util.concurrent.atomic.AtomicInteger

/**
 * The Data Access Object (DAO) for StockLines - In real life this would be the layer that interacted with the database,
 * for example it might contain SQL queries. For this example, however, it stores data in a hashmap in memory.
 */
class StockLineDao {
    private val stockLines = hashMapOf<Int, StockLine>()

    private var lastId: AtomicInteger = AtomicInteger(stockLines.size - 1)

    fun createWithId(attributes: StockLineAttributes, id: Int): StockLine {
        val newStockLine = StockLine(id, attributes)
        stockLines[id] = newStockLine
        // FIXME: update `lastId` properly
        return newStockLine
    }

    fun create(attributes: StockLineAttributes): StockLine {
        val id = lastId.incrementAndGet()
        return createWithId(attributes, id)
    }

    fun getById(id: Int): StockLine? {
        return stockLines[id]
    }

    fun getAll(): List<StockLine> {
        return stockLines.values.toList()
    }

    fun update(stockLine: StockLine): StockLine {
        stockLines[stockLine.id] = stockLine
        return stockLine
    }

    fun delete(id: Int) {
        stockLines.remove(id)
    }

    fun delete(stockLine: StockLine) {
        stockLines.remove(stockLine.id)
    }
}
