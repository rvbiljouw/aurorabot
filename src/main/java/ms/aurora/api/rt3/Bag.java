package ms.aurora.api.rt3;

/**
 * @author rvbiljouw
 */
public interface Bag {

    public int getSentinelCount();

    public Node getLastRetrived();

    public Node getCurrent();

    public Node[] getSentinels();

    public int getCurrentSentinel();

}
