package org.bibletranslationtools.scriptureaudiovalidator.common.io

typealias Versification = Map<String, List<Int>>

interface IVersificationReader {
    fun read(languageSlug: String): Versification
}