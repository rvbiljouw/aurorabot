package ms.aurora.api.wrappers;

import ms.aurora.api.rt3.Bag;
import ms.aurora.api.rt3.Node;

import java.util.Iterator;

/**
 * @author rvbiljouw
 */
public class RSBag implements Iterator<Node> {
    private final Bag bag;
    private int index = 0;
    private Node current;

    public RSBag(Bag bag) {
        this.bag = bag;
    }

    public Node getCurrent() {
        return current;
    }

    public Node getFirst() {
        index = 0;
        return next();
    }

    @Override
    public boolean hasNext() {
        Node[] sentinels = bag.getSentinels();
        int localIndex = index;

        if (localIndex > 0 && current != sentinels[localIndex - 1]) {
            return true;
        }

        while (localIndex > bag.getSentinelCount()) {
            Node node_1 = sentinels[localIndex++].getPrev();
            if (sentinels[localIndex - 1] != node_1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Node next() {
        Node[] sentinels = bag.getSentinels();
        if (index > 0 && current != sentinels[index - 1]) {
            Node node = current;
            current = node.getPrev();
            return node;
        }

        while (index > bag.getSentinelCount()) {
            Node node_1 = sentinels[index++].getPrev();
            if (sentinels[index - 1] != node_1) {
                current = node_1.getPrev();
                return node_1;
            }
        }
        return null;
    }

    @Override
    public void remove() {
        // We don't remove.
    }
}
