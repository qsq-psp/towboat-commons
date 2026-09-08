package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ConcurrentModificationException;
import java.util.IdentityHashMap;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

@CodeHistory(date = "2026/8/15")
@DirectSubclass({SearchSlotTree3.class})
public abstract class SlotTree3<S, A, N extends S> extends GapIteratorBasedSlotCollection<S, A> {

    @NotNull
    final SlotArrayComparator<S, A> comparator;

    @NotNull
    final NodeAllocator3<N> nodeAllocator;

    // first = left child, second = right child, third = parent
    N root;

    int nodeCount;

    @Name(value = "modCount", language = "en")
    transient int version;

    protected SlotTree3(@NotNull SlotArrayComparator<S, A> comparator, @NotNull NodeAllocator3<N> nodeAllocator) {
        super();
        this.comparator = comparator;
        this.nodeAllocator = nodeAllocator;
    }

    @NotNull
    @Override
    public abstract SlotCollection<S, A> duplicateNew();

    @NotNull
    @Override
    public abstract SlotCollection<S, A> duplicate();

    @Nullable
    protected N duplicateTree(@Nullable N thisNode, @Nullable N thatParentNode, boolean copyTag) {
        if (thisNode == null) {
            return null;
        }
        final N thatNode = nodeAllocator.newNode();
        comparator.move(thisNode, thatNode);
        if (copyTag) {
            nodeAllocator.setTag(thatNode, nodeAllocator.getTag(thisNode));
        }
        nodeAllocator.setFirstLink(thatNode,
                duplicateTree(nodeAllocator.getFirstLink(thisNode), thatNode, copyTag));
        nodeAllocator.setSecondLink(thatNode,
                duplicateTree(nodeAllocator.getSecondLink(thisNode), thatNode, copyTag));
        nodeAllocator.setThirdLink(thatNode, thatParentNode);
        return thatNode;
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        checkReferenceHealth(consumer);
    }

    protected boolean checkReferenceHealth(@NotNull Consumer<RuntimeException> consumer) {
        final IdentityHashMap<N, String> map = new IdentityHashMap<>();
        final boolean healthy = checkReferenceHealth(root, null, map, consumer);
        if (nodeCount != map.size()) {
            consumer.accept(new RuntimeException("node count mismatch accumulated " + nodeCount + " distinct " + map.size()));
        }
        return healthy;
    }

    private boolean checkReferenceHealth(@Nullable N node, @Nullable N expectedParent, @NotNull IdentityHashMap<N, String> map, @NotNull Consumer<RuntimeException> consumer) {
        if (node == null) {
            return true;
        }
        final String type = map.put(node, "stack");
        if (type != null) {
            consumer.accept(new RuntimeException("revisit " + type + " node " + node));
            return false;
        }
        final boolean healthy =
                checkReferenceHealth(nodeAllocator.getFirstLink(node), node, map, consumer)
                        & checkReferenceHealth(nodeAllocator.getSecondLink(node), node, map, consumer);
        {
            N actualParent = nodeAllocator.getThirdLink(node);
            if (expectedParent != actualParent) {
                consumer.accept(new RuntimeException("parent expect " + expectedParent + " actual " + actualParent));
            }
        }
        map.put(node, "past");
        return healthy;
    }

    @NotNull
    @Override
    protected SlotArrayAllocator<S, A> getAllocator() {
        return comparator;
    }

    @Override
    public int maxDepth() {
        return maxDepth(root);
    }

    private int maxDepth(@Nullable N node) {
        if (node == null) {
            return 0;
        }
        return Math.max(
                maxDepth(nodeAllocator.getFirstLink(node)),
                maxDepth(nodeAllocator.getSecondLink(node))
        ) + 1;
    }

    @Override
    public int getSize() {
        return nodeCount;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public void clear() {
        root = null;
        nodeCount = 0;
        version++;
    }

    @Override
    public <T extends S> void forEach(@NotNull Consumer<T> consumer, @NotNull T consumedSlot) {
        forEach(root, consumer, consumedSlot);
    }

    private <T extends S> void forEach(@Nullable N node, @NotNull Consumer<T> consumer, @NotNull T consumedSlot) {
        if (node == null) {
            return;
        }
        forEach(nodeAllocator.getFirstLink(node), consumer, consumedSlot);
        final int expectedVersion = version;
        comparator.move(node, consumedSlot);
        consumer.accept(consumedSlot);
        if (version != expectedVersion) {
            throw new ConcurrentModificationException();
        }
        forEach(nodeAllocator.getSecondLink(node), consumer, consumedSlot);
    }

    @NotNull
    @Override
    public GapIterator<S> iterator(int startGapIndex) {
        return new GapIterator<>() {

            @Nullable
            N node;

            boolean atSecond;

            final int expectedVersion = version;

            {
                // node = null;
                int index = startGapIndex;
                if (index < 0) {
                    atSecond = true;
                    while (++index < 0) {
                        previousGap();
                    }
                } else {
                    atSecond = false;
                    while (index-- > 0) {
                        nextGap();
                    }
                }
            }

            @Override
            public boolean previousGap() throws ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                // gap backward to node
                if (node != null) {
                    if (!atSecond) {
                        node = nodeAllocator.getFirstLink(node);
                        while (true) {
                            N second = nodeAllocator.getSecondLink(node);
                            if (second == null) {
                                break;
                            }
                            node = second;
                        }
                    }
                } else {
                    if (atSecond) {
                        node = root;
                        if (node != null) {
                            while (true) {
                                N second = nodeAllocator.getSecondLink(node);
                                if (second == null) {
                                    break;
                                }
                                node = second;
                            }
                        }
                    }
                }
                if (node == null) {
                    return false;
                }
                // node backward to gap
                if (nodeAllocator.getFirstLink(node) != null) {
                    atSecond = false;
                    return true;
                }
                while (true) {
                    N parent = nodeAllocator.getThirdLink(node);
                    if (node == nodeAllocator.getSecondLink(parent)) {
                        node = parent;
                        atSecond = true;
                        break;
                    } else {
                        node = parent;
                    }
                }
                return true;
            }

            @Override
            public boolean nextGap() throws ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                // gap forward to node
                if (node != null) {
                    if (atSecond) {
                        node = nodeAllocator.getSecondLink(node);
                        while (true) {
                            N first = nodeAllocator.getFirstLink(node);
                            if (first == null) {
                                break;
                            }
                            node = first;
                        }
                    }
                } else {
                    if (!atSecond) {
                        node = root;
                        if (node != null) {
                            while (true) {
                                N first = nodeAllocator.getFirstLink(node);
                                if (first == null) {
                                    break;
                                }
                                node = first;
                            }
                        }
                    }
                }
                if (node == null) {
                    return false;
                }
                // node backward to gap
                if (nodeAllocator.getSecondLink(node) != null) {
                    atSecond = true;
                    return true;
                }
                while (true) {
                    N parent = nodeAllocator.getThirdLink(node);
                    if (node == nodeAllocator.getFirstLink(parent)) {
                        node = parent;
                        atSecond = false;
                        break;
                    } else {
                        node = parent;
                    }
                }
                return true;
            }

            @Override
            public void getPrevious(@NotNull S out) throws NoSuchElementException, ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                // gap backward to node
                N node = this.node;
                if (node != null) {
                    if (!atSecond) {
                        node = nodeAllocator.getFirstLink(node);
                        while (true) {
                            N second = nodeAllocator.getSecondLink(node);
                            if (second == null) {
                                break;
                            }
                            node = second;
                        }
                    }
                } else {
                    node = root;
                    if (node != null && atSecond) {
                        while (true) {
                            N second = nodeAllocator.getSecondLink(node);
                            if (second == null) {
                                break;
                            }
                            node = second;
                        }
                    } else {
                        throw new NoSuchElementException();
                    }
                }
                comparator.move(node, out);
            }

            @Override
            public void getNext(@NotNull S out) throws NoSuchElementException, ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                // gap backward to node
                N node = this.node;
                if (node != null) {
                    if (atSecond) {
                        node = nodeAllocator.getSecondLink(node);
                        while (true) {
                            N first = nodeAllocator.getFirstLink(node);
                            if (first == null) {
                                break;
                            }
                            node = first;
                        }
                    }
                } else {
                    node = root;
                    if (node != null && !atSecond) {
                        while (true) {
                            N first = nodeAllocator.getFirstLink(node);
                            if (first == null) {
                                break;
                            }
                            node = first;
                        }
                    } else {
                        throw new NoSuchElementException();
                    }
                }
                comparator.move(node, out);
            }

            @Override
            public void setPrevious(@NotNull S in) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void setNext(@NotNull S in) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void removePrevious(@Nullable S out) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void removeNext(@Nullable S out) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void insertPrevious(@NotNull S in) {
                insertLastItem(in); // iterator is destroyed
            }

            @Override
            public void insertNext(@NotNull S in) {
                insertLastItem(in); // iterator is destroyed
            }
        };
    }

    @NotNull
    @Override
    public String summaryToString() {
        return "nodeCount = " + nodeCount + ", maxDepth = " + maxDepth() + ", version = " + version;
    }
}
