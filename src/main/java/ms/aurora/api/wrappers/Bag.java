package ms.aurora.api.wrappers;

import ms.aurora.rt3.IBag;
import ms.aurora.rt3.INode;

/**
 * @author Rick
 */
public final class Bag<T extends INode> {

    private IBag cache;
    private int caret;
    private INode currentNode;

    public Bag(IBag cache) {
        this.cache = cache;
        caret = 0;
    }

    public T getNext() {
        if (caret > 0 && cache.getSentinels()[caret - 1] != currentNode) {
            INode node = currentNode;
            currentNode = node.getPrev();
            return (T) node;
        }
        while (cache.getSentinels().length > caret) {
            INode node = cache.getSentinels()[caret++].getPrev();
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
