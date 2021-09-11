package dp.routes

import dp.service.DistanceService

import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

import org.koin.ktor.ext.inject

import org.litote.kmongo.coroutine.CoroutineClient

fun Application.distanceRoute() {

    // Initiate DB connection
    val mongoClient: CoroutineClient by inject()
    // Initiate Distance service
    val distanceService = DistanceService(mongoClient)

    routing {
        // Distance routes
        get("/distance") {
            val fromAddress = call.request.queryParameters["from"] ?: ""
            val toAddress = call.request.queryParameters["to"] ?: ""

            val distance = distanceService.getDistance(fromAddress, toAddress)
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Distance can't be calculated")

            return@get call.respond(distance)
        }
    }

}
