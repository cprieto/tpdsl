package com.cprieto.learning.tpdsl.kt

import com.tngtech.java.junit.dataprovider.DataProvider as dataProvider
import org.junit.Test as test
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertEquals
import com.tngtech.java.junit.dataprovider.DataProviderRunner
import com.tngtech.java.junit.dataprovider.UseDataProvider as useProvider
import org.junit.runner.RunWith as runWith



@runWith(DataProviderRunner::class)
class KtListLexerTests {

    @test
    fun itReturnsIterator() {
        val lexer = ListLexer("")

        assertNotNull(lexer.iterator())
    }

    @test
    fun EmptyStringIsEmptyIterator() {
        val iterator = ListLexer("")

        assertEquals(0, iterator.count())
    }

    @test
    fun BlankStringIsEmptyIterator(){
        val iterator = ListLexer("  ")

        assertEquals(0, iterator.count())
    }

    companion object {
        @dataProvider
        @JvmStatic fun tokenProvider() = arrayOf(
                arrayOf("]", TokenName.RBRACK),
                arrayOf("[", TokenName.LBRACK),
                arrayOf(",", TokenName.COMMA),
                arrayOf("a", TokenName.NAME),
                arrayOf("ab", TokenName.NAME),
                arrayOf("zebra", TokenName.NAME))
    }

    @test
    @useProvider("tokenProvider")
    fun itReturnsCorrectToken(text: String, expected: TokenName) {
        val lexer = ListLexer(text)

        assertEquals(1, lexer.count())
        val token = lexer.last()

        assertNotNull(token)
        assertEquals(text, token.text)
        assertEquals(expected, token.type)
    }

    @test
    fun itCanParseList()
    {
        val lexer = ListLexer("[a,b, casa]")

        assertEquals(7, lexer.count())

        assertEquals(TokenName.LBRACK, lexer.elementAt(0).type)
        assertEquals(TokenName.NAME, lexer.elementAt(1).type)
        assertEquals(TokenName.COMMA, lexer.elementAt(2).type)
        assertEquals(TokenName.NAME, lexer.elementAt(3).type)
        assertEquals(TokenName.COMMA, lexer.elementAt(4).type)
        assertEquals(TokenName.NAME, lexer.elementAt(5).type)
        assertEquals(TokenName.RBRACK, lexer.elementAt(6).type)

        assertEquals("a", lexer.elementAt(1).text)
        assertEquals("b", lexer.elementAt(3).text)
        assertEquals("casa", lexer.elementAt(5).text)
    }
}