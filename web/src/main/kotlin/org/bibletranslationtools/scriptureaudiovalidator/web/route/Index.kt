package org.bibletranslationtools.scriptureaudiovalidator.web.route

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
import org.bibletranslationtools.scriptureaudiovalidator.common.data.FileStatus
import org.bibletranslationtools.scriptureaudiovalidator.common.data.SerializableFileResult
import org.bibletranslationtools.scriptureaudiovalidator.common.usecases.FileProcessingRouter
import org.slf4j.LoggerFactory
import java.io.File
import java.util.*

private val logger = LoggerFactory.getLogger(Routing::index.javaClass)
private val uploadDir = File(System.getenv("UPLOAD_DIR"))
private val fileProcessor = FileProcessingRouter.build()

fun Routing.index() {
    route("/") {
        get {
            call.respond(HttpStatusCode.OK, "Hello, Ktor!")
        }
    }
    route("/upload") {
        post {
            val multipart = call.receiveMultipart()
            val filesReport = mutableListOf<SerializableFileResult>()
            val dir = uploadDir.resolve(UUID.randomUUID().toString()).apply { mkdir() }

            multipart.forEachPart { filePart ->
                when (filePart) {
                    is PartData.FileItem -> {
                        // Get the file name and save the file to a location (e.g., "uploads" directory)
                        val fileName = filePart.originalFileName ?: "unknown_file"
                        val file = dir.resolve(fileName)
                        filePart.streamProvider().use { input ->
                            file.outputStream().buffered().use { output ->
                                input.copyTo(output) // saves the uploaded file stream
                            }
                        }

                        val processedFiles = processFile(file)
                        filesReport.addAll(processedFiles)
                    }
                    else -> {
                        filePart.dispose() // Dispose of the part if it's not a file
                    }
                }
            }

            call.respond(filesReport)
            dir.deleteRecursively()
        }
    }
}

/**
 * Processes the uploaded file individually to avoid having an error
 * blocking the entire batch.
 */
private fun processFile(file: File): List<SerializableFileResult> {
    return try {
        fileProcessor.handleFiles(listOf(file))
            .map { result ->
                SerializableFileResult(result) // hide internal storage path
            }
    } catch (e: Exception) {
        logger.error("An error occurred while processing $file.", e)
        listOf(
            SerializableFileResult(FileStatus.REJECTED, file.name, "Error")
        )
    }
}