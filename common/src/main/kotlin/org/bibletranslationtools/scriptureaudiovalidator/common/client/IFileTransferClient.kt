package org.bibletranslationtools.scriptureaudiovalidator.common.client

import io.reactivex.Completable

interface IFileTransferClient {
    fun transfer(): Completable
}
