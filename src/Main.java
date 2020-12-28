import static internal.Duration.*;
import static internal.Tasks.*;
import static internal.TimerExpressionBuilder.timer;

public class Main {
    public static void main(String[] args) {
        timer()
            .execute(print("Hello, World repeatedly!"))
            .repeatedly()
            .every(seconds(10))
            .after(seconds(30))
            .setup();

        timer()
            .execute(print("Hello, World once!"))
            .once()
            .after(seconds(10))
            .setup();

        timer()
            .execute(print("Hello, World once now!"))
            .once()
            .rightNow()
            .setup();
    }
}
