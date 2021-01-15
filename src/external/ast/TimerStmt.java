package external.ast;

public class TimerStmt {
    private final Command command;
    private final TimerConfiguration configuration;

    public TimerStmt(Command command, TimerConfiguration configuration) {
        this.command = command;
        this.configuration = configuration;
    }

    public Command getCommand() {
        return command;
    }

    public TimerConfiguration getConfiguration() {
        return configuration;
    }
}
