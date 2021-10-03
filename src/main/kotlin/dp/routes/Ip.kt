package dp.routes

import dp.service.IpServiceInf

import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

import org.koin.ktor.ext.inject

fun Application.ipRoute() {

    // Initiate Ip service
    val ipService by inject<IpServiceInf>()

    routing {
        // Ip routes
        get("/ip") {
            val fromIp = call.request.queryParameters["from"] ?: ""
            val toIp = call.request.queryParameters["to"] ?: ""

            val distance = ipService.getDistance(fromIp, toIp)
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Distance can't be calculated")

            return@get call.respond(distance)
        }
    }

}
