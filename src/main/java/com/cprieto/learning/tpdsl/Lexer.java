package com.cprieto.learning.tpdsl;

public abstract class Lexer {
    protected char currentCharacter;
    protected int currentPosition = 0;
    protected String input;

    protected Lexer(String input) {
        this.input = input;
        currentCharacter = input.charAt(currentPosition);
    }

    public static final char EOF = (char) -1;

    protected void consume() {
        currentPosition++;
        if (currentPosition >= input.length())
            currentCharacter = EOF;
        else
            currentCharacter = input.charAt(currentPosition);
    }

    protected void whitespace() {
        while (isSpace(currentCharacter)) {
            consume();
        }
    }

    protected static boolean isLetter(char element) {
        return element >= 'a' && element <= 'z' || element >= 'A' && element <= 'Z';
    }

    protected static boolean isSpace(char element) {
        return element == ' ' || element == '\t' || element == '\n' || element == '\r';
    }

    protected Token name() {
        if (!isLetter(currentCharacter))
            throw new Error("Unrecognized character: " + currentCharacter);
        StringBuilder builder = new StringBuilder();
        do {
            builder.append(currentCharacter);
            consume();
        } while (isLetter(currentCharacter));
        return new Token(ListToken.NAME, builder.toString());
    }
}
