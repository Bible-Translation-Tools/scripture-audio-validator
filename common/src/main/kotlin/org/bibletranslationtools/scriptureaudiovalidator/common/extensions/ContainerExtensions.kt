package org.bibletranslationtools.scriptureaudiovalidator.common.extensions

enum class ContainerExtensions(val ext: String) {
    TR("tr");

    companion object: SupportedExtensions {
        override fun isSupported(ext: String) =
            values().any {
                it.name == ext.uppercase() || it.ext == ext
            }
    }
}
