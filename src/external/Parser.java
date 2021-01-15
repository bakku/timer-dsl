package external;

import external.ast.Command;
import external.ast.TimerConfiguration;
import external.ast.TimerStmt;
import static external.TokenType.*;
import static external.ast.Command.*;
import static external.ast.TimerConfiguration.*;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int current;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.current = 0;
    }

    // program → timer_stmt+
    public List<TimerStmt> parse() throws TimerDSLException {
        var timerStatements = new ArrayList<TimerStmt>();
        timerStatements.add(timerStmt());

        while (!isAtEnd()) {
            timerStatements.add(timerStmt());
        }

        return timerStatements;
    }

    // timer_stmt → "timer" command (once_timer | repeated_timer) "end"
    private TimerStmt timerStmt() throws TimerDSLException {
        consume(TIMER, "Expected 'timer' at the beginning of definition.");

        var command = command();

        TimerConfiguration config = null;

        if (match(ONCE)) config = onceTimer();
        else {
            consume(REPEATEDLY, "Expected 'once' or 'repeatedly' after command.");
            config = repeatedTimer();
        }

        consume(END, "Expected 'end' at the end of definition.");

        return new TimerStmt(command, config);
    }

    // command → "print" STRING
    private Command command() throws TimerDSLException {
        consume(PRINT, "Expected 'print' command.");
        var message = consume(STRING, "Expected 'string' after 'print'.");
        return new PrintCommand((String) message.getValue());
    }

    // once_timer → "once" after_configuration
    private OnceTimer onceTimer() throws TimerDSLException {
        current++;
        return new OnceTimer(afterConfig());
    }

    // repeated_timer → "repeatedly" "every" NUMBER time_unit after_configuration
    private RepeatedTimer repeatedTimer() throws TimerDSLException {
        consume(EVERY, "Expected 'every' after 'repeatedly'.");
        var number = consume(NUMBER, "Expected 'number' after 'every'.");
        return new RepeatedTimer(
            new TimeSetting(Long.valueOf((Integer) number.getValue()), timeUnit()),
            afterConfig()
        );
    }

    // after_configuration → "right" "now" | "after" NUMBER time_unit
    private TimeSetting afterConfig() throws TimerDSLException {
        if (match(RIGHT)) {
            current++;
            consume(NOW, "Expected 'now' after 'right'.");
            return new TimeSetting(0, TimeUnit.SECONDS);
        } else {
            consume(AFTER, "Expected 'right now' or 'after' as a time setting.");
            var number = consume(NUMBER, "Expected 'number' after 'after'.");
            return new TimeSetting(Long.valueOf((Integer) number.getValue()), timeUnit());
        }
    }

    // time_unit → "seconds" | "minutes" | "hours"
    private TimeUnit timeUnit() throws TimerDSLException {
        if (match(SECONDS)) {
            current++;
            return TimeUnit.SECONDS;
        } else if (match(MINUTES)) {
            current++;
            return TimeUnit.MINUTES;
        } else {
            consume(HOURS, "Expected 'minutes', 'seconds', or 'hours' as time unit.");
            return TimeUnit.HOURS;
        }
    }

    private Token consume(TokenType type, String message) throws TimerDSLException {
        if (match(type)) {
            current++;
            return tokens.get(current-1);
        }

        throw new TimerDSLException(message);
    }

    private boolean match(TokenType type) {
        return tokens.get(current).getType() == type;
    }

    private boolean isAtEnd() {
        return tokens.get(current).getType() == EOF;
    }
}
