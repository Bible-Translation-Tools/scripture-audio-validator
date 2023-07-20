package org.bibletranslationtools.scriptureaudiovalidator.common.fileprocessor

import org.bibletranslationtools.scriptureaudiovalidator.common.data.FileData
import org.bibletranslationtools.scriptureaudiovalidator.common.data.FileResult
import org.bibletranslationtools.scriptureaudiovalidator.common.data.FileStatus
import org.bibletranslationtools.scriptureaudiovalidator.common.usecases.ParseFileName
import org.slf4j.LoggerFactory
import java.io.File
import java.util.Queue

abstract class FileProcessor {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    abstract fun process(
        file: File,
        fileQueue: Queue<File>,
        resultList: MutableList<FileResult>
    ): FileStatus

    protected fun getFileData(file: File): FileData {
        return try {
            ParseFileName(file).parse()
        } catch (e: Exception) {
            logger.error("Error while parsing file name.", e)
            throw e
        }
    }
}