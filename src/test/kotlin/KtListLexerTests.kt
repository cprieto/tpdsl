package com.cprieto.learning.tpdsl.kt

import org.junit.Test as test
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull


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
    fun itDoesNotHaveMoreElementWhenAlreadyCalled() {
        val iterator = ListLexer("").iterator()

        iterator.next()
        assertFalse("Second time in empty string it should have not more element", iterator.hasNext())
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

    @test
    fun itCanExtractName() {
        val expected = Token(TokenName.NAME, "a")
        assertTokenIs(expected, "a")
    }

//    fun itCanParseExpression() {
//        val lexer = ListLexer("[a")
//        val iterator = lexer.iterator()
//
//        assertTrue(iterator.hasNext())
//
//        var token = iterator.next()
//        assertNotNull(token)
//
//        assertEquals(TokenName.LBRACK, token.type)
//
//        assertTrue(iterator.hasNext())
//    }

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