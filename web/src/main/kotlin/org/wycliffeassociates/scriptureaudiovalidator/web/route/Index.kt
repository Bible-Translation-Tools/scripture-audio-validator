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
import org.wycliffeassociates.scriptureaudiovalidator.common.data.FileResult
import org.wycliffeassociates.scriptureaudiovalidator.common.data.FileStatus
import org.wycliffeassociates.scriptureaudiovalidator.common.io.VersificationReader
import org.wycliffeassociates.scriptureaudiovalidator.common.usecases.FileProcessingRouter
import org.wycliffeassociates.scriptureaudiovalidator.common.usecases.VersificationChecker
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
            val reportList = mutableListOf<FileResult>()
            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FileItem -> {
                        // Get the file name and save the file to a location (e.g., "uploads" directory)
                        val fileName = part.originalFileName ?: "unknown_file"
                        val file = File("E:\\miscs\\temp\\sav\\$fileName")
                        part.streamProvider().use { input ->
                            file.outputStream().buffered().use { output ->
                                input.copyTo(output)
                            }
                        }

                        val fileProcessor = FileProcessingRouter.build()
                        val processedFiles = fileProcessor.handleFiles(listOf(file))
                        reportList.addAll(processedFiles.filter { it.status == FileStatus.REJECTED })

                        val firstPass = processedFiles.filter { it.status == FileStatus.PROCESSED }
                        with(VersificationChecker(VersificationReader().read())) {
                            val verifiedItems = firstPass.map {
                                val checkResult = check(it.data!!)
                                it.copy(status = checkResult.status, message = checkResult.message)
                            }
                            reportList.addAll(verifiedItems)
                        }
                    }

                    else -> {
                        part.dispose() // Dispose of the part if it's not a file
                    }
                }
            }

            call.respond(reportList)
        }
    }
}