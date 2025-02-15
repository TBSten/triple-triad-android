package me.tbsten.tripleTriad.common

import kotlin.test.assertEquals
import org.junit.Test

class ListExtTest {
    @Test
    fun updateTest() {
        assertEquals(
            listOf("A", "B", "C", "D").update("C", "XXX"),
            listOf("A", "B", "XXX", "D"),
        )
    }

    @Test
    fun updateIndexOfTest() {
        assertEquals(
            listOf("A", "B", "C", "D").updateIndexed(3) { "XXX" },
            listOf("A", "B", "C", "XXX"),
        )
    }
}
