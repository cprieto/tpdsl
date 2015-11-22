package com.cprieto.learning.tpdsl.kt

import java.text.ParseException
import java.util.NoSuchElementException
import kotlin.jvm.internal.iterator

enum class TokenName {
    LBRACK, NAME, COMMA, RBRACK, EQUALS
}

data class Token(val type: TokenName, val text: String) {
    override fun toString() = "<'$text', $type>"
}

class StringTokenIterator(private val input: String) : Iterable<Char> {
    private val cleaned = input.filter { !it.isWhitespace() }

    override fun iterator(): Iterator<Char> {
        return object : Iterator<Char> {
            private var currentPosition: Int = 0
            override fun next(): Char {
                if (currentPosition >= cleaned.length)
                    throw StringIndexOutOfBoundsException()

                return cleaned[currentPosition++]
            }

            override fun hasNext() = currentPosition < cleaned.length
        }
    }

    operator fun get(index: Int): Char = cleaned[index]
    val size = cleaned.length
}

abstract class Lexer(protected val input: String) : Iterable<Token> {
    fun isBlank() = input.isBlank()

    abstract fun getTokenIterator(): Iterator<Token>

    override fun iterator(): Iterator<Token> {
        if (input.isBlank())
            return object : Iterator<Token> {
                override fun next(): Token = throw StringIndexOutOfBoundsException()

                override fun hasNext() = false
            }
        return getTokenIterator()
    }
}

class ListLexer(input: String) : Lexer(input) {
    override fun getTokenIterator() = object : Iterator<Token> {
        private val entry = StringTokenIterator(input)
        private val total = entry.count()
        private var currentPosition = 0

        override fun next(): Token {
            if (currentPosition == total)
                throw NoSuchElementException()

            return choseToken(entry[currentPosition++])
        }

        private fun getNameToken(current: Char): Token {
            fun shouldAppend() =
                    currentPosition < total && entry[currentPosition].isLetter()

            if (!current.isLetter())
                throw Error("Unrecognized character $current")

            val buffer = StringBuilder()
            buffer.append(current)

            while (shouldAppend()) {
                buffer.append(entry[currentPosition++])
            }

            return Token(TokenName.NAME, buffer.toString())
        }

        private fun choseToken(current: Char) = when (current) {
            ']' -> Token(TokenName.RBRACK, "]")
            '[' -> Token(TokenName.LBRACK, "[")
            ',' -> Token(TokenName.COMMA, ",")
            '=' -> Token(TokenName.EQUALS, "=")
            else -> getNameToken(current)
        }

        override fun hasNext() = currentPosition < total
    }
}

abstract class Parser(protected val lexer: Lexer) {
    private val iterator = lexer.iterator()
    protected var lookahead: Token? = null
    private var endOfFile = false

    init {
        if (lexer.isBlank())
            throw IllegalArgumentException("Empty entries cannot be parsed")
        consume()
    }

    private fun consume() {
        if (!iterator.hasNext())
            endOfFile = true
        else
            lookahead = iterator.next()
    }

    protected fun match(token: TokenName) {
        if (endOfFile)
            throw ParseException("Expected $token instead got End of File", 0)

        if (lookahead!!.type != token)
            throw ParseException("Expected $token instead got ${lookahead!!.type}", 0)

        consume()
    }
}

/*
list:     '[' elements ']';
elements: element (',' element)*;
element: NAME | list;
NAME: ('a'-'z'|'Z'-'Z')+;
 */
class SimpleListParser(lexer: Lexer) : Parser(lexer) {
    fun list() {
        match(TokenName.LBRACK)
        elements()
        match(TokenName.RBRACK)
    }

    private fun elements() {
        element()
        while (lookahead?.type == TokenName.COMMA) {
            match(TokenName.COMMA)
            element()
        }
    }

    private fun element() {
        if (lookahead?.type == TokenName.NAME)
            match(TokenName.NAME)
        else if (lookahead?.type == TokenName.LBRACK)
            list()
        else
            throw ParseException("Unrecognized character or token", 0)
    }
}

abstract class BufferedParser(protected val lexer: Lexer, private val bufferSize: Int) {
    private val iterator = lexer.iterator()
    protected var buffer = arrayOfNulls<Token>(bufferSize)
    private var position = 0

    init {
        if (lexer.isBlank())
            throw IllegalArgumentException("Empty entries cannot be parsed")
        for (i in 1..bufferSize)
            consume()
    }

    private fun consume() {
        buffer[position] = if (iterator.hasNext()) iterator.next() else null
        position = (position + 1) % bufferSize
    }

    fun tokenAt(idx: Int):Token? = buffer[(position + idx -1) % bufferSize]
    fun tokenAhead(idx: Int): Token? = tokenAt(idx)

    protected fun match(token: TokenName) {

        val lookahead = tokenAhead(1)?.type
                ?: throw ParseException("Expected $token instead got End of File", 0)

        if (lookahead != token)
            throw ParseException("Expected $token instead got ${tokenAt(1)}", 0)
        consume()
    }
}

class BufferedListParser(lexer: Lexer, bufferSize: Int): BufferedParser(lexer, bufferSize) {
    fun list() {
        match(TokenName.LBRACK)
        elements()
        match(TokenName.RBRACK)
    }

    private fun elements() {
        element()
        while (tokenAhead(1)?.type == TokenName.COMMA) {
            match(TokenName.COMMA)
            element()
        }
    }

    private fun element() {
        val isAssignation = tokenAhead(2)?.type == TokenName.EQUALS

        if (isAssignation()) {
            match(TokenName.NAME)
            match(TokenName.EQUALS)
            match(TokenName.NAME)
        } else if (tokenAhead(1)?.type == TokenName.NAME)
            match(TokenName.NAME)
        else if (tokenAhead(1)?.type == TokenName.LBRACK)
            list()
        else
            throw ParseException("Expected name or list, got ${tokenAt(1)?.type}", 0)
    }

    private fun isAssignation() =
            tokenAhead(1)?.type == TokenName.NAME
            && tokenAhead(2)?.type == TokenName.EQUALS
}
