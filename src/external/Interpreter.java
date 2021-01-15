package external;

import external.ast.Command;
import external.ast.TimerConfiguration;
import external.ast.TimerStmt;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Interpreter {
    private List<TimerStmt> timerStatements;

    public Interpreter(List<TimerStmt> timerStatements) {
        this.timerStatements = timerStatements;
    }

    public void interpret() throws TimerDSLException {
        for(var stmt: timerStatements) {
            evaluate(stmt);
        }
    }

    private void evaluate(TimerStmt stmt) throws TimerDSLException {
        var timer = new Timer();
        var timerTask = buildTask(stmt.getCommand());

        if (stmt.getConfiguration() instanceof TimerConfiguration.OnceTimer) {
            var onceTimer = (TimerConfiguration.OnceTimer) stmt.getConfiguration();
            timer.schedule(
                timerTask,
                getMillis(
                    onceTimer.getAfterSetting().getNumber(),
                    onceTimer.getAfterSetting().getUnit()
                )
            );
        } else {
            var repeatedTimer = (TimerConfiguration.RepeatedTimer) stmt.getConfiguration();
            timer.schedule(
                timerTask,
                getMillis(
                    repeatedTimer.getAfterSetting().getNumber(),
                    repeatedTimer.getAfterSetting().getUnit()
                ),
                getMillis(
                    repeatedTimer.getEverySetting().getNumber(),
                    repeatedTimer.getEverySetting().getUnit()
                )
            );
        }
    }

    private TimerTask buildTask(Command command) throws TimerDSLException {
        if (command instanceof Command.PrintCommand) {
            var message = ((Command.PrintCommand) command).getMessage();
            return new TimerTask() {
                @Override
                public void run() {
                    System.out.println(message);
                }
            };
        } else throw new TimerDSLException("Unknown command type");
    }

    private long getMillis(long number, TimerConfiguration.TimeUnit unit) {
        if (unit == TimerConfiguration.TimeUnit.SECONDS) {
            return number * 1000;
        } else if (unit == TimerConfiguration.TimeUnit.MINUTES) {
            return number * 1000 * 60;
        } else {
            return number * 1000 * 60 * 60;
        }
    }
}
