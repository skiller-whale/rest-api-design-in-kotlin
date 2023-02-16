package stock_management.routers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import stock_management.daos.StockInstanceDao
import stock_management.Warehouse
import stock_management.daos.WarehouseDao

fun Routing.warehouseRouter(
    warehouseDao: WarehouseDao,
    stockInstanceDao: StockInstanceDao
) {
    route("/warehouses") {
        get {
            val warehouses = warehouseDao.getAll()
            call.respond(warehouses)
        }

        get("/{warehouse-id}") {
            val id = call.parameters["warehouse-id"]!!

            val warehouse = warehouseDao.getById(id)
            if (warehouse != null) {
                call.respond(warehouse)
                call.response.status(HttpStatusCode.OK)
            } else {
                call.response.status(HttpStatusCode.NotFound)
            }
        }

        put("/{warehouse-id}") {
            val id = call.parameters["warehouse-id"]!!

            val existingWarehouse = warehouseDao.getById(id)
            val warehouse = call.receive<Warehouse>()
            if (existingWarehouse != null) {
                call.respond(warehouseDao.update(warehouse))
                call.response.status(HttpStatusCode.OK)
            } else {
                call.respond(warehouseDao.create(warehouse))
                call.response.status(HttpStatusCode.Created)
            }
        }

        delete("/{warehouse-id}") {
            val id = call.parameters["warehouse-id"]!!

            val warehouse = warehouseDao.getById(id)
            if (warehouse != null) {

                //Delete all stock in the warehouse.
                val stockInstances = stockInstanceDao.getForWarehouse(id)
                for (stockInstance in stockInstances) {
                    stockInstanceDao.delete(stockInstance)
                }

                warehouseDao.delete(warehouse)
                call.respond(warehouse)
                call.response.status(HttpStatusCode.OK)

            } else {
                call.response.status(HttpStatusCode.NotFound)
            }
        }
    }
}