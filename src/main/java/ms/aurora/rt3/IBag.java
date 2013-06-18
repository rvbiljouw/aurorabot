package ms.aurora.rt3;

/**
 * @author Rick
 */
public interface IBag {

    int getSentinelCount();

    INode getLastRetrived();

    INode getCurrent();

    INode[] getSentinels();

    int getCurrentSentinel();

}
