package com.cprieto.learning.tpdsl;

public class ListLexer {
    private char currentCharacter;
    private int currentPosition = 0;
    private String input;

    public ListLexer(String input) {
        this.input = input;
        currentCharacter = input.charAt(0);
    }

    public Token nextToken() {
        return null;
    }
}
