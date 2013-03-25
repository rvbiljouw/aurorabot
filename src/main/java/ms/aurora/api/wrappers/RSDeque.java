package ms.aurora.api.wrappers;

import ms.aurora.rt3.Deque;
import ms.aurora.rt3.Node;

import java.util.Iterator;

/**
 * @author tobiewarburton
 */
public final class RSDeque implements Iterator<Node> {
    private Deque deque;
    private Node current;

    public RSDeque(Deque deque) {
        this.deque = deque;
        if (deque.getHead() != null)
            this.current = deque.getHead().getNext();
        else
            this.current = null;
    }

    @Override
    public boolean hasNext() {
        return current != null && current.getNext() != null;
    }

    @Override
    public Node next() {
        Node node = current;
        if (node == deque.getHead()) {
            current = null;
            return null;
        } else {
            current = node.getNext();
            return node;
        }
    }

    @Override
    public void remove() {
        //not needed
    }

    public int size() {
        int i = 0;
        Node head = deque.getHead();
        for (Node node = head.getNext(); head != node; ) {
            node = node.getNext();
            i++;
        }
        return i;
    }
}
