package dp.routes

import dp.features.apis.geocode.*
import dp.features.apis.matrix.*
import dp.model.Distance
import dp.model.Place

import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

import org.koin.ktor.ext.inject

import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.and
import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.litote.kmongo.or

fun Application.distanceRoute() {

    // Initiate DB connection
    val mongoClient: CoroutineClient by inject()
    // Initiate distanceMatrix API
    val distanceAPI = DistanceAPI()
    // Initiate geocode API
    val geocodeAPI = GeocodeAPI()

    routing {
        // Distance routes
        get("/distance") {
            val fromAddress = call.request.queryParameters["from"] ?: ""
            val toAddress = call.request.queryParameters["to"] ?: ""

            if (fromAddress.isEmpty() || toAddress.isEmpty()) {
                return@get call.respond(HttpStatusCode.BadRequest, "Query arguments are missing")
            }

            // Declare Distance collection
            val collection = mongoClient
                .getDatabase("ms-distance-dev")
                .getCollection<Distance>("distance")

            // Find document by query arguments
            var distance = collection
                .findOne(
                    or(
                        and(Distance::from / Place::address eq fromAddress, Distance::to / Place::address eq toAddress),
                        and(Distance::from / Place::address eq toAddress, Distance::to / Place::address eq fromAddress)
                    )
                )

            if (distance != null) {
                return@get call.respond(distance)
            }

            // Calculate place positions based on the given addresses
            val fromPlace = geocodeAPI.getPlace(fromAddress)
            val toPlace = geocodeAPI.getPlace(toAddress)

            if (fromPlace == null || toPlace == null) {
                return@get call.respond(HttpStatusCode.NotFound, "Provided addresses were not found")
            }

            // Find the minimal time distance
            distance = distanceAPI.getMinDuration(fromPlace, toPlace)

            if (distance == null) {
                return@get call.respond(HttpStatusCode.NotFound, "Minimal duration could not be computed")
            }

            // Insert document to the database
            collection.insertOne(distance)

            return@get call.respond(distance)
        }
    }

}
