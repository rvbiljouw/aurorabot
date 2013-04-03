package ms.aurora.rt3;

/**
 * @author Rick
 */
public interface Node {

    Node getPrev();

    Node getNext();

    long getHash();

}
