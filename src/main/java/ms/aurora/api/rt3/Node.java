package ms.aurora.api.rt3;

/**
 * @author rvbiljouw
 */
public interface Node {

    public Node getPrev();

    public Node getNext();

    public long getHash();

}
