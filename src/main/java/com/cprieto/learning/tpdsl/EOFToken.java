package com.cprieto.learning.tpdsl;

public final class EOFToken extends Token {
    public EOFToken() {
        super(ListToken.EOF, ListToken.EOFText);
    }
}
