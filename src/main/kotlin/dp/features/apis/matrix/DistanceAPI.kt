package dp.features.apis.matrix

import com.typesafe.config.ConfigFactory
import io.ktor.client.request.*

import dp.features.apis.ClientAPI
import dp.model.Distance
import dp.model.Place

class DistanceAPI() {

    private val url: String = ConfigFactory.load().getString("ktor.distanceMatrix.url")
    private val key: String = ConfigFactory.load().getString("ktor.distanceMatrix.apiKey")

    /**
     * Returns the list of DistanceResponse objects based on the given [fromAddress] and [toAddress] parameters
     */
    private suspend fun getDistances(fromAddress: String, toAddress: String): List<DistanceResponse> {
        return ClientAPI().useMany(
            listOf(
                { client ->
                    client.get(url) {
                        parameter("origins", fromAddress)
                        parameter("destinations", toAddress)
                        parameter("mode", "driving")
                        parameter("key", key)
                    }
                },
                { client ->
                    client.get(url) {
                        parameter("origins", fromAddress)
                        parameter("destinations", toAddress)
                        parameter("mode", "transit")
                        parameter("transit_mode", "rail")
                        parameter("transit_routing_preference", "less_walking")
                        parameter("key", key)
                    }
                },
                { client ->
                    client.get(url) {
                        parameter("origins", fromAddress)
                        parameter("destinations", toAddress)
                        parameter("mode", "transit")
                        parameter("transit_mode", "bus")
                        parameter("transit_routing_preference", "less_walking")
                        parameter("key", key)
                    }
                }
            )
        )
    }

    /**
     * Returns the list of Distance objects based on the given [fromPlace] and [toPlace] parameters
     */
    private suspend fun getLand(fromPlace: Place, toPlace: Place): List<Distance> {
        return getDistances(fromPlace.address, toPlace.address).map { result ->
            Distance(
                from = fromPlace,
                to = toPlace,
                distance = result.rows?.getOrNull(0)?.elements?.getOrNull(0)?.distance?.value,
                duration = result.rows?.getOrNull(0)?.elements?.getOrNull(0)?.duration?.value
            )
        }
    }

    /**
     * Returns the list of Distance objects based on the given [fromPlace] and [toPlace] parameters
     */
    private fun getAir(fromPlace: Place, toPlace: Place): List<Distance> {
        return listOf(
            Distance(
                from = fromPlace,
                to = toPlace,
                distance = fromPlace.airPlaneDistanceTo(toPlace),
                duration = fromPlace.airplaneDurationTo(toPlace)
            )
        )
    }

    /**
     * Returns the Distance objects or null based on the given [fromPlace] and [toPlace] parameters
     */
    suspend fun getMinDistance(fromPlace: Place, toPlace: Place): Distance? {
        return try {
            val distances = getLand(fromPlace, toPlace) + getAir(fromPlace, toPlace)
            return distances.minByOrNull { it.duration ?: Int.MAX_VALUE }
        } catch (err: Throwable) {
            println(err)
            null
        }
    }

}
