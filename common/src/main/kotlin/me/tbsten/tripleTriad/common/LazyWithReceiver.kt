@file:Suppress("ktlint:standard:filename")

package me.tbsten.tripleTriad.common

fun <Receiver, Value> lazyWithReceiver(init: Receiver.() -> Value): Lazy<Receiver, Value> = LazyImpl(init)

interface Lazy<Receiver, Value> {
    operator fun getValue(thisRef: Receiver, property: Any?): Value
}

private class LazyImpl<Receiver, Value>(private val init: Receiver.() -> Value) : Lazy<Receiver, Value> {
    private var initialized = false
    private var value: Value? = null

    override operator fun getValue(thisRef: Receiver, property: Any?): Value = if (initialized) {
        value ?: error("`lazyWithReceiver` not initialized.")
    } else {
        init(thisRef)
            .also { initializedValue ->
                initialized = true
                value = initializedValue
            }
    }
}
