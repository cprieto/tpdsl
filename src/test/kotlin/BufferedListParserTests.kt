package com.cprieto.learning.tpdsl.kt

import com.tngtech.java.junit.dataprovider.DataProviderRunner
import org.junit.runner.RunWith as runWith
import com.tngtech.java.junit.dataprovider.UseDataProvider as useDataProvider
import com.tngtech.java.junit.dataprovider.DataProvider as dataProvider
import org.junit.Test as test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.text.ParseException

@runWith(DataProviderRunner::class)
class BufferedListParserTests {
    @test(expected = IllegalArgumentException::class)
    fun itShouldFailWhenEmptyEntry() {
        val lexer = mock(Lexer::class.java)
        `when`(lexer.isBlank()).thenReturn(true)
        BufferedListParser(lexer, 3)
    }

    @test(expected = ParseException::class)
    fun itShouldFailWhenInvalidInput() {
        val lexer = ListLexer("[")
        val parser = BufferedListParser(lexer, 3)

        parser.list()
    }

    @test(expected = ParseException::class)
    fun itShouldFailWhenEmptyList() {
        val lexer = ListLexer("[]")
        val parser = BufferedListParser(lexer, 3)

        parser.list()
    }

    @test(expected = ParseException::class)
    fun itShouldFailWithJustComma() {
        val lexer = ListLexer("[,]")
        val parser = BufferedListParser(lexer, 3)

        parser.list()
    }

    @test
    fun itShouldPassWithMultipleElements() {
        val lexer = ListLexer("[a,b]")
        val parser = BufferedListParser(lexer, 3)

        parser.list()
    }

    @test
    fun itShouldPassWithSimpleItem() {
        val lexer = ListLexer("[first]")
        val parser = BufferedListParser(lexer, 3)

        parser.list()
    }

    @test
    fun itShouldSupportNestedList() {
        val lexer = ListLexer("[a,b,[c,d],e]")
        val parser = BufferedListParser(lexer, 3)

        parser.list()
    }

    @test
    fun itShouldSupportAssignations() {
        val lexer = ListLexer("[a,b,[c,d],e=f]")
        val parser = BufferedListParser(lexer, 3)

        parser.list()
    }
}