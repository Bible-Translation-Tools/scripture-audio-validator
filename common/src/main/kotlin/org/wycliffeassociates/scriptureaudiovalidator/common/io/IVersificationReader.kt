package org.wycliffeassociates.scriptureaudiovalidator.common.io

typealias Versification = Map<String, List<Int>>

interface IVersificationReader {
    fun read(): Versification
}