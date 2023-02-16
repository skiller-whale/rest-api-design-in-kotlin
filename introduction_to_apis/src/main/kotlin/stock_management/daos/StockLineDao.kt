package stock_management.daos

import stock_management.StockLine

/**
 * The Data Access Object (DAO) for StockLines - In real life this would be the layer that interacted with the database,
 * for example it might contain SQL queries. For this example, however, it stores data in a hashmap in memory.
 */
class StockLineDao {
    private val stockLines = hashMapOf<String, StockLine>()

    fun create(stockLine: StockLine): StockLine {
        stockLines[stockLine.id] = stockLine
        return stockLine
    }

    fun getById(id: String): StockLine? {
        return stockLines[id]
    }

    fun getAll(): List<StockLine> {
        return stockLines.values.toList()
    }

    fun update(stockLine: StockLine): StockLine {
        stockLines[stockLine.id] = stockLine
        return stockLine
    }

    fun delete(stockLine: StockLine) {
        stockLines.remove(stockLine.id)
    }
}



