package external;

import java.util.*;

import static external.TokenType.*;

public class Lexer {
    private static final Map<String, TokenType> KEYWORDS = new HashMap<>();
    private int startOfToken = 0;
    private int endOfToken = 0;
    private final String code;
    private final List<Token> tokens = new ArrayList<>();

    static {
        KEYWORDS.putAll(Map.of(
            "timer", TIMER, "print", PRINT, "repeatedly", REPEATEDLY, "once", ONCE,
            "every", EVERY, "after", AFTER, "seconds", SECONDS, "minutes", MINUTES,
            "hours", HOURS, "right", RIGHT
        ));
        KEYWORDS.putAll(Map.of(
            "now", NOW, "end", END
        ));
    }

    public Lexer(String code) {
        this.code = code;
    }

    public List<Token> getTokens() throws TimerDSLException {
        while (!isAtEnd()) {
            readNextToken();
            this.startOfToken = this.endOfToken + 1;
            this.endOfToken = this.startOfToken;
        }

        tokens.add(new Token(EOF, null));
        return tokens;
    }

    private void readNextToken() throws TimerDSLException {
        var nextChar = code.charAt(this.startOfToken);

        if (List.of(' ', '\r', '\t', '\n').contains(nextChar)) {
            // do nothing
        } else if ('"' == nextChar) {
            string();
        } else if (isDigit(nextChar)) {
            number();
        } else if (isAlpha(nextChar)) {
            keyword();
        } else {
            throw new TimerDSLException("Unexpected character");
        }
    }

    private void string() throws TimerDSLException {
        endOfToken++;
        while (peek() != '"' && !isAtEnd()) endOfToken++;

        if (isAtEnd()) throw new TimerDSLException("Unterminated string");

        endOfToken++;
        var value = code.substring(startOfToken + 1, endOfToken - 1);
        tokens.add(new Token(STRING, value));
    }

    private void number() {
        while (isDigit(peek())) endOfToken++;
        tokens.add(
            new Token(NUMBER, Integer.parseInt(code.substring(startOfToken, endOfToken)))
        );
    }

    private void keyword() throws TimerDSLException {
        while (isAlpha(peek())) endOfToken++;
        var text = code.substring(startOfToken, endOfToken);

        if (KEYWORDS.containsKey(text))
            tokens.add(new Token(KEYWORDS.get(text), null));
        else
            throw new TimerDSLException("Unexpected keyword.");
    }

    private char peek() {
        return code.charAt(endOfToken);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAlpha(char c) {
        return c >= 'a' && c <= 'z';
    }

    private boolean isAtEnd() {
        return startOfToken >= code.length();
    }
}
