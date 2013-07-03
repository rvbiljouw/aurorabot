package ms.aurora.api.util;

import ms.aurora.api.random.Random;

/**
 * @author iJava
 */
public class BreakHandler {

    private Break[] breaks;

    public BreakHandler(Break... breaks) {
        this.breaks = breaks;
    }

    public Break[] getBreaks() {
        return breaks;
    }

    public Break findBreak(long time) {
        for(Break brea : breaks) {
            if(brea.trigger(time)) {
                return brea;
            }
        }
        return null;
    }

    public class Break {

        private long triggerTime, breakTime;
        private boolean triggered = false;

        private Break(long minTriggerTime, long maxTriggerTime, long minBreakTime, long maxBreakTime) {
            triggerTime = Utilities.random((minTriggerTime * 60000), (maxTriggerTime * 60000));
            breakTime = Utilities.random((minBreakTime * 60000), (maxBreakTime * 60000));
        }

        public boolean trigger(long time) {
            if(triggered) {
                return false;
            }
            if(time >= triggerTime) {
                triggered = true;
                return true;
            }
            return false;
        }

        public long getBreakTime() {
            return breakTime;
        }
    }
}
