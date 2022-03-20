package dp

import com.typesafe.config.ConfigFactory
import dp.routes.addressRoute
import dp.routes.ipRoute
import dp.service.AddressService
import dp.service.AddressServiceInf
import dp.service.IpService
import dp.service.IpServiceInf
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.server.netty.*
import org.koin.dsl.module
import org.koin.experimental.builder.singleBy
import org.koin.ktor.ext.Koin
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo


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
    addressRoute()
    ipRoute()
}

// Koin modules
val modules = module(createdAtStart = true) {
    singleBy<AddressServiceInf, AddressService>()
    singleBy<IpServiceInf, IpService>()
    single {
        KMongo.createClient(
            ConfigFactory.load().getString("database.mongoDB.url")
        )
            .coroutine
            .getDatabase(ConfigFactory.load().getString("database.mongoDB.database"))
    }
}
