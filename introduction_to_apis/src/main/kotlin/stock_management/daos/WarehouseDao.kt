package stock_management.daos

import stock_management.Warehouse

/**
 * The Data Access Object (DAO) for Warehouses - In real life this would be the layer that interacted with the database,
 * for example it might contain SQL queries. For this example, however, it stores data in a hashmap in memory.
 */
class WarehouseDao {
    private val warehouses = hashMapOf<String, Warehouse>()

    fun create(warehouse: Warehouse): Warehouse {
        warehouses[warehouse.id] = warehouse
        return warehouse
    }

    fun getById(id: String): Warehouse? {
        return warehouses[id]
    }

    fun getAll(): List<Warehouse> {
        return warehouses.values.toList()
    }

    fun update(warehouse: Warehouse): Warehouse {
        warehouses[warehouse.id] = warehouse
        return warehouse
    }

    fun delete(warehouse: Warehouse) {
        warehouses.remove(warehouse.id)
    }
}



