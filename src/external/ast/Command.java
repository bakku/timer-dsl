package external.ast;

public abstract class Command {
    public static class PrintCommand extends Command {
        private final String message;

        public PrintCommand(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
