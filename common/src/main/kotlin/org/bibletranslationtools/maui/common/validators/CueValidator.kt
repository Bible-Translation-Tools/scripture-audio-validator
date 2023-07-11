package org.bibletranslationtools.maui.common.validators

import org.apache.tika.Tika
import java.io.File

class CueValidator(private val file: File) : IValidator {

    companion object {
        private const val PLAIN_TEXT_MIME_TYPE = "text/plain"
    }

    override fun validate() {
        val fileType = Tika().detect(file)

        if (fileType != PLAIN_TEXT_MIME_TYPE) {
            throw IllegalArgumentException("Not a plain text file")
        }
    }
}
