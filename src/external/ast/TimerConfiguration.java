package external.ast;

public abstract class TimerConfiguration {
    public enum TimeUnit {
        SECONDS, MINUTES, HOURS
    }

    public static class OnceTimer extends TimerConfiguration {
        private final TimeSetting afterSetting;

        public OnceTimer(TimeSetting afterSetting) {
            this.afterSetting = afterSetting;
        }

        public TimeSetting getAfterSetting() {
            return afterSetting;
        }
    }

    public static class RepeatedTimer extends TimerConfiguration {
        private final TimeSetting everySetting;
        private final TimeSetting afterSetting;

        public RepeatedTimer(TimeSetting everySetting, TimeSetting afterSetting) {
            this.everySetting = everySetting;
            this.afterSetting = afterSetting;
        }

        public TimeSetting getEverySetting() {
            return everySetting;
        }

        public TimeSetting getAfterSetting() {
            return afterSetting;
        }
    }

    public static class TimeSetting {
        private final long number;
        private final TimeUnit unit;

        public TimeSetting(long number, TimeUnit unit) {
            this.number = number;
            this.unit = unit;
        }

        public long getNumber() {
            return number;
        }

        public TimeUnit getUnit() {
            return unit;
        }
    }
}
