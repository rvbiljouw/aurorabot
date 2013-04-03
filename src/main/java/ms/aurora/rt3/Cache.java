package ms.aurora.rt3;

/**
 * @author Rick
 */
public interface Cache {

    Queue getQueue();

    Bag getBag();

    CacheableNode getEmptyCacheableNode();

}
