package org.bibletranslationtools.scriptureaudiovalidator.common.io

import io.reactivex.Single

interface IResourceReader {
    fun read(): Single<List<String>>
}
