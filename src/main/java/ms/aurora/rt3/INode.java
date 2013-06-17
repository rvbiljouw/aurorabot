package ms.aurora.rt3;

/**
 * @author Rick
 */
public interface INode {

    INode getPrev();

    INode getNext();

    long getHash();

}
