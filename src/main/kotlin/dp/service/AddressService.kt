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
    fun findPairOfPlaces(p1: String, p2: String): Pair<Place?, Place?>
    fun findDistance(from: Place, to: Place): Distance?
    fun createDistance(from: Place, to: Place): Distance?
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
     * Returns the Pair of Place objects or the Pair of null based on the given [p1] and [p2] parameters
     */
    override fun findPairOfPlaces(p1: String, p2: String): Pair<Place?, Place?> {
        if (p1.isEmpty() || p2.isEmpty()) return Pair(null, null)

        val fromPlace = geocodeAPI.getPlace(p1)
        val toPlace = geocodeAPI.getPlace(p2)
        return Pair(fromPlace, toPlace)
    }

    /**
     * Returns the Distance object or null based on the given [from] and [to] parameters
     */
    override fun findDistance(from: Place, to: Place): Distance? {
        return runBlocking {
            try {
                // Find distance based on the calculated places
                collection.findOne(
                    or(
                        and(
                            Distance::from / Place::address eq from.address,
                            Distance::to / Place::address eq to.address
                        ),
                        and(
                            Distance::from / Place::address eq to.address,
                            Distance::to / Place::address eq from.address
                        )
                    )
                )
            } catch (err: Throwable) {
                println(err)
                null
            }
        }
    }

    /**
     * Returns the Distance object or null based on the given [from] and [to] parameters
     */
    override fun createDistance(from: Place, to: Place): Distance? {
        // Calculate the minimal distance
        val distance = distanceAPI.getMinDistance(from, to) ?: return null

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
        val (fromPlace, toPlace) = findPairOfPlaces(from, to)

        if (fromPlace == null || toPlace == null) return null

        return findDistance(fromPlace, toPlace) ?: createDistance(fromPlace, toPlace)
    }

}