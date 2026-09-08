package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

@CodeHistory(date = "2025/5/23", project = "Ultramarine", name = "BinomialQueue")
@CodeHistory(date = "2025/6/6", name = "BinomialQueue")
@CodeHistory(date = "2026/8/8")
@Name(value = "二项队列", language = "zh")
public class BinomialSlotQueue<S, A, N extends S> extends GapIteratorBasedSlotCollection<S, A> {

    @NotNull
    final SlotArrayComparator.PolicySAC<S, A> comparator;

    @NotNull
    final NodeAllocator2<N> nodeAllocator;

    // for sister node: first = first child, second = next sibling
    @NotNull
    N[] roots;

    @Name(value = "modCount", language = "en")
    transient int version;

    public BinomialSlotQueue(@NotNull SlotArrayComparator.PolicySAC<S, A> comparator, @NotNull NodeAllocator2<N> nodeAllocator) {
        super();
        this.comparator = comparator;
        this.nodeAllocator = nodeAllocator;
        this.roots = nodeAllocator.newNodeArray(comparator.getCapacityPolicy().initialCapacity());
    }

    BinomialSlotQueue(@NotNull SlotArrayComparator.PolicySAC<S, A> comparator, @NotNull NodeAllocator2<N> nodeAllocator, @NotNull N[] roots) {
        this(comparator, nodeAllocator);
        this.roots = roots;
    }

    @NotNull
    @Override
    public SlotCollection<S, A> duplicateNew() {
        return new BinomialSlotQueue<>(comparator, nodeAllocator);
    }

    @NotNull
    @Override
    public SlotCollection<S, A> duplicate() {
        final int thisLength = roots.length;
        int thatLength = 0;
        for (int rootIndex = thisLength; rootIndex > 0; rootIndex--) {
            if (roots[rootIndex - 1] != null) {
                thatLength = rootIndex;
                break;
            }
        }
        final N[] thatRoots = nodeAllocator.newNodeArray(thatLength);
        for (int rootIndex = 0; rootIndex < thatLength; rootIndex++) {
            thatRoots[rootIndex] = duplicateTree(roots[rootIndex]);
        }
        return new BinomialSlotQueue<>(comparator, nodeAllocator, thatRoots);
    }

    @Nullable
    private N duplicateTree(@Nullable N thisNode) {
        if (thisNode == null) {
            return null;
        }
        final N thatNode = nodeAllocator.newNode();
        comparator.move(thisNode, thatNode);
        // tag value is ignored
        nodeAllocator.setFirstLink(thatNode, duplicateTree(nodeAllocator.getFirstLink(thisNode)));
        nodeAllocator.setSecondLink(thatNode, duplicateTree(nodeAllocator.getSecondLink(thisNode)));
        return thatNode;
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        //
    }

    @Override
    public int maxDepth() {
        for (int index = roots.length - 1; index >= 0; index--) {
            if (roots[index] != null) {
                return index + 1;
            }
        }
        return 0;
    }

    @NotNull
    @Override
    protected SlotArrayAllocator<S, A> getAllocator() {
        return comparator;
    }

    @Override
    public int getSize() {
        final int rootLength = roots.length;
        int sum = 0;
        for (int rootIndex = 0; rootIndex < rootLength; rootIndex++) {
            if (roots[rootIndex] == null) {
                continue;
            }
            if (rootIndex >= Integer.SIZE - 1) {
                return Integer.MAX_VALUE;
            }
            sum |= 1 << rootIndex;
        }
        return sum;
    }

    @Override
    public void getItemAt(int index, @NotNull S out) throws IndexOutOfBoundsException {
        if (index != 0) {
            throw new IllegalArgumentException();
        }
        getFirstItem(out);
    }

    @Override
    public void getFirstItem(@NotNull S out) throws NoSuchElementException {
        final int rootLength = roots.length;
        int minRootIndex = -1;
        N selectedNode = null;
        for (int rootIndex = 0; rootIndex < rootLength; rootIndex++) {
            N current = roots[rootIndex];
            if (current != null && (selectedNode == null || comparator.compareSlot(selectedNode, current) > 0)) {
                minRootIndex = rootIndex;
                selectedNode = current;
            }
        }
        if (minRootIndex == -1) {
            throw new NoSuchElementException();
        }
        comparator.move(selectedNode, out);
    }

    @Override
    public void getLastItem(@NotNull S out) throws NoSuchElementException {
        throw new UnsupportedOperationException();
    }

    private void insertTree(@NotNull N thatTree, int index) {
        final int rootLength = roots.length;
        while (true) {
            N thisTree;
            if (index < rootLength) {
                thisTree = roots[index];
            } else {
                thisTree = null;
            }
            if (thisTree == null) {
                if (index >= rootLength) {
                    N[] newRoots = nodeAllocator.newNodeArray(comparator.getCapacityPolicy().largerCapacity(index));
                    System.arraycopy(roots, 0, newRoots, 0, rootLength);
                    roots = newRoots;
                }
                roots[index] = thatTree;
                break;
            }
            roots[index] = null;
            if (comparator.compareSlot(thatTree, thisTree) > 0) {
                N tempTree = thisTree;
                thisTree = thatTree;
                thatTree = tempTree;
            }
            assert nodeAllocator.getSecondLink(thisTree) == null;
            assert nodeAllocator.getSecondLink(thatTree) == null;
            nodeAllocator.setSecondLink(thisTree, nodeAllocator.getFirstLink(thatTree));
            nodeAllocator.setFirstLink(thatTree, thisTree);
            index++;
        }
    }

    @Override
    public void removeItemAt(int index, @Nullable S out) throws IndexOutOfBoundsException {
        if (index != 0) {
            throw new IllegalArgumentException();
        }
        removeFirstItem(out);
    }

    @Override
    public void removeFirstItem(@Nullable S out) throws NoSuchElementException {
        final int rootLength = roots.length;
        int minIndex = -1;
        N minTree = null;
        for (int rootIndex = 0; rootIndex < rootLength; rootIndex++) {
            N tree = roots[rootIndex];
            if (tree != null && (minTree == null || comparator.compareSlot(minTree, tree) > 0)) {
                minIndex = rootIndex;
                minTree = tree;
            }
        }
        if (minIndex == -1) {
            throw new NoSuchElementException();
        }
        roots[minIndex] = null;
        if (out != null) {
            comparator.move(minTree, out);
        }
        N node = nodeAllocator.getFirstLink(minTree); // node = minTree.firstChild;
        nodeAllocator.setFirstLink(minTree, null); // minTree.firstChild = null;
        while (node != null) {
            N next = nodeAllocator.getSecondLink(node); // next = node.nextSibling;
            nodeAllocator.setSecondLink(node, null); // node.nextSibling = null;
            insertTree(node, --minIndex);
            node = next;
        }
        assert minIndex == 0;
        version++;
    }

    @Override
    public void removeLastItem(@Nullable S out) throws NoSuchElementException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertItemBefore(int gapIndex, @NotNull S in) throws IndexOutOfBoundsException {
        insertLastItem(in);
    }

    @Override
    public void insertFirstItem(@NotNull S in) {
        insertLastItem(in);
    }

    @Override
    public void insertLastItem(@NotNull S in) {
        final N node = nodeAllocator.newNode();
        comparator.move(in, node);
        insertTree(node, 0);
        version++;
    }

    @Override
    public void clear() {
        Arrays.fill(roots, null);
        version++;
    }

    @NotNull
    @Override
    public GapIterator<S> iterator(int startGapIndex) {
        return new GapIterator<>() {

            @Nullable
            TraversalFrame<N> frame;

            // item index (0 <= index && index < roots.length) if frame != null
            // gap index (0 <= index && index <= roots.length) if frame == null
            int rootIndex;
            
            final int expectedVersion = version;

            {
                int index = startGapIndex;
                frame = null;
                if (index < 0) {
                    rootIndex = roots.length;
                    while (++index < 0) {
                        previousGap();
                    }
                } else {
                    rootIndex = 0;
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
                    LOOP:
                    for (rootIndex--; rootIndex > 0; rootIndex--) {
                        if (roots[rootIndex] != null) {
                            frame = new TraversalFrame<>(null, roots[rootIndex]);
                            while (true) {
                                N second = nodeAllocator.getSecondLink(frame.node);
                                if (second == null) {
                                    break LOOP;
                                }
                                frame.stage = 2;
                                frame = new TraversalFrame<>(frame, second);
                            }
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
                    LOOP:
                    for (int rootLength = roots.length; rootIndex < rootLength; rootIndex++) {
                        if (roots[rootIndex] != null) {
                            frame = new TraversalFrame<>(null, roots[rootIndex]);
                            while (true) {
                                N first = nodeAllocator.getFirstLink(frame.node);
                                if (first == null) {
                                    break LOOP;
                                }
                                frame.stage = 1;
                                frame = new TraversalFrame<>(frame, first);
                            }
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
                    rootIndex++; // gap0, node0, gap1, node1, gap2, node2, ...
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
                    for (int tempIndex = rootIndex; tempIndex > 0; tempIndex--) {
                        N node = roots[tempIndex - 1];
                        if (node == null) {
                            continue;
                        }
                        while (true) {
                            N second = nodeAllocator.getSecondLink(node);
                            if (second == null) {
                                break;
                            }
                            node = second;
                        }
                        comparator.move(node, out);
                        return;
                    }
                    throw new NoSuchElementException();
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
                    int rootLength = roots.length;
                    for (int tempIndex = rootIndex; tempIndex < rootLength; tempIndex++) {
                        N node = roots[tempIndex];
                        if (node == null) {
                            continue;
                        }
                        while (true) {
                            N first = nodeAllocator.getFirstLink(node);
                            if (first == null) {
                                break;
                            }
                            node = first;
                        }
                        comparator.move(node, out);
                        return;
                    }
                    throw new NoSuchElementException();
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
}
