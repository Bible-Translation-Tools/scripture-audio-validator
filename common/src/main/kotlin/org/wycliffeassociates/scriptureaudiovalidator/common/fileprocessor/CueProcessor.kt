package org.wycliffeassociates.scriptureaudiovalidator.common.fileprocessor

import org.wycliffeassociates.scriptureaudiovalidator.common.data.FileResult
import org.wycliffeassociates.scriptureaudiovalidator.common.data.FileStatus
import org.wycliffeassociates.scriptureaudiovalidator.common.extensions.MediaExtensions
import org.wycliffeassociates.scriptureaudiovalidator.common.validators.CueValidator
import java.io.File
import java.lang.IllegalArgumentException
import java.util.Queue

class CueProcessor: FileProcessor() {
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

        if (ext != MediaExtensions.CUE) {
            return FileStatus.REJECTED
        }

        return try {
            CueValidator(file).validate()
            val fileData = getFileData(file)
            val result = FileResult(status = FileStatus.PROCESSED, data = fileData)
            resultList.add(result)

            FileStatus.PROCESSED
        } catch (ex: Exception) {
            FileStatus.REJECTED
        }
    }
}