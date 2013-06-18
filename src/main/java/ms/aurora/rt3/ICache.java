package ms.aurora.rt3;

/**
 * @author Rick
 */
public interface ICache {

    IQueue getQueue();

    IBag getBag();

    ICacheableNode getEmptyCacheableNode();

}
