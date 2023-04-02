package stock_management.daos

import java.util.concurrent.atomic.AtomicInteger

data class Warehouse(val id: Int, val name: String, val address: String) {
    constructor(id: Int, attributes: WarehouseAttributes) : this(id, attributes.name, attributes.address)
}
// data class Warehouse(val id: Int, val name: String, val address: String, val manager: String) {
//     constructor(id: Int, attributes: WarehouseAttributes) : this(id, attributes.name, attributes.address, attributes.manager)
// }

data class WarehouseAttributes(val name: String, val address: String)
// data class WarehouseAttributes(val name: String, val address: String, val manager: String)

/**
 * The Data Access Object (DAO) for Warehouses - In real life this would be the layer that interacted with the database,
 * for example it might contain SQL queries. For this example, however, it stores data in a hashmap in memory.
 */
class WarehouseDao {
    private val warehouses = hashMapOf<Int, Warehouse>()

    private var lastId: AtomicInteger = AtomicInteger(0)

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

    fun addStartingData() {
        val startingData = arrayOf(
            Triple("Aldwalk", "10 Barley Road", "Shark Ruffalo"),
            Triple("Bedale", "3 Riverside Court", "Skate Winslet"),
            Triple("Haworth", "The Parsonage, Church Street", "Arctic Charlotte Brontë"),
            Triple("Heworth", "124 South Drive", "Judi Tench"),
            Triple("Ilkley", "37 Moorfield Crescent", "Mark Whaleberg"),
            Triple("Morley", "35 Scrogg Lane", "Sharkimedes"),
            Triple("Osset", "198 The Burrows", "Roe-pert Grint"),
            Triple("Pickering", "65 Brookside View", "Sandra Pollock"),
            Triple("Pocklington", "176 Pocket Handkerchief Lane", "Ernest Herringway"),
            Triple("Ripon", "30 Who-the-what-now-gate", "Agatha Fishtie"),
            Triple("Selby", "127 Cheese Gate Nab Side", "Pilchard Gere"),
            Triple("Sheffield", "5 North Road", "Sean Bream"),
            Triple("Skipton", "1984 Manor Farm Road", "George Orwhale"),
            Triple("Tadcaster", "294 Blossom Street", "Eel Pacino"),
            Triple("Wakefield", "106 Alma Terrace", "Christian Whale"),
        )

        for ((name, address, manager) in startingData) {
            this.create(WarehouseAttributes(name, address))
            // this.create(WarehouseAttributes(name, address, manager))
        }
    }
}
