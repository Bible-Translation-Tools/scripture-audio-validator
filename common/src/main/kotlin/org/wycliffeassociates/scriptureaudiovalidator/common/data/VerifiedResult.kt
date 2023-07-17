package org.wycliffeassociates.scriptureaudiovalidator.common.data

import java.io.File

class VerifiedResult (
    val status: FileStatus,
    val file: File,
    val message: String? = null
)