package com.cprieto.learning.tpdsl;

public class Token {
    public final ListToken type;
    public final String text;
    public Token(ListToken type, String text) {
        this.type = type;
        this.text = text;
    }

    @Override
    public String toString() {
        return "<'" + text + "', " + type.toString() + ">";
    }
}

