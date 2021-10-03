package dp.features.apis.ipwhois

import com.typesafe.config.ConfigFactory
import dp.features.apis.ClientAPI
import io.ktor.client.request.*

import dp.model.Place

class IpwhoisAPI {

    private val url: String = ConfigFactory.load().getString("ktor.ipwhois.uri")

    /**
     * Returns the GeocodeResponse object based on the given [ip]
     */
    private fun getLocation(ip: String): IpwhoisReponse {
        return ClientAPI().useOne { client ->
            client.get(url) {
                url {
                    path(listOf("json", ip))
                }
            }
        }
    }

    /**
     * Returns the Place object or null based on the given [ip]
     */
    fun getPlace(ip: String): Place? {
        return try {
            val location = getLocation(ip)
            Place(
                address = location.city!!,
                lat = location.latitude,
                lng = location.longitude,
            )
        } catch (err: Throwable) {
            println(err)
            null
        }
    }

}