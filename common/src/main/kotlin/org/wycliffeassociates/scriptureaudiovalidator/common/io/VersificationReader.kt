package org.wycliffeassociates.scriptureaudiovalidator.common.io

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.reactivex.Single

class VersificationReader : IVersificationReader {
    @JsonIgnoreProperties(ignoreUnknown = true)
    private class VersificationSchema(
        val maxVerses: Map<String, List<Int>>
    )

    override fun read(): Versification {
        return parseVersification()
    }

    private fun parseVersification(): Versification {
        val versificationFile = javaClass.getResource("/eng.json").openStream()

        versificationFile.use { inputStream ->
            val versification: VersificationSchema = jacksonObjectMapper().readValue(inputStream)
            return versification.maxVerses as Versification
        }
    }
}