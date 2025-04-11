package ch.qos.logback.core.subst;

import ch.qos.logback.core.spi.ScanException;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    final String pattern;
    final int patternLength;
    TokenizerState state;
    int pointer;

    public Tokenizer(String pattern) {
        this.state = TokenizerState.LITERAL_STATE;
        this.pointer = 0;
        this.pattern = pattern;
        this.patternLength = pattern.length();
    }

    List<Token> tokenize() throws ScanException {
        List<Token> tokenList = new ArrayList<Token>();
        StringBuilder buf = new StringBuilder();

        while (this.pointer < this.patternLength) {
            char c = this.pattern.charAt(this.pointer);
            this.pointer++;

            switch (this.state) {
                case LITERAL_STATE:
                    handleLiteralState(c, tokenList, buf);

                case START_STATE:
                    handleStartState(c, tokenList, buf);

                case DEFAULT_VAL_STATE:
                    handleDefaultValueState(c, tokenList, buf);
            }

        }
        switch (this.state) {
            case LITERAL_STATE:
                addLiteralToken(tokenList, buf);
                break;
            case START_STATE:
                throw new ScanException("Unexpected end of pattern string");
        }
        return tokenList;
    }

    private void handleDefaultValueState(char c, List<Token> tokenList, StringBuilder stringBuilder) {
        switch (c) {
            case '-':
                tokenList.add(Token.DEFAULT_SEP_TOKEN);
                this.state = TokenizerState.LITERAL_STATE;
                return;
            case '$':
                stringBuilder.append(':');
                addLiteralToken(tokenList, stringBuilder);
                stringBuilder.setLength(0);
                this.state = TokenizerState.START_STATE;
                return;
        }
        stringBuilder.append(':').append(c);
        this.state = TokenizerState.LITERAL_STATE;
    }

    private void handleStartState(char c, List<Token> tokenList, StringBuilder stringBuilder) {
        if (c == '{') {
            tokenList.add(Token.START_TOKEN);
        } else {
            stringBuilder.append('$').append(c);
        }
        this.state = TokenizerState.LITERAL_STATE;
    }

    private void handleLiteralState(char c, List<Token> tokenList, StringBuilder stringBuilder) {
        if (c == '$') {
            addLiteralToken(tokenList, stringBuilder);
            stringBuilder.setLength(0);
            this.state = TokenizerState.START_STATE;
        } else if (c == ':') {
            addLiteralToken(tokenList, stringBuilder);
            stringBuilder.setLength(0);
            this.state = TokenizerState.DEFAULT_VAL_STATE;
        } else if (c == '{') {
            addLiteralToken(tokenList, stringBuilder);
            tokenList.add(Token.CURLY_LEFT_TOKEN);
            stringBuilder.setLength(0);
        } else if (c == '}') {
            addLiteralToken(tokenList, stringBuilder);
            tokenList.add(Token.CURLY_RIGHT_TOKEN);
            stringBuilder.setLength(0);
        } else {
            stringBuilder.append(c);
        }
    }

    private void addLiteralToken(List<Token> tokenList, StringBuilder stringBuilder) {
        if (stringBuilder.length() == 0)
            return;
        tokenList.add(new Token(Token.Type.LITERAL, stringBuilder.toString()));
    }

    enum TokenizerState {
        LITERAL_STATE, START_STATE, DEFAULT_VAL_STATE;
    }
}

