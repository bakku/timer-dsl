import external.Interpreter;
import external.Lexer;
import external.Parser;
import external.TimerDSLException;

public class External {
    public static void main(String[] args) throws TimerDSLException {
        var code =
            "timer\n" +
            "  print \"Hello World\"\n" +
            "  repeatedly\n" +
            "  every 30 seconds\n" +
            "  after 2 minutes\n" +
            "end\n" +
            "\n" +
            "timer\n" +
            "  print \"Hello World once!\"\n" +
            "  once\n" +
            "  after 10 seconds\n" +
            "end\n" +
            "\n" +
            "timer\n" +
            "  print \"Hello World now!\"\n" +
            "  once\n" +
            "  right now\n" +
            "end\n";

        var lexer = new Lexer(code);
        var parser = new Parser(lexer.getTokens());
        var interpreter = new Interpreter(parser.parse());

        interpreter.interpret();
    }
}
