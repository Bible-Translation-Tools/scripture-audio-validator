package org.wycliffeassociates.scriptureaudiovalidator.web

import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import org.wycliffeassociates.scriptureaudiovalidator.web.route.index

fun Application.module() {
    install(ContentNegotiation)

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
