package dp.features.apis

import io.ktor.client.*
import io.ktor.client.features.json.*
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class ClientAPI {

    private val client: HttpClient get() = HttpClient() {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
    }

    fun<T> useOne(callback: suspend (client: HttpClient) -> T): T = runBlocking {
        return@runBlocking useMany(listOf(callback))[0]
    }

    fun<T> useMany(callbacks: List<suspend (client: HttpClient) -> T>): List<T> = runBlocking {
        val responses = callbacks.map { async { it(client) } }
        client.close()
        return@runBlocking responses.map { it.await() }
    }

}