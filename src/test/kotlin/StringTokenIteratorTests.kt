package com.cprieto.learning.tpdsl.kt

import org.junit.Test as test
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat

class StringTokenIteratorTests {
    @test
    fun itHasChar() {
        val iterator = StringTokenIterator("abc")
        assertThat(iterator.count(), `is`(3))
    }

    @test
    fun itGetsCorrectElements() {
        val iterator = StringTokenIterator("abc")

        assertThat(iterator.elementAt(0), `is`('a'))
        assertThat(iterator.elementAt(1), `is`('b'))
        assertThat(iterator.elementAt(2), `is`('c'))
    }

    @test
    fun itIgnoresSpacesAtSides() {
        val iterator = StringTokenIterator("  abc  ")
        assertThat(iterator.count(), `is`(3))
    }

    @test
    fun itIgnoresAllSpaces() {
        val iterator = StringTokenIterator("a b c")
        assertThat(iterator.count(), `is`(3))
    }
}