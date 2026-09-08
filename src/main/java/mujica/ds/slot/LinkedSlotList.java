package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

@CodeHistory(date = "2026/7/19")
@Name(value = "双向链表", language = "zh")
public class LinkedSlotList<S, A, N extends S> extends GapIteratorBasedSlotCollection<S, A> {

    @NotNull
    final SlotArrayAllocator<S, A> arrayAllocator;

    @NotNull
    final NodeAllocator2<N> nodeAllocator;

    N head;

    N tail;

    @Name(value = "modCount", language = "en")
    int version;

    public LinkedSlotList(@NotNull SlotArrayAllocator<S, A> arrayAllocator, @NotNull NodeAllocator2<N> nodeAllocator) {
        super();
        this.arrayAllocator = arrayAllocator;
        this.nodeAllocator = nodeAllocator;
    }

    @NotNull
    @Override
    public SlotCollection<S, A> duplicateNew() {
        return new LinkedSlotList<>(arrayAllocator, nodeAllocator);
    }

    @NotNull
    @Override
    public LinkedSlotList<S, A, N> duplicate() {
        final LinkedSlotList<S, A, N> that = new LinkedSlotList<>(arrayAllocator, nodeAllocator);
        N thisNode = this.head;
        while (thisNode != null) {
            N thatNode = nodeAllocator.newNode();
            arrayAllocator.move(thisNode, thatNode);
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
        if (head == null || tail == null) {
            if (head != null) {
                consumer.accept(new RuntimeException("only head not null"));
            }
            if (tail != null) {
                consumer.accept(new RuntimeException("only tail not null"));
            }
            return;
        }
        N node = head;
        if (nodeAllocator.getSecondLink(node) != null) {
            consumer.accept(new RuntimeException("unexpected head"));
        }
        while (node != null) {
            N next = nodeAllocator.getFirstLink(node);
            if (next != null) {
                if (nodeAllocator.getSecondLink(next) != node) {
                    consumer.accept(new RuntimeException("bad pair"));
                }
            } else {
                if (node != tail) {
                    consumer.accept(new RuntimeException("unexpected tail"));
                }
            }
            node = next;
        }
    }

    @NotNull
    @Override
    protected SlotArrayAllocator<S, A> getAllocator() {
        return arrayAllocator;
    }

    @Override
    public int getSize() {
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
    public int firstIndexOf(@NotNull S in) {
        int index = 0;
        N node = head;
        while (node != null) {
            if (in.equals(node)) {
                return index;
            }
            index++;
            node = nodeAllocator.getFirstLink(node); // node = node.next;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(@NotNull S in) {
        N node = tail;
        while (node != null) {
            if (in.equals(node)) {
                break;
            }
            node = nodeAllocator.getSecondLink(node); // node = node.previous;
        }
        if (node == null) {
            return -1;
        }
        int index = -1;
        do {
            index++;
            node = nodeAllocator.getSecondLink(node); // node = node.previous;
        } while (node != null);
        return index;
    }

    @Override
    public void getItemAt(int index, @NotNull S out) throws IndexOutOfBoundsException {
        if (index < 0) {
            throw new IndexOutOfBoundsException();
        }
        N node = head;
        while (node != null) {
            if (index == 0) {
                arrayAllocator.move(node, out);
                return;
            }
            index--;
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
                arrayAllocator.move(in, node);
                return;
            }
            index--;
            node = nodeAllocator.getFirstLink(node); // node = node.next;
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public void exchangeItemAt(int index, @NotNull S inOut) throws IndexOutOfBoundsException {
        if (index < 0) {
            throw new IndexOutOfBoundsException();
        }
        N node = head;
        while (node != null) {
            if (index == 0) {
                arrayAllocator.exchange(node, inOut);
                return;
            }
            index--;
            node = nodeAllocator.getFirstLink(node); // node = node.next;
        }
        throw new IndexOutOfBoundsException();
    }

    private void removeNode(@NotNull N node) {
        {
            N previous = nodeAllocator.getSecondLink(node);
            if (previous != null) {
                nodeAllocator.setFirstLink(previous, nodeAllocator.getFirstLink(node)); // node.previous.next = node.next;
            } else {
                assert head == node;
                head = nodeAllocator.getFirstLink(node); // head = node.next;
            }
        }
        {
            N next = nodeAllocator.getFirstLink(node);
            if (next != null) {
                nodeAllocator.setSecondLink(next, nodeAllocator.getSecondLink(node)); // node.next.previous = node.previous;
            } else {
                assert tail == node;
                tail = nodeAllocator.getSecondLink(node); // tail = node.previous
            }
        }
        // node.previous = null;
        // node.next = null;
        nodeAllocator.releaseNode(node);
    }

    @Override
    public void clear() {
        head = null;
        tail = null;
        version++;
    }

    @NotNull
    @Override
    public GapIterator<S> iterator(int startGapIndex) {
        return new GapIterator<>() {

            N previousNode;

            N nextNode;

            int expectedVersion = version;

            {
                int index = startGapIndex;
                if (index < 0) {
                    previousNode = tail;
                    nextNode = null;
                    while (++index < 0) {
                        if (previousNode == null) {
                            throw new IndexOutOfBoundsException();
                        }
                        nextNode = previousNode;
                        previousNode = nodeAllocator.getSecondLink(previousNode);
                    }
                } else {
                    previousNode = null;
                    nextNode = head;
                    while (index-- > 0) {
                        if (nextNode == null) {
                            throw new IndexOutOfBoundsException();
                        }
                        previousNode = nextNode;
                        nextNode = nodeAllocator.getFirstLink(nextNode);
                    }
                }
            }

            @Override
            public boolean previousGap() throws ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                if (previousNode != null) {
                    nextNode = previousNode;
                    previousNode = nodeAllocator.getSecondLink(previousNode);
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public boolean nextGap() throws ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                if (nextNode != null) {
                    previousNode = nextNode;
                    nextNode = nodeAllocator.getFirstLink(nextNode);
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public void getPrevious(@NotNull S out) throws NoSuchElementException, ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                if (previousNode == null) {
                    throw new NoSuchElementException();
                }
                arrayAllocator.move(previousNode, out);
            }

            @Override
            public void getNext(@NotNull S out) throws NoSuchElementException, ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                if (nextNode == null) {
                    throw new NoSuchElementException();
                }
                arrayAllocator.move(nextNode, out);
            }

            @Override
            public void setPrevious(@NotNull S in) throws NoSuchElementException, ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                if (previousNode == null) {
                    throw new NoSuchElementException();
                }
                arrayAllocator.move(in, previousNode);
            }

            @Override
            public void setNext(@NotNull S in) throws NoSuchElementException, ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                if (nextNode == null) {
                    throw new NoSuchElementException();
                }
                arrayAllocator.move(in, nextNode);
            }

            @Override
            public void removePrevious(@Nullable S out) throws NoSuchElementException, ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                if (previousNode == null) {
                    throw new NoSuchElementException();
                }
                if (out != null) {
                    arrayAllocator.move(previousNode, out);
                }
                removeNode(previousNode);
                if (nextNode == null) {
                    previousNode = tail;
                } else {
                    previousNode = nodeAllocator.getSecondLink(nextNode); // previousNode = nextNode.previous;
                }
                expectedVersion = ++version;
            }

            @Override
            public void removeNext(@Nullable S out) throws NoSuchElementException, ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                if (nextNode == null) {
                    throw new NoSuchElementException();
                }
                if (out != null) {
                    arrayAllocator.move(nextNode, out);
                }
                removeNode(nextNode);
                if (previousNode == null) {
                    nextNode = head;
                } else {
                    nextNode = nodeAllocator.getFirstLink(previousNode); // nextNode = previousNode.next;
                }
                expectedVersion = ++version;
            }

            @NotNull
            private N insert(@NotNull S in) throws ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                final N newNode = nodeAllocator.newNode();
                arrayAllocator.move(in, newNode);
                nodeAllocator.setFirstLink(newNode, nextNode);
                nodeAllocator.setSecondLink(newNode, previousNode);
                if (previousNode == null) {
                    assert head == nextNode;
                    head = newNode;
                } else {
                    nodeAllocator.setFirstLink(previousNode, newNode); // previousNode.next = newNode;
                }
                if (nextNode == null) {
                    assert tail == previousNode;
                    tail = newNode;
                } else {
                    nodeAllocator.setSecondLink(nextNode, newNode); // nextNode.previous = newNode;
                }
                expectedVersion = ++version;
                return newNode;
            }

            @Override
            public void insertPrevious(@NotNull S in) throws ConcurrentModificationException {
                previousNode = insert(in);
            }

            @Override
            public void insertNext(@NotNull S in) throws ConcurrentModificationException {
                nextNode = insert(in);
            }
        };
    }

    @NotNull
    @Override
    public String summaryToString() {
        return "size = " + getSize() + ", version = " + version;
    }
}
