package me.tbsten.tripleTriad.common

import timber.log.Timber

fun initLogger() {
    if (BuildConfig.DEBUG) {
        Timber.plant(Timber.DebugTree())
    }
}

@Suppress("unused")
fun log(message: String?, vararg args: Any?) {
    Timber.d(message, args)
}

@Suppress("unused")
fun logWarn(message: String?, vararg args: Any?) {
    Timber.e(message, args)
}

@Suppress("unused")
fun logError(message: String?, vararg args: Any?) {
    Timber.e(message, args)
}

fun logError(throwable: Throwable, message: String?, vararg args: Any?) {
    Timber.e(throwable, message, args)
}

@Suppress("unused")
fun logError(throwable: Throwable) {
    Timber.e(throwable)
}
