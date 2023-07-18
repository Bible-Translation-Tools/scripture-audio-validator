package org.wycliffeassociates.scriptureaudiovalidator.web.route

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.wycliffeassociates.scriptureaudiovalidator.common.fileprocessor.FileProcessor
import org.wycliffeassociates.scriptureaudiovalidator.common.usecases.FileProcessingRouter
import java.io.File

fun Routing.index() {
    route("/") {
        get {
            call.respond(HttpStatusCode.OK, "Hello, Ktor!")
        }
    }
}