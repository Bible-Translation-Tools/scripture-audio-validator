package org.bibletranslationtools.scriptureaudiovalidator.common.data

import java.io.File

data class FileResult(
    val status: FileStatus,
    val data: FileData?,
    val file: File,
    val message: String? = null
) {
    val fileName = file.name
}