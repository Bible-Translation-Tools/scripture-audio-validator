package org.bibletranslationtools.scriptureaudiovalidator.common.usecases

import io.reactivex.Completable
import org.bibletranslationtools.scriptureaudiovalidator.common.client.IFileTransferClient

class TransferFile(private val client: IFileTransferClient) {

    fun transfer(): Completable {
        return client.transfer()
    }
}
