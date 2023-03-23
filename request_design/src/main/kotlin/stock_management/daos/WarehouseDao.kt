package stock_management.daos

import stock_management.Warehouse
import stock_management.WarehouseAttributes
import java.util.concurrent.atomic.AtomicInteger

/**
 * The Data Access Object (DAO) for Warehouses - In real life this would be the layer that interacted with the database,
 * for example it might contain SQL queries. For this example, however, it stores data in a hashmap in memory.
 */
class WarehouseDao {
    private val warehouses = hashMapOf<Int, Warehouse>()

    private var lastId: AtomicInteger = AtomicInteger(warehouses.size - 1)

    fun create(attributes: WarehouseAttributes): Warehouse {
        val id = lastId.incrementAndGet()
        val newWarehouse = Warehouse(id, attributes)
        warehouses[id] = newWarehouse
        return newWarehouse
    }

    fun getById(id: Int): Warehouse? {
        return warehouses[id]
    }

    fun getAll(): List<Warehouse> {
        return warehouses.values.toList()
    }

    fun update(warehouse: Warehouse): Warehouse {
        warehouses[warehouse.id] = warehouse
        return warehouse
    }

    fun delete(id: Int) {
        warehouses.remove(id)
    }

    fun delete(warehouse: Warehouse) {
        warehouses.remove(warehouse.id)
    }
}



