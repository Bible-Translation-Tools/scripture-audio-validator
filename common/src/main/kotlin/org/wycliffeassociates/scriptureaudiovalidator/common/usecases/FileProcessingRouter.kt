package org.wycliffeassociates.scriptureaudiovalidator.common.usecases

import org.wycliffeassociates.scriptureaudiovalidator.common.data.FileResult
import org.wycliffeassociates.scriptureaudiovalidator.common.data.FileStatus
import org.wycliffeassociates.scriptureaudiovalidator.common.extensions.MediaExtensions
import org.wycliffeassociates.scriptureaudiovalidator.common.fileprocessor.CueProcessor
import org.wycliffeassociates.scriptureaudiovalidator.common.fileprocessor.FileProcessor
import org.wycliffeassociates.scriptureaudiovalidator.common.fileprocessor.JpgProcessor
import org.wycliffeassociates.scriptureaudiovalidator.common.fileprocessor.Mp3Processor
import org.wycliffeassociates.scriptureaudiovalidator.common.fileprocessor.OratureFileProcessor
import org.wycliffeassociates.scriptureaudiovalidator.common.fileprocessor.TrProcessor
import org.wycliffeassociates.scriptureaudiovalidator.common.fileprocessor.WavProcessor
import org.wycliffeassociates.scriptureaudiovalidator.common.io.VersificationReader
import java.io.File
import java.io.IOException
import java.util.Queue
import java.util.LinkedList

class FileProcessingRouter(private val processors: List<FileProcessor>) {
    private val fileQueue: Queue<File> = LinkedList<File>()
    private val checker = VersificationChecker(VersificationReader())

    @Throws(IOException::class)
    fun handleFiles(files: List<File>): List<FileResult> {
        val resultList = mutableListOf<FileResult>()
        fileQueue.addAll(files)

        while (fileQueue.isNotEmpty()) {
            processFile(fileQueue.poll(), resultList)
        }

        val firstPass = resultList.filter {
            it.status == FileStatus.PROCESSED &&
                MediaExtensions.of(it.file.extension) == MediaExtensions.WAV
        }
        val secondPass = mutableListOf<FileResult>()

        val verifiedItems = firstPass.map {
            val checkResult = checker.check(it.data!!)
            it.copy(status = checkResult.status, message = checkResult.message)
        }
        secondPass.addAll(verifiedItems)

        return secondPass + resultList.filter {
            it.status == FileStatus.REJECTED || MediaExtensions.of(it.file.extension) != MediaExtensions.WAV
        }
    }

    private fun processFile(file: File, resultList: MutableList<FileResult>) {
        processors.forEach {
            val status = it.process(file, fileQueue, resultList)
            if (status == FileStatus.PROCESSED) {
                return
            }
        }
        // file was not processed by any processor
        val rejected = FileResult(
                status = FileStatus.REJECTED,
                data = null,
                file = file
        )
        resultList.add(rejected)
    }

    companion object {
        fun build(): FileProcessingRouter {
            val processorList: List<FileProcessor> = listOf(
                    CueProcessor(),
                    JpgProcessor(),
                    Mp3Processor(),
                    TrProcessor(),
                    WavProcessor(),
                    OratureFileProcessor()
            )
            return FileProcessingRouter(processorList)
        }
    }
}