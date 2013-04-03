package ms.aurora.rt3;

/**
 * @author Rick
 */
public interface Bag {

    int getSentinelCount();

    Node getLastRetrived();

    Node getCurrent();

    Node[] getSentinels();

    int getCurrentSentinel();

}
