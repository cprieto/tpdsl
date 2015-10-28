package com.cprieto.learning.tpdsl;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class TokenTest {
    @Test
    public void itSetFieldsForToken() {
        Token token = new Token(ListToken.COMMA, ",");

        assertThat(token.type, is(ListToken.COMMA));
        assertThat(token.text, is(","));
    }

    @Test
    public void itHasToString() {
        Token token = new Token(ListToken.COMMA, ",");

        assertThat(token.toString(), is("<',', COMMA>"));
    }

    @Test
    public void EOFTokenIsThere() {
        Token token = new EOFToken();

        assertThat(token.type, is(ListToken.EOF));
        assertThat(token.text, is(ListToken.EOFText));
    }
}
