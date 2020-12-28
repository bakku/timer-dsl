package internal;

public final class Duration {
    private Duration() {}

    public static long seconds(long n) {
        return n * 1000;
    }

    public static long minutes(long n) {
        return seconds(60 * n);
    }

    public static long hours(long n) {
        return minutes(60 * n);
    }
}
