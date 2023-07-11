package org.bibletranslationtools.maui.common.fileprocessor

import org.bibletranslationtools.maui.common.data.FileResult
import org.bibletranslationtools.maui.common.data.FileStatus
import org.bibletranslationtools.maui.common.extensions.MediaExtensions
import org.bibletranslationtools.maui.common.validators.WavValidator
import org.wycliffeassociates.otter.common.audio.wav.InvalidWavFileException
import java.io.File
import java.lang.IllegalArgumentException
import java.util.Queue

class WavProcessor : FileProcessor() {
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

        if (ext != MediaExtensions.WAV) {
            return FileStatus.REJECTED
        }

        return try {
            WavValidator(file).validate()
            val fileData = getFileData(file)
            val result = FileResult(status = FileStatus.PROCESSED, data = fileData)
            resultList.add(result)

            FileStatus.PROCESSED
        } catch (ex: Exception) {
            FileStatus.REJECTED
        }
    }
}