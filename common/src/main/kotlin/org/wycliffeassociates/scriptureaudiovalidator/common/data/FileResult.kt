package org.wycliffeassociates.scriptureaudiovalidator.common.data

import java.io.File

data class FileResult(
    val status: FileStatus,
    val data: FileData?,
    val file: File? = null,
    val message: String? = null
)