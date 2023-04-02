package stock_management.daos

import java.util.concurrent.atomic.AtomicInteger

data class StockLine(val id: Int, val name: String) {
    constructor(id: Int, attributes: StockLineAttributes) : this(id, attributes.name)
}

data class StockLineAttributes(val name: String)

/**
 * The Data Access Object (DAO) for StockLines - In real life this would be the layer that interacted with the database,
 * for example it might contain SQL queries. For this example, however, it stores data in a hashmap in memory.
 */
class StockLineDao {
    private val stockLines = hashMapOf<Int, StockLine>()

    private var lastId: AtomicInteger = AtomicInteger(0)

    fun create(attributes: StockLineAttributes): StockLine {
        val id = lastId.incrementAndGet()
        val newStockLine = StockLine(id, attributes)
        stockLines[id] = newStockLine
        return newStockLine
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

    fun addStartingData() {
        val stockLineNames = arrayOf(
            "hemispherical rod end",
            "slide outlet",
            "nippled connector",
            "nippled adaptor",
            "mechanical comparator",
            "elbow adaptor",
            "tubing nut",
            "grub screw",
            "internal fan washer",
            "dog point",
            "half dog point",
            "white metal bush",
            "parallel stud coupling",
            "back nut",
            "front nut"
        )

        for (stockLineName in stockLineNames) {
            this.create(StockLineAttributes(stockLineName))
        }
    }
}
