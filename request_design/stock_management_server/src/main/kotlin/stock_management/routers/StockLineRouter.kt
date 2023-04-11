package stock_management.routers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import stock_management.StockLine
import stock_management.StockLineAttributes
import stock_management.TemplateProperty
import stock_management.daos.StockInstanceDao
import stock_management.daos.StockLineDao
import stock_management.representations.stockLineRepresentation
import stock_management.response

// utility function for calculating an MD5 of a string - useful for ETag calculation
fun String.md5(): String {
    return java.math.BigInteger(1,
        java.security.MessageDigest.getInstance("MD5").
        digest(toByteArray())).toString(16).padStart(32, '0')
}

fun Routing.stockLineRouter(
    stockLineDao: StockLineDao,
    stockInstanceDao: StockInstanceDao
) {
    route("/stock-lines") {

        get {
            val stockLines = stockLineDao.getAll()

            call.response.header("Allow", "GET, POST")
            call.response.header("ETag", "W/"+stockLines.toString().md5())
            call.respond(
                message = object {
                    val data = stockLines.map(::stockLineRepresentation)
                },
                status = HttpStatusCode.OK
            )
        }

        post {
            val stockLineAttributes: StockLineAttributes
            try {
                stockLineAttributes = call.receive<StockLineAttributes>()
            } catch (e: BadRequestException) {
                call.response.header("Allow", "GET, POST")
                call.respond(
                    message = object {
                        val error = "Invalid stock line data in request body."
                    },
                    status = HttpStatusCode.UnprocessableEntity
                )
                return@post
            }

            if (call.request.headers["If-Match"] == null) {
                call.respond(
                    message = object {
                       val error = "Must supply If-Match when POSTing to a collection"
                    },
                    status = HttpStatusCode.PreconditionFailed
                )
                return@post
            }

            if (call.request.headers["If-Match"] != "W/"+stockLineDao.getAll().toString().md5()) {
                call.respond(
                    message = object {
                        val error = "Client state doesn't match"
                    },
                    status = HttpStatusCode.PreconditionFailed
                )
                return@post
            }

            val createdStockLine = stockLineDao.create(stockLineAttributes)

            call.response.header("Allow", "GET, POST")
            call.respond(
                message = object {
                    val data = stockLineRepresentation(createdStockLine)
                },
                status = HttpStatusCode.Created
            )
        }

        route("/{stockLine-id}") {

            fun allowedMethods(stockLineId: Int) =
                if (stockInstanceDao.doesStockExist(stockLineId)) "GET, PUT" else "GET, PUT, DELETE"

            get {
                val id = call.parameters["stockLine-id"]!!.toInt()

                val stockLine = stockLineDao.getById(id)
                if (stockLine != null) {
                    //
                    // Add ETag header here - you can use "Foo".md5() on any String to calculate an MD5
                    //
                    call.response.header("Allow", allowedMethods(id))
                    call.respond(
                        message = object {
                            val data = stockLineRepresentation(stockLine)
                        },
                        status = HttpStatusCode.OK
                    )
                } else {
                    call.response.status(HttpStatusCode.NotFound)
                }
            }

            put {
                val id = call.parameters["stockLine-id"]!!.toInt()

                val existingStockLine = stockLineDao.getById(id)

                val stockLineAttributes: StockLineAttributes
                try {
                    stockLineAttributes = call.receive<StockLineAttributes>()
                } catch (e: BadRequestException) {
                    call.response.header("Allow", allowedMethods(id))
                    call.respond(
                        message = object {
                            val error = "Invalid stock line data in request body."
                        },
                        status = HttpStatusCode.UnprocessableEntity
                    )
                    return@put
                }

                val updatedStockLine = if (existingStockLine != null) {
                    stockLineDao.update(StockLine(id, stockLineAttributes))
                } else {
                    stockLineDao.createWithId(stockLineAttributes, id)
                }

                call.response.header("Allow", allowedMethods(id))
                call.respond(
                    message = object {
                        val data = stockLineRepresentation(updatedStockLine)
                    },
                    status = HttpStatusCode.OK
                )
            }

            delete {
                val id = call.parameters["stockLine-id"]!!.toInt()

                val stockLine = stockLineDao.getById(id)
                if (stockLine != null) {
                    if (!stockInstanceDao.doesStockExist(id)) {
                        stockLineDao.delete(id)
                        call.response.status(HttpStatusCode.NoContent)
                    } else {
                        call.response.status(HttpStatusCode.MethodNotAllowed)
                        call.response.header("Allow", allowedMethods(id))
                        call.respond(
                            object {
                                val error = "You can only delete stock lines that have no stock"
                            }
                        )
                    }
                } else {
                    call.response.status(HttpStatusCode.NotFound)
                }
            }
        }
    }
}
