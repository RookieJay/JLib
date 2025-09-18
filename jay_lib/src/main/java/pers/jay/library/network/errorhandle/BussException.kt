package pers.jay.library.network.errorhandle

class BussException(val code: Int = 0, override val message: String?) : Exception(message) {
}