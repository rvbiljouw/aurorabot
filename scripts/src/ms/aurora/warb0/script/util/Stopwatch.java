package ms.aurora.warb0.script.util;

/**
 * @author tobiewarburton
 */
public class Stopwatch {
    private long start = -1;

    private Stopwatch() {
    }

    public void start() {
        if (start == -1) {
            start = System.currentTimeMillis();
        } else {
            throw new IllegalArgumentException("tried to start twice");
        }
    }

    public void stop() {
        start = -1;
    }

    /**
     * @return the runtime in milliseconds
     */
    public long getRuntime() {
        return System.currentTimeMillis() - start;
    }

    public int[] getStandardized() {
        long runtime = getRuntime();
        int seconds = ((int) ((runtime / 1000) % 60));
        int mins = ((int) (((runtime / 1000) / 60) % 60));
        int hours = ((int) ((((runtime / 1000) / 60) / 60) % 60));
        return new int[]{hours, mins, seconds};
    }

    public String getPrettyFormat() {
        int[] runtimes = getStandardized();
        return String.format("Runtime: %02d:%02d:%02d", runtimes[0], runtimes[1], runtimes[2]);
    }

    public int getHourlyRate(int current) {
        int[] runtimes = getStandardized();
        int seconds = runtimes[2];
        int mins = runtimes[1];
        int hours = runtimes[0];

        float xpSecond = 0;
        if ((mins > 0 || hours > 0 || seconds > 0) && current > 0) {
            xpSecond = ((float) current / (float) (seconds + (mins * 60) + (hours * 60 * 60)));
        }
        float xpMinute = xpSecond * 60;
        return (int) xpMinute * 60;
    }


    public static Stopwatch create() {
        return new Stopwatch();
    }
}
