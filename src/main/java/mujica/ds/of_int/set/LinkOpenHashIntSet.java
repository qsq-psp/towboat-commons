package mujica.ds.of_int.set;

import mujica.ds.of_int.list.ResizePolicy;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;

/**
 * Created on 2025/6/27.
 */
@CodeHistory(date = "2025/6/27")
public class LinkOpenHashIntSet extends AbstractHashIntSet {

    private static final long serialVersionUID = 0xad45741f369a787fL;

    private static class Node implements Serializable {

        private static final long serialVersionUID = 0xbc13fefc05bec133L;

        final int value;

        Node next;

        Node(int value) {
            super();
            this.value = value;
        }

        Node(int value, Node next) {
            super();
            this.value = value;
            this.next = next;
        }

        @Override
        public String toString() {
            return Integer.toString(value);
        }
    }

    @NotNull
    Node[] array;

    public LinkOpenHashIntSet(@Nullable ResizePolicy policy) {
        super(policy);
        array = new Node[this.policy.initialCapacity()];
    }

    LinkOpenHashIntSet(@NotNull ResizePolicy policy, @NotNull Node[] array) {
        super(policy);
        this.array = array;
    }

    @NotNull
    @Override
    public LinkOpenHashIntSet duplicate() {
        final int m = array.length;
        final Node[] thatArray = new Node[m];
        for (int i = 0; i < m; i++) {
            Node thisNode = array[i];
            if (thisNode == null) {
                continue;
            }
            Node thatNode = new Node(thisNode.value);
            thatArray[i] = thatNode;
            while (thisNode.next != null) {
                thatNode.next = new Node(thisNode.next.value);
                thisNode = thisNode.next;
                thatNode = thatNode.next;
            }
        }
        final LinkOpenHashIntSet that = new LinkOpenHashIntSet(policy, thatArray);
        that.size = this.size;
        return that;
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        //
    }

    private class SafeIterator implements PrimitiveIterator.OfInt {

        int expectedModCount;

        int nextIndex;

        int currentIndex = -1;

        Node nextNode;

        Node currentNode;

        Node previousNode;

        SafeIterator() {
            super();
            final int m = array.length;
            while (nextIndex < m) {
                nextNode = array[nextIndex];
                if (nextNode != null) {
                    break;
                }
                nextIndex++;
            }
            expectedModCount = modCount;
        }

        @Override
        public boolean hasNext() {
            return nextNode != null;
        }

        @Override
        public int nextInt() {
            return 0;
        }
    }

    @NotNull
    @Override
    public PrimitiveIterator.OfInt iterator() {
        return new SafeIterator();
    }
}
