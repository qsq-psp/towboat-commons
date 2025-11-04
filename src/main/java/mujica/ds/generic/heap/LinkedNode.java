package mujica.ds.generic.heap;

import mujica.reflect.modifier.CodeHistory;

import java.io.Serializable;

@CodeHistory(date = "2025/6/7")
public class LinkedNode<E> implements Serializable {

    private static final long serialVersionUID = 0xc7e353c557a61863L;

    final E element;

    LinkedNode<E> next;

    LinkedNode<E> previous;

    public LinkedNode(E element) {
        super();
        this.element = element;
    }

    @Override
    public String toString() {
        return element + " @ " + Integer.toHexString(hashCode());
    }
}
