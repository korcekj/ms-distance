package dp.features.apis.ipwhois

import com.typesafe.config.ConfigFactory
import dp.features.apis.ClientAPI
import io.ktor.client.request.*

import dp.model.Place

class IpwhoisAPI {

    private val url: String = ConfigFactory.load().getString("ktor.ipwhois.url")

    /**
     * Returns the IpwhoisReponse object based on the given [ip]
     */
    private suspend fun getLocation(ip: String): IpwhoisReponse {
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
    suspend fun getPlace(ip: String): Place? {
        return try {
            val location = getLocation(ip)
            when(location.success) {
                true -> Place(
                    address = "${location.city}, ${location.countryCode}",
                    lat = location.latitude,
                    lng = location.longitude,
                )
                else -> null
            }
        } catch (err: Throwable) {
            println(err)
            null
        }
    }

}