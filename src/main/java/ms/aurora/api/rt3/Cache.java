package ms.aurora.api.rt3;

/**
 * @author rvbiljouw
 */
public interface Cache {

    public Queue getQueue();

    public Bag getBag();

    public CacheableNode getEmptyCacheableNode();

}
