package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ConcurrentModificationException;
import java.util.IdentityHashMap;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

@CodeHistory(date = "2025/6/2", name = "BiasedPriorityQueue")
@CodeHistory(date = "2025/6/4", name = "SisterPriorityQueue")
@CodeHistory(date = "2026/8/8")
public abstract class SlotTree2<S, A, N extends S> extends GapIteratorBasedSlotCollection<S, A> {

    private static final long serialVersionUID = 0x39e726abbb9a1b07L;

    @NotNull
    final SlotArrayComparator<S, A> comparator;

    @NotNull
    final NodeAllocator2<N> nodeAllocator;

    // for biased node: first = deep child, second = shallow child, tag = distance (LeftListSlotTree only)
    // for sister node: first = first child, second = next sibling, tag = order (FibonacciSlotHeap only)
    // for search tree: first = left child (smaller), second = right child (larger)
    N root;

    int nodeCount;

    @Name(value = "modCount", language = "en")
    transient int version;

    protected SlotTree2(@NotNull SlotArrayComparator<S, A> comparator, @NotNull NodeAllocator2<N> nodeAllocator) {
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
    protected N duplicateTree(@Nullable N thisNode, boolean copyTag) {
        if (thisNode == null) {
            return null;
        }
        final N thatNode = nodeAllocator.newNode();
        comparator.move(thisNode, thatNode);
        if (copyTag) {
            nodeAllocator.setTag(thatNode, nodeAllocator.getTag(thisNode));
        }
        nodeAllocator.setFirstLink(thatNode, duplicateTree(nodeAllocator.getFirstLink(thisNode), copyTag));
        nodeAllocator.setSecondLink(thatNode, duplicateTree(nodeAllocator.getSecondLink(thisNode), copyTag));
        return thatNode;
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        checkReferenceHealth(consumer);
    }

    protected boolean checkReferenceHealth(@NotNull Consumer<RuntimeException> consumer) {
        final IdentityHashMap<N, String> map = new IdentityHashMap<>();
        final boolean healthy = checkReferenceHealth(root, map, consumer);
        if (nodeCount != map.size()) {
            consumer.accept(new RuntimeException("node count mismatch accumulated " + nodeCount + " distinct " + map.size()));
        }
        return healthy;
    }

    private boolean checkReferenceHealth(@Nullable N node, @NotNull IdentityHashMap<N, String> map, @NotNull Consumer<RuntimeException> consumer) {
        if (node == null) {
            return true;
        }
        final String type = map.put(node, "stack");
        if (type != null) {
            consumer.accept(new RuntimeException("revisit " + type + " node " + node));
            return false;
        }
        final boolean healthy =
                checkReferenceHealth(nodeAllocator.getFirstLink(node), map, consumer)
                        & checkReferenceHealth(nodeAllocator.getSecondLink(node), map, consumer);
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
            TraversalFrame<N> frame;

            boolean atEnd;

            final int expectedVersion = version;

            {
                // frame = null;
                int index = startGapIndex;
                if (index < 0) {
                    atEnd = true;
                    while (++index < 0) {
                        previousGap();
                    }
                } else {
                    atEnd = false;
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
                if (frame != null) {
                    if (frame.stage == 1) {
                        frame = new TraversalFrame<>(frame, nodeAllocator.getFirstLink(frame.node));
                        while (true) {
                            N second = nodeAllocator.getSecondLink(frame.node);
                            if (second == null) {
                                break;
                            }
                            frame.stage = 2;
                            frame = new TraversalFrame<>(frame, second);
                        }
                    }
                    frame.stage = 0;
                } else {
                    if (atEnd && root != null) {
                        frame = new TraversalFrame<>(null, root);
                        while (true) {
                            N second = nodeAllocator.getSecondLink(frame.node);
                            if (second == null) {
                                break;
                            }
                            frame.stage = 2;
                            frame = new TraversalFrame<>(frame, second);
                        }
                    }
                    if (frame == null) {
                        return false;
                    }
                }
                // node backward to gap
                if (nodeAllocator.getFirstLink(frame.node) != null) {
                    frame.stage = 1;
                    return true;
                }
                do {
                    frame = frame.parent;
                } while (frame != null && frame.stage != 2);
                if (frame == null) {
                    atEnd = false;
                }
                return true;
            }

            @Override
            public boolean nextGap() throws ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                // gap forward to node
                if (frame != null) {
                    if (frame.stage == 2) {
                        frame = new TraversalFrame<>(frame, nodeAllocator.getSecondLink(frame.node));
                        while (true) {
                            N first = nodeAllocator.getFirstLink(frame.node);
                            if (first == null) {
                                break;
                            }
                            frame.stage = 1;
                            frame = new TraversalFrame<>(frame, first);
                        }
                    }
                    frame.stage = 0;
                } else {
                    if (!atEnd && root != null) {
                        frame = new TraversalFrame<>(null, root);
                        while (true) {
                            N first = nodeAllocator.getFirstLink(frame.node);
                            if (first == null) {
                                break;
                            }
                            frame.stage = 1;
                            frame = new TraversalFrame<>(frame, first);
                        }
                    }
                    if (frame == null) {
                        return false;
                    }
                }
                // node forward to gap
                if (nodeAllocator.getSecondLink(frame.node) != null) {
                    frame.stage = 2;
                    return true;
                }
                do {
                    frame = frame.parent;
                } while (frame != null && frame.stage != 1);
                if (frame == null) {
                    atEnd = true;
                }
                return true;
            }

            @Override
            public void getPrevious(@NotNull S out) throws NoSuchElementException, ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                // gap backward to node (without modifying frame)
                if (frame != null) {
                    if (frame.stage == 1) {
                        N node = nodeAllocator.getFirstLink(frame.node);
                        while (true) {
                            N second = nodeAllocator.getSecondLink(node);
                            if (second == null) {
                                break;
                            }
                            node = second;
                        }
                        comparator.move(node, out);
                    } else if (frame.stage == 2) {
                        comparator.move(frame.node, out);
                    } else {
                        throw new IllegalStateException();
                    }
                } else {
                    N node = root;
                    if (node != null && atEnd) {
                        while (true) {
                            N second = nodeAllocator.getSecondLink(node);
                            if (second == null) {
                                break;
                            }
                            node = second;
                        }
                        comparator.move(node, out);
                    } else {
                        throw new NoSuchElementException();
                    }
                }
            }

            @Override
            public void getNext(@NotNull S out) throws NoSuchElementException, ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                // gap forward to node (without modifying frame)
                if (frame != null) {
                    if (frame.stage == 1) {
                        comparator.move(frame.node, out);
                    } else if (frame.stage == 2) {
                        N node = nodeAllocator.getSecondLink(frame.node);
                        while (true) {
                            N first = nodeAllocator.getFirstLink(node);
                            if (first == null) {
                                break;
                            }
                            node = first;
                        }
                        comparator.move(node, out);
                    } else {
                        throw new IllegalStateException();
                    }
                } else {
                    N node = root;
                    if (node != null && !atEnd) {
                        while (true) {
                            N first = nodeAllocator.getFirstLink(node);
                            if (first == null) {
                                break;
                            }
                            node = first;
                        }
                        comparator.move(node, out);
                    } else {
                        throw new NoSuchElementException();
                    }
                }
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
