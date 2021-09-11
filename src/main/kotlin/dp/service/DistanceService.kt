package dp.service

import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineCollection

import dp.features.apis.geocode.GeocodeAPI
import dp.features.apis.matrix.DistanceAPI
import dp.model.Distance
import dp.model.Place
import kotlinx.coroutines.runBlocking

import org.litote.kmongo.and
import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.litote.kmongo.or

class DistanceService(
    mongoClient: CoroutineClient
) {

    private var collection: CoroutineCollection<Distance>
    // Initiate distanceMatrix API
    private val distanceAPI = DistanceAPI()
    // Initiate geocode API
    private val geocodeAPI = GeocodeAPI()

    init {
        // Declare Distance collection
        collection = mongoClient
            .getDatabase("ms-distance-dev")
            .getCollection("distance")
    }

    /**
     * Returns the Distance object or null based on the given [fromAddress] and [toAddress] parameters
     */
    fun createDistance(fromAddress: String, toAddress: String): Distance? = runBlocking {
        if (fromAddress.isEmpty() || toAddress.isEmpty()) return@runBlocking null

        val fromPlace = geocodeAPI.getPlace(fromAddress) ?: return@runBlocking null
        val toPlace = geocodeAPI.getPlace(toAddress) ?: return@runBlocking null
        val distance = distanceAPI.getMinDistance(fromPlace, toPlace) ?: return@runBlocking null

        return@runBlocking try {
            // Insert document to the database
            collection.insertOne(distance)

            distance
        } catch (err: Throwable) {
            println(err)
            null
        }
    }

    /**
     * Returns the Distance object or null based on the given [fromAddress] and [toAddress] parameters
     */
    fun getDistance(fromAddress: String, toAddress: String): Distance? = runBlocking {
        if (fromAddress.isEmpty() || toAddress.isEmpty()) return@runBlocking null

        return@runBlocking try {
            // Find distance based on the given addresses
            val distance = collection.findOne(
                or(
                    and(Distance::from / Place::address eq fromAddress, Distance::to / Place::address eq toAddress),
                    and(Distance::from / Place::address eq toAddress, Distance::to / Place::address eq fromAddress)
                )
            )

            distance ?: createDistance(fromAddress, toAddress)
        } catch (err: Throwable) {
            println(err)
            null
        }
    }

}