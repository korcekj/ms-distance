package dp.service

import org.litote.kmongo.coroutine.CoroutineDatabase

import dp.features.apis.ipwhois.IpwhoisAPI
import dp.features.apis.matrix.DistanceAPI
import dp.model.Distance
import dp.model.Place

import kotlinx.coroutines.runBlocking

import org.litote.kmongo.and
import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.litote.kmongo.or

interface IpServiceInf {
    fun createDistance(from: String, to: String): Distance?
    fun getDistance(from: String, to: String): Distance?
}

class IpService(
    mongoDatabase: CoroutineDatabase
) : IpServiceInf {

    // Initiate mongoDB collection
    private var collection = mongoDatabase.getCollection<Distance>()

    // Initiate distanceMatrix API
    private val distanceAPI = DistanceAPI()

    // Initiate ip to location API
    private val ipwhoisAPI = IpwhoisAPI()

    /**
     * Returns the Distance object or null based on the given [from] and [to] parameters
     */
    override fun createDistance(from: String, to: String): Distance? {
        if (from.isEmpty() || to.isEmpty()) return null

        val fromPlace = ipwhoisAPI.getPlace(from) ?: return null
        val toPlace = ipwhoisAPI.getPlace(to) ?: return null
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