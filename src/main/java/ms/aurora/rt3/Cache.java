package ms.aurora.rt3;

/**
 * @author rvbiljouw
 */
public interface Cache {

    Queue getQueue();

    Bag getBag();

    CacheableNode getEmptyCacheableNode();

}
