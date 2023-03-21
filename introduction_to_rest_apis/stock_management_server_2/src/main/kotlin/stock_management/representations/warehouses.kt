package stock_management.representations

import stock_management.ROOT_URL
import stock_management.daos.Warehouse
import stock_management.daos.WarehouseAttributes

fun warehouseRepresentation(warehouse: Warehouse): Any {
    return object {
        val id = warehouse.id
        val attributes = WarehouseAttributes(
            name = warehouse.name,
            address = warehouse.address,
            // manager = warehouse.manager,
        )
        val links = object {
            val self = "$ROOT_URL/warehouses/${id}"
            val stockInstances = "$ROOT_URL/warehouses/${id}/stock-instances"
        }
    }
}