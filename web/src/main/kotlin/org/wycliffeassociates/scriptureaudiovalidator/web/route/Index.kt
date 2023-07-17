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
    route("/upload") {
        post {
            val multipart = call.receiveMultipart()
            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FileItem -> {
                        // Get the file name and save the file to a location (e.g., "uploads" directory)
                        val fileName = part.originalFileName ?: "unknown_file"
                        val file = File("path/$fileName")
                        part.streamProvider().use { input ->
                            file.outputStream().buffered().use { output ->
                                input.copyTo(output)
                            }
                        }

                        val fileProcessor = FileProcessingRouter.build()
                        val results = fileProcessor.handleFiles(listOf(file))
//                        call.respond(results)
                        println(results)
                    }

                    else -> {
                        part.dispose() // Dispose of the part if it's not a file
                    }
                }
            }
        }
    }
}