package internal;

import java.util.TimerTask;

public final class Tasks {
    private Tasks() {}

    public static TimerTask print(String message) {
        return new TimerTask() {
            @Override
            public void run() {
                System.out.println(message);
            }
        };
    }
}
