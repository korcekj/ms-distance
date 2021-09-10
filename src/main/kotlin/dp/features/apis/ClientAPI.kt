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
    fun<T> useOne(callback: suspend (client: HttpClient) -> T): T = runBlocking {
        return@runBlocking useMany(listOf(callback))[0]
    }

    /**
     * Returns the list of values of type [T] based on the given [callbacks] parameter
     */
    fun<T> useMany(callbacks: List<suspend (client: HttpClient) -> T>): List<T> = runBlocking {
        val responses = callbacks.map { async { it(client) } }
        client.close()
        return@runBlocking responses.map { it.await() }
    }

}