package org.bibletranslationtools.scriptureaudiovalidator.web

import io.ktor.serialization.gson.gson
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.routing.*
import org.bibletranslationtools.scriptureaudiovalidator.web.route.index

fun Application.module() {
    install(CORS) {
        anyHost()
    }
    install(ContentNegotiation) {
        gson()
    }
    install(Routing) {
        index()
    }
}

fun main() {
    val host = "localhost" // Bind to all network interfaces
    val port = 8080

    val server = embeddedServer(Netty, host = host, port = port, module = Application::module)
    println("Ktor server has started at http://$host:$port/")
    server.start(wait = true)
}
