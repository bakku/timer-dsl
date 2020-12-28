package internal;

import java.util.Timer;
import java.util.TimerTask;

public final class TimerExpressionBuilder {
    private TimerExpressionBuilder() {
    }

    public static TimerExpressionBuilder timer() {
        return new TimerExpressionBuilder();
    }

    public TimerExpressionBuilderWithTask execute(TimerTask task) {
        return new TimerExpressionBuilderWithTask(task);
    }

    public static class TimerExpressionBuilderWithTask {
        private final TimerTask task;

        public TimerExpressionBuilderWithTask(TimerTask task) {
            this.task = task;
        }

        public RepeatableTimerExpressionBuilder repeatedly() {
            return new RepeatableTimerExpressionBuilder(this.task);
        }

        public SingleTimerExpressionBuilder once() {
            return new SingleTimerExpressionBuilder(this.task);
        }
    }

    public static class RepeatableTimerExpressionBuilder {
        private final TimerTask task;

        public RepeatableTimerExpressionBuilder(TimerTask task) {
            this.task = task;
        }

        public PeriodicRepeatableTimerExpressionBuilder every(long period) {
            return new PeriodicRepeatableTimerExpressionBuilder(this.task, period);
        }
    }

    public static class SingleTimerExpressionBuilder {
        private final TimerTask task;

        public SingleTimerExpressionBuilder(TimerTask task) {
            this.task = task;
        }

        public FinalizedSingleTimerExpressionBuilder rightNow() {
            return after(0);
        }

        public FinalizedSingleTimerExpressionBuilder after(long delay) {
            return new FinalizedSingleTimerExpressionBuilder(this.task, delay);
        }
    }

    public static class FinalizedSingleTimerExpressionBuilder {
        private final TimerTask task;
        private final long delay;

        public FinalizedSingleTimerExpressionBuilder(TimerTask task, long delay) {
            this.task = task;
            this.delay = delay;
        }

        public void setup() {
            var timer = new Timer();
            timer.schedule(this.task, delay);
        }
    }

    public static class PeriodicRepeatableTimerExpressionBuilder {
        private final TimerTask task;
        private final long period;

        public PeriodicRepeatableTimerExpressionBuilder(TimerTask task, long period) {
            this.task = task;
            this.period = period;
        }

        public FinalizedRepeatableTimerExpressionBuilder rightNow() {
            return after(0);
        }

        public FinalizedRepeatableTimerExpressionBuilder after(long delay) {
            return new FinalizedRepeatableTimerExpressionBuilder(this.task, this.period, delay);
        }
    }

    public static class FinalizedRepeatableTimerExpressionBuilder {
        private final TimerTask task;
        private final long period;
        private final long delay;

        public FinalizedRepeatableTimerExpressionBuilder(TimerTask task, long period, long delay) {
            this.task = task;
            this.period = period;
            this.delay = delay;
        }

        public void setup() {
            var timer = new Timer();
            timer.schedule(this.task, this.delay, this.period);
        }
    }
}
