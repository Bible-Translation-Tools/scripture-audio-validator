package org.wycliffeassociates.scriptureaudiovalidator.common.usecases

import io.reactivex.Completable
import org.bibletranslationtools.maui.common.client.IFileTransferClient

class TransferFile(private val client: IFileTransferClient) {

    fun transfer(): Completable {
        return client.transfer()
    }
}
