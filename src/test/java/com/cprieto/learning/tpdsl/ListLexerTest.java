package com.cprieto.learning.tpdsl;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ListLexerTest {
    private Token[] expected = {
            new Token(ListToken.LBRACK, "["),
            new Token(ListToken.NAME, "a"),
            new Token(ListToken.COMMA, ","),
            new Token(ListToken.NAME, "b"),
            new Token(ListToken.RBRACK, "]"),
            new EOFToken()
    };

    @Test
    public void itCanParseList() {
        ListLexer lexer = new ListLexer("[a,b]");

        assertExpectedTokens(lexer);
    }

    @Test
    public void itIgnoresSpaces() {
        ListLexer lexer = new ListLexer("[a ,   b]");

        assertExpectedTokens(lexer);
    }

    private void assertExpectedTokens(ListLexer lexer) {
        Token token = lexer.nextToken();
        int n = 0;

        while (token.type != ListToken.EOF) {
            assertThat(token.type, is(expected[n].type));
            assertThat(token.text, is(expected[n].text));
            n += 1;
            token = lexer.nextToken();
        }
    }
}
