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

//I'm resisting the urge to do something clever to avoid retyping of all the attributes, but this is simple, explicit, and flexible.
//For exercises where we don't have an attributes object, this function will change.
//TODO - OPTIONS - this is maybe more flexible in general, if we don't have nested attributes, or if they are split into relations and attributes.
//fun warehouseRepresentation(warehouse: Warehouse): Any {
//    return object {
//        val id = warehouse.id
//        val attributes = object {
//            val name = warehouse.name
//        }
//        val links = object {
//            val self = "${ROOT_URL}/warehouses/${id}"
//            val stock = "${ROOT_URL}/warehouses/${id}/stock-lines"
//        }
//    }
//}