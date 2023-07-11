package org.bibletranslationtools.maui.common.fileprocessor

import org.bibletranslationtools.maui.common.data.FileResult
import org.bibletranslationtools.maui.common.data.FileStatus
import org.bibletranslationtools.maui.common.extensions.MediaExtensions
import org.bibletranslationtools.maui.common.validators.TrValidator
import java.io.File
import java.lang.IllegalArgumentException
import java.util.Queue

class TrProcessor: FileProcessor() {
    override fun process(
            file: File,
            fileQueue: Queue<File>,
            resultList: MutableList<FileResult>
    ): FileStatus {
        val ext = try {
            MediaExtensions.of(file.extension)
        } catch (ex: IllegalArgumentException) {
            null
        }

        if (ext != MediaExtensions.TR) {
            return FileStatus.REJECTED
        }

        return try {
            TrValidator(file).validate()
            val fileData = getFileData(file)
            val result = FileResult(status = FileStatus.PROCESSED, data = fileData)
            resultList.add(result)

            FileStatus.PROCESSED
        } catch (ex: Exception) {
            FileStatus.REJECTED
        }
    }
}