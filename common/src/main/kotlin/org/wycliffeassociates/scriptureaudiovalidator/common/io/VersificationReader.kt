package org.wycliffeassociates.scriptureaudiovalidator.common.io

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.wycliffeassociates.scriptureaudiovalidator.common.data.VersificationScheme

class VersificationReader : IVersificationReader {

    private val schema = mutableMapOf<VersificationScheme, Versification>()

    init {
        schema[VersificationScheme.ENG] = parseVersification("en")
        schema[VersificationScheme.RSC] = parseVersification("ru")
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private class VersificationSchema(
        val maxVerses: Map<String, List<Int>>
    )

    override fun read(languageSlug: String): Versification {
        return when(languageSlug) {
            "ru" -> schema[VersificationScheme.RSC]
            else -> schema[VersificationScheme.ENG]
        }!!
    }

    private fun parseVersification(languageSlug: String): Versification {
        val versificationScheme = when(languageSlug) {
            "ru" -> "/rsc.json"
            else ->"/eng.json"
        }
        javaClass.getResource(versificationScheme).openStream().use { inputStream ->
            val versification: VersificationSchema = jacksonObjectMapper().readValue(inputStream)
            return versification.maxVerses as Versification
        }
    }
}