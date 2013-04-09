package ms.aurora.api.util;

/**
 * Created with IntelliJ IDEA.
 * User: Cov
 * Date: 30/03/13
 * Time: 11:32
 * To change this template use File | Settings | File Templates.
 */
public class Timer {

    private long startTime = 0;
    private long endTime = 0;

    public Timer(long runTime) {
        if (runTime < 0) {
            throw new IllegalArgumentException("Can't have a Timer with a negative time!");
        }
        startTime = System.currentTimeMillis();
        endTime = startTime + runTime;
    }

    public Timer() {
        startTime = System.currentTimeMillis();
        endTime = startTime;
    }

    public void setRunTime(long runTime) {
        endTime = System.currentTimeMillis() + runTime;
    }

    public boolean finished() {
        return endTime <= System.currentTimeMillis();
    }

    public long remaining() {
        return endTime - System.currentTimeMillis();
    }

    public long elapsed() {
        return System.currentTimeMillis() - startTime;
    }

    public static String formatTime(long timeMilliseconds) {
        StringBuilder b = new StringBuilder();
        long runtime = timeMilliseconds;
        long totalSecs = runtime / 1000;
        long totalMins = totalSecs / 60;
        long totalHours = totalMins / 60;
        int seconds = (int) totalSecs % 60;
        int minutes = (int) totalMins % 60;
        int hours = (int) totalHours % 60;
        if (hours < 10) {
            b.append("0");
        }
        b.append(hours);
        b.append(" : ");
        if (minutes < 10) {
            b.append("0");
        }
        b.append(minutes);
        b.append(" : ");
        if (seconds < 10) {
            b.append("0");
        }
        b.append(seconds);
        return b.toString();
    }

}
