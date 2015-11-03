package com.cprieto.learning.tpdsl;

public class ListLexer extends Lexer {

    public ListLexer(String input) {
        super(input);
    }

    public Token nextToken() {
        while (currentCharacter != EOF) {
            switch (currentCharacter) {
                case ' ':
                case '\t':
                case '\n':
                case '\r':
                    whitespace();
                    continue;
                case '[':
                    consume();
                    return new Token(ListToken.LBRACK, "[");
                case ',':
                    consume();
                    return new Token(ListToken.COMMA, ",");
                case ']':
                    consume();
                    return new Token(ListToken.RBRACK, "]");
                default:
                    return name();
            }
        }
        return new EOFToken();
    }
}
