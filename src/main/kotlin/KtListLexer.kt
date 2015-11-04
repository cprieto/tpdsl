package com.cprieto.learning.tpdsl.kt

enum class TokenName {
    LBRACK, NAME, COMMA, RBRACK, EOF
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
}

fun Char.choseTokenFromChar():Token? =
    when(this) {
        '[' -> Token(TokenName.LBRACK, "[")
        ',' -> Token(TokenName.COMMA, ",")
        ']' -> Token(TokenName.RBRACK, "]")
        else -> null
    }

class ListLexer(private val input: String):  Iterable<Token> {
    private val EOF = (-1).toChar()

    override fun iterator(): Iterator<Token> {
        if (input.isBlank())
            return object: Iterator<Token> {
                private var counter = 0
                override fun next(): Token {
                    if (counter++ >= 1)
                        throw StringIndexOutOfBoundsException()
                    return Token(TokenName.EOF, "<EOF>")
                }

                override fun hasNext() = counter == 0
            }

        return object: Iterator<Token> {
            private var currentPosition = 0
            private val currentCharacter: Char
                get() = input[currentPosition]

            override fun next(): Token {
                val token = currentCharacter.choseTokenFromChar() ?: tryNameOrEof(currentCharacter)

                return token
            }

            private fun tryNameOrEof(item: Char): Token {
                return if (item.isLetter())
                    Token(TokenName.NAME, item.toString())
                else Token(TokenName.EOF, "<EOF>")
            }


            override fun hasNext(): Boolean = currentPosition <= input.length
        }
    }
}