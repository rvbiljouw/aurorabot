package ms.aurora.api.wrappers;

import ms.aurora.rt3.Bag;
import ms.aurora.rt3.Node;

/**
 * @author Rick
 */
public final class RSBag<T extends Node> {

    private Bag cache;
    private int caret;
    private Node currentNode;

    public RSBag(Bag cache) {
        this.cache = cache;
        caret = 0;
    }

    public T getNext() {
        if (caret > 0 && cache.getSentinels()[caret - 1] != currentNode) {
            Node node = currentNode;
            currentNode = node.getPrev();
            return (T) node;
        }
        while (cache.getSentinels().length > caret) {
            Node node = cache.getSentinels()[caret++].getPrev();
            if (cache.getSentinels()[caret - 1] != node) {
                currentNode = node.getPrev();
                return (T) node;
            }
        }
        return null;
    }

    public int getLength() {
        return cache.getSentinels().length;
    }

    public T get(int index) {
        return (T) (cache.getSentinels().length < index ? cache.getSentinels()[index] : null);
    }

    public Object getRaw(int index) {
        return (cache.getSentinels().length < index ? cache.getSentinels()[index] : null);
    }

    public T getFirst() {
        caret = 0;
        return getNext();
    }
}
