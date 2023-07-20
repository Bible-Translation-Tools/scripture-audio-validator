package org.bibletranslationtools.scriptureaudiovalidator.common.io

import io.reactivex.Single

typealias Versification = Map<String, List<Int>>

interface IVersificationReader {
    fun read(): Single<Versification>
}