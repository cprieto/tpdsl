package com.cprieto.learning.tpdsl.kt

import java.util.NoSuchElementException

enum class TokenName {
    LBRACK, NAME, COMMA, RBRACK
}

data class Token(val type:TokenName, val text: String) {
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

    operator fun get(index: Int):Char = cleaned[index]
    val size = cleaned.length
}

class ListLexer(private val input: String):  Iterable<Token> {
    override fun iterator(): Iterator<Token> {
        if (input.isBlank())
            return object: Iterator<Token> {
                override fun next(): Token = throw StringIndexOutOfBoundsException()

                override fun hasNext() = false
            }

        return object: Iterator<Token> {
            private val entry = StringTokenIterator(input)
            private val total = entry.count()
            private var currentPosition = 0

            override fun next(): Token {
                if (currentPosition == total)
                    throw NoSuchElementException()

                val currentChar = entry[currentPosition++]
                when (currentChar) {
                    ']' -> return Token(TokenName.RBRACK, "]")
                    '[' -> return Token(TokenName.LBRACK, "[")
                    ',' -> return Token(TokenName.COMMA, ",")
                    else -> return getNameToken(currentChar)
                }
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

            override fun hasNext() = currentPosition < total
        }
    }
}