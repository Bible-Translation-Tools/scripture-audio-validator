package org.wycliffeassociates.scriptureaudiovalidator.common.data

import java.io.File

class FileResult(
        val status: FileStatus,
        val data: FileData?,
        val requestedFile: File? = null
)