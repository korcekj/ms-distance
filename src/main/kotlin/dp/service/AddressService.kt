package dp.service

import org.litote.kmongo.coroutine.CoroutineDatabase

import dp.features.apis.geocode.GeocodeAPI
import dp.features.apis.matrix.DistanceAPI
import dp.model.Distance
import dp.model.Place
import kotlinx.coroutines.runBlocking

import org.litote.kmongo.and

import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.litote.kmongo.or

interface AddressServiceInf {
    fun createDistance(from: String, to: String): Distance?
    fun getDistance(from: String, to: String): Distance?
}

class AddressService(
    mongoDatabase: CoroutineDatabase
) : AddressServiceInf {

    // Initiate mongoDB collection
    private var collection = mongoDatabase.getCollection<Distance>()

    // Initiate distanceMatrix API
    private val distanceAPI = DistanceAPI()

    // Initiate geocode API
    private val geocodeAPI = GeocodeAPI()

    /**
     * Returns the Distance object or null based on the given [from] and [to] parameters
     */
    override fun createDistance(from: String, to: String): Distance? {
        if (from.isEmpty() || to.isEmpty()) return null

        val fromPlace = geocodeAPI.getPlace(from) ?: return null
        val toPlace = geocodeAPI.getPlace(to) ?: return null
        val distance = distanceAPI.getMinDistance(fromPlace, toPlace) ?: return null

        return runBlocking {
            try {
                // Insert document to the database
                collection.insertOne(distance)
                distance
            } catch (err: Throwable) {
                println(err)
                null
            }
        }
    }

    /**
     * Returns the Distance object or null based on the given [from] and [to] parameters
     */
    override fun getDistance(from: String, to: String): Distance? {
        if (from.isEmpty() || to.isEmpty()) return null

        return runBlocking {
            try {
                // Find distance based on the given addresses
                val distance = collection.findOne(
                    or(
                        and(Distance::from / Place::address eq from, Distance::to / Place::address eq to),
                        and(Distance::from / Place::address eq to, Distance::to / Place::address eq from)
                    )
                )

                distance ?: createDistance(from, to)
            } catch (err: Throwable) {
                println(err)
                null
            }
        }
    }

}