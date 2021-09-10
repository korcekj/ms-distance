package dp.features.apis.geocode

import com.typesafe.config.ConfigFactory
import io.ktor.client.request.*

import dp.features.apis.ClientAPI
import dp.model.Place

class GeocodeAPI {

    private val url: String = ConfigFactory.load().getString("ktor.geocode.uri")
    private val key: String = ConfigFactory.load().getString("ktor.geocode.apiKey")

    /**
     * Returns the GeocodeResponse object based on the given [address]
     */
    private fun getLocation(address: String): GeocodeResponse {
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
    fun getPlace(address: String): Place? {
        return try {
            val location = getLocation(address)
            Place(
                address = address,
                lat = location.results[0]?.geometry?.location?.lat,
                lng = location.results[0]?.geometry?.location?.lng,
            )
        } catch (err: Throwable) {
            println(err)
            null
        }
    }

}