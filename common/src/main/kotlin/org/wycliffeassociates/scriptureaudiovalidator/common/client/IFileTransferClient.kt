package org.wycliffeassociates.scriptureaudiovalidator.common.client

import io.reactivex.Completable

interface IFileTransferClient {
    fun transfer(): Completable
}
