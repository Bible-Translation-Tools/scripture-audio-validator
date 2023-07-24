package org.bibletranslationtools.scriptureaudiovalidator.common.data

import java.io.File

data class FileResult(
    val status: FileStatus,
    val data: FileData?,
    val file: File,
    val message: String? = null
)

/**
 * An API data entity describing the validation status of a given file.
 */
data class SerializableFileResult(
    val status: FileStatus,
    val fileName: String,
    val message: String? = null
) {
    constructor(result: FileResult) : this(result.status, result.file.name, result.message)
}