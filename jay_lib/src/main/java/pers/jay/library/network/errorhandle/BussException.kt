package pers.jay.library.network.errorhandle

class BussException(val code: Int = SERVER, override val message: String?) : Exception(message) {

    companion object {
        const val SERVER = 1
        const val APP = 2
    }
}