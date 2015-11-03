package com.cprieto.learning.tpdsl.kt

import org.junit.Test as test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.runner.RunWith


class KtListLexerTests {
    @test
    fun itReturnsIterator() {
        val lexer = ListLexer("")

        assertNotNull(lexer.iterator())
    }

    @test
    fun itHasNextIfFirst() {
        val iterator = ListLexer("").iterator()

        assertTrue(iterator.hasNext())
    }

    @test
    fun itHasTokenIfEmpty() {
        val iterator = ListLexer("").iterator()

        assertNotNull(iterator.next())
    }

    @test
    fun itHasEofTokenWhenOnlyBlanks(){
        val iterator = ListLexer("  ").iterator()
        val token = iterator.next()

        assertThat(token.type, `is`(TokenName.EOF))
    }

    @test
    fun itReturnsEofTokenWhenEmpty() {
        val iterator = ListLexer("").iterator()
        val token = iterator.next()

        assertThat(token.type, `is`(TokenName.EOF))
    }

    @test
    fun itReturnsLeftBracketWhenPresent() {
        val expected = Token(TokenName.LBRACK, "[")
        assertTokenIs(expected, "[")
    }

    @test
    fun itReturnsCommaWhenPresent() {
        val expected = Token(TokenName.COMMA, ",")
        assertTokenIs(expected, ",")
    }

    @test
    fun itReturnsRightBrackedWhenPresent() {
        val expected = Token(TokenName.RBRACK, "]")
        assertTokenIs(expected, "]")
    }

    private fun assertTokenIs(expected: Token, input: String) {
        val lexer = ListLexer(input)
        val iterator = lexer.iterator()
        val token = iterator.next()

        assertThat(iterator.hasNext(), `is`(true))
        assertNotNull(token)

        assertThat(token.type, `is`(expected.type))
        assertThat(token.text, `is`(expected.text))
    }
}