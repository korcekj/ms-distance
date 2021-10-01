package dp

import com.typesafe.config.ConfigFactory
import io.ktor.server.netty.EngineMain
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*

import org.koin.ktor.ext.Koin
import org.koin.dsl.module
import org.koin.experimental.builder.single
import org.koin.experimental.builder.singleBy

import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

import dp.routes.*
import dp.service.DistanceService
import dp.service.DistanceServiceInf

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.module() {
    // JSON serialization
    install(ContentNegotiation) {
        gson()
    }
    // CORS
    install(CORS) {
        method(HttpMethod.Get)
        anyHost()
    }
    // Koin
    install(Koin) {
        modules(modules)
    }
    // Routes
    distanceRoute()
}

// Koin modules
val modules = module(createdAtStart = true) {
    singleBy<DistanceServiceInf, DistanceService>()
    single {
        KMongo.createClient(
            ConfigFactory.load().getString("ktor.mongoDB.uri")
        ).coroutine
    }
}
