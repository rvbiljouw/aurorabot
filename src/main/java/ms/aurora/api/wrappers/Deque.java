package ms.aurora.api.wrappers;

import ms.aurora.rt3.IDeque;
import ms.aurora.rt3.INode;

import java.util.Iterator;

/**
 * @author tobiewarburton
 */
public final class Deque implements Iterator<INode> {
    private IDeque deque;
    private INode current;

    public Deque(IDeque deque) {
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
    public INode next() {
        INode node = current;
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
        INode head = deque.getHead();
        for (INode node = head.getNext(); head != node; ) {
            node = node.getNext();
            i++;
        }
        return i;
    }
}
