package stock_management.REST.representations

import stock_management.ROOT_URL
import stock_management.REST.daos.Warehouse
import stock_management.REST.daos.WarehouseAttributes

fun warehouseRepresentation(warehouse: Warehouse): Any {
    return object {
        val id = warehouse.id
        val attributes = WarehouseAttributes(
            name = warehouse.name,
            address = warehouse.address,
            // manager = warehouse.manager,
        )
        val links = object {
            val self = "$ROOT_URL/rest/warehouses/${id}"
            val stockInstances = "$ROOT_URL/rest/warehouses/${id}/stock-instances"
        }
    }
}