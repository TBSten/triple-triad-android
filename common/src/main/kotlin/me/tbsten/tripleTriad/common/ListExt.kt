package me.tbsten.tripleTriad.common

fun <T> List<T>.update(prev: T, new: T) = map {
    if (it != prev) {
        it
    } else {
        new
    }
}

fun <T> List<T>.update(prev: T, new: (T) -> T) = map {
    if (it != prev) {
        it
    } else {
        new(it)
    }
}

fun <T> List<T>.updateIndexed(index: Int, new: (T) -> T) = mapIndexed { i, element ->
    if (i != index) {
        element
    } else {
        new(element)
    }
}
