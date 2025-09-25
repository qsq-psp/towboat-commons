package mujica.ds.generic.heap;

import java.io.Serializable;

/**
 * Created on 2025/6/7.
 */
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
