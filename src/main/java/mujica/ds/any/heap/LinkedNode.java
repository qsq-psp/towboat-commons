package mujica.ds.any.heap;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@CodeHistory(date = "2025/6/7")
@Name(value = "double linked list node", language = "en")
@Name(value = "双向链表节点", language = "zh")
public class LinkedNode<E> implements Serializable {

    private static final long serialVersionUID = 0xc7e353c557a61863L;

    final E element;

    LinkedNode<E> next;

    LinkedNode<E> previous;

    public LinkedNode(E element) {
        super();
        this.element = element;
    }

    @NotNull
    @Override
    public String toString() {
        return element + " @" + Integer.toHexString(hashCode());
    }
}
