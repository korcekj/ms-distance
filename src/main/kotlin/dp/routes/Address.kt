package dp.routes

import dp.service.DistanceServiceInf

import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

import org.koin.ktor.ext.inject

fun Application.addressRoute() {

    // Initiate Address service
    val addressService by inject<DistanceServiceInf>()

    routing {
        // Distance routes
        get("/address") {
            val fromAddress = call.request.queryParameters["from"] ?: ""
            val toAddress = call.request.queryParameters["to"] ?: ""

            val distance = addressService.getDistance(fromAddress, toAddress)
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Distance can't be calculated")

            return@get call.respond(distance)
        }
    }

}
