package org.wycliffeassociates.scriptureaudiovalidator.web.route

import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.application.call
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import org.wycliffeassociates.scriptureaudiovalidator.common.usecases.FileProcessingRouter
import java.io.File

fun Routing.index() {
    route("/") {
        get {
            call.respond(HttpStatusCode.OK, "Hello, Ktor!")
        }
    }
    route("/upload") {
        post {
            val multipart = call.receiveMultipart()
            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FileItem -> {
                        // Get the file name and save the file to a location (e.g., "uploads" directory)
                        val fileName = part.originalFileName ?: "unknown_file"
                        val file = File("D:\\misc\\temp\\sav\\$fileName")
                        part.streamProvider().use { input ->
                            file.outputStream().buffered().use { output ->
                                input.copyTo(output)
                            }
                        }

                        val fileProcessor = FileProcessingRouter.build()
                        val results = fileProcessor.handleFiles(listOf(file))
                        call.respond(results)
                    }

                    else -> {
                        part.dispose() // Dispose of the part if it's not a file
                    }
                }
            }
        }
    }
}