package stock_management.representations

import stock_management.ROOT_URL
import stock_management.Warehouse
import stock_management.WarehouseAttributes

fun warehouseRepresentation(warehouse: Warehouse): Any {
    return object {
        val id = warehouse.id
        val attributes = WarehouseAttributes(name = warehouse.name, address = warehouse.address)
        val links = object {
            val self = "$ROOT_URL/warehouses/${id}"
            val stock = "$ROOT_URL/warehouses/${id}/stock-lines"
        }
    }
}
