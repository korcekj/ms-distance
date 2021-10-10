package dp.features.apis

import io.ktor.client.*
import io.ktor.client.features.json.*
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class ClientAPI {

    /**
     * Returns the HTTP Client along with the JSON serializer
     */
    private val client: HttpClient get() = HttpClient() {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
    }

    /**
     * Returns the value of type [T] based on the given [callback] parameter
     */
    suspend fun<T> useOne(callback: suspend (client: HttpClient) -> T): T {
        return useMany(listOf(callback))[0]
    }

    /**
     * Returns the list of values of type [T] based on the given [callbacks] parameter
     */
    suspend fun<T> useMany(callbacks: List<suspend (client: HttpClient) -> T>): List<T> {
        val responses = callbacks.map { it(client) }
        client.close()
        return responses
    }

}