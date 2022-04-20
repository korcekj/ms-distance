package dp.features.apis.geocode

import com.typesafe.config.ConfigFactory
import dp.features.apis.ClientAPI
import dp.model.Place
import io.ktor.client.request.*

class GeocodeAPI {

    private val url: String = ConfigFactory.load().getString("apis.geocode.url")
    private val key: String = ConfigFactory.load().getString("apis.geocode.apiKey")

    /**
     * Returns the GeocodeResponse object based on the given [address]
     */
    private suspend fun getLocation(address: String): GeocodeResponse {
        return ClientAPI().useOne { client ->
            client.get(url) {
                parameter("address", address)
                parameter("key", key)
            }
        }
    }

    /**
     * Returns the Place object or null based on the given [address]
     */
    suspend fun getPlace(address: String): Place? {
        return try {
            val location = getLocation(address)
            when(location.status) {
                "OK" -> Place(
                    address = "${location.results[0]?.formattedAddress()}",
                    lat = location.results[0]?.geometry?.location?.lat,
                    lng = location.results[0]?.geometry?.location?.lng,
                )
                else -> null
            }
        } catch (err: Throwable) {
            println(err)
            null
        }
    }

}