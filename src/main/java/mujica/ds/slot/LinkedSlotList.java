package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@CodeHistory(date = "2026/7/19")
public class LinkedSlotList<S, A, N extends S> extends VersionSlotList<S, A> {

    @NotNull
    final SlotArrayAllocator<S, A> arrayAllocator;

    @NotNull
    final SlotAllocator.WithNode2<S, N> nodeAllocator;

    N head;

    N tail;

    public LinkedSlotList(@NotNull SlotArrayAllocator<S, A> arrayAllocator, @NotNull SlotAllocator.WithNode2<S, N> nodeAllocator) {
        super();
        this.arrayAllocator = arrayAllocator;
        this.nodeAllocator = nodeAllocator;
    }

    @NotNull
    @Override
    public LinkedSlotList<S, A, N> duplicate() {
        final LinkedSlotList<S, A, N> that = new LinkedSlotList<>(arrayAllocator, nodeAllocator);
        N thisNode = this.head;
        while (thisNode != null) {
            N thatNode = nodeAllocator.newNode();
            nodeAllocator.move(thisNode, thatNode);
            if (that.head == null) {
                that.head = thatNode;
            }
            if (that.tail != null) {
                nodeAllocator.setFirstLink(that.tail, thatNode); // that.tail.next = thatNode;
            }
            nodeAllocator.setSecondLink(thatNode, that.tail); // thatNode.previous = that.tail;
            that.tail = thatNode;
            thisNode = nodeAllocator.getFirstLink(thisNode); // thisNode = thisNode.next;
        }
        return that;
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {

    }

    @NotNull
    @Override
    protected SlotArrayAllocator<S, A> getAllocator() {
        return arrayAllocator;
    }

    @Override
    public int size() {
        int count = 0;
        N node = head;
        while (node != null) {
            count++;
            node = nodeAllocator.getFirstLink(node); // node = node.next;
        }
        return count;
    }

    @Override
    public boolean isEmpty() {
        return head == null;
    }

    @Override
    public void getItemAt(int index, @NotNull S out) throws IndexOutOfBoundsException {
        if (index < 0) {
            throw new IndexOutOfBoundsException();
        }
        N node = head;
        while (node != null) {
            if (index == 0) {
                nodeAllocator.move(node, out);
                return;
            }
            node = nodeAllocator.getFirstLink(node); // node = node.next;
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public void setItemAt(int index, @NotNull S in) throws IndexOutOfBoundsException {
        if (index < 0) {
            throw new IndexOutOfBoundsException();
        }
        N node = head;
        while (node != null) {
            if (index == 0) {
                nodeAllocator.move(in, node);
                return;
            }
            node = nodeAllocator.getFirstLink(node); // node = node.next;
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public void exchangeItemAt(int index, @NotNull S slot) throws IndexOutOfBoundsException {
        if (index < 0) {
            throw new IndexOutOfBoundsException();
        }
        N node = head;
        while (node != null) {
            if (index == 0) {
                nodeAllocator.exchange(node, slot);
                return;
            }
            node = nodeAllocator.getFirstLink(node); // node = node.next;
        }
        throw new IndexOutOfBoundsException();
    }
}
