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

    public int getHourlyRate(int current) {
        return (int) (current * 3600000D / elapsed());
    }

    public static String formatTime(long timeMilliseconds) {
        int seconds = ((int) ((timeMilliseconds / 1000) % 60));
        int mins = ((int) (((timeMilliseconds / 1000) / 60) % 60));
        int hours = ((int) ((((timeMilliseconds / 1000) / 60) / 60) % 60));
        return String.format("%02d:%02d:%02d", hours, mins, seconds);
    }

}
