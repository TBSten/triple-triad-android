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

fun <T> List<T>.updateIndexOf(index: Int, new: T) = mapIndexed { i, value ->
    if (i != index) {
        value
    } else {
        new
    }
}

fun <T> List<T>.removedItemOf(removeTarget: T) = filter {
    it != removeTarget
}

fun <T> List<T>.removedIndexOf(index: Int) = filterIndexed { i, _ ->
    i != index
}
