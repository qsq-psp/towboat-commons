package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.function.Consumer;

@CodeHistory(date = "2025/5/24", project = "Ultramarine", name = "FibonacciHeap")
@CodeHistory(date = "2025/6/5", name = "FibonacciHeap")
@CodeHistory(date = "2026/8/10")
@Name(value = "斐波那契堆", language = "zh")
public class FibonacciSlotHeap<S, A, N extends S> extends SlotTree2<S, A, N> {

    private static final long serialVersionUID = 0x61af941e05aab750L;

    public FibonacciSlotHeap(@NotNull SlotArrayComparator<S, A> comparator, @NotNull NodeAllocator2<N> nodeAllocator) {
        super(comparator, nodeAllocator);
    }

    FibonacciSlotHeap(@NotNull SlotArrayComparator<S, A> comparator, @NotNull NodeAllocator2<N> nodeAllocator, N root, int size) {
        super(comparator, nodeAllocator);
        this.root = root;
        this.nodeCount = size;
    }

    @NotNull
    @Override
    public SlotCollection<S, A> duplicateNew() {
        return new FibonacciSlotHeap<>(comparator, nodeAllocator);
    }

    @NotNull
    @Override
    public SlotCollection<S, A> duplicate() {
        return new FibonacciSlotHeap<>(comparator, nodeAllocator, duplicateTree(root, true), nodeCount);
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        super.checkHealth(consumer);
    }

    private void add(@NotNull N[] roots, @NotNull N thatTree) {
        // add thatTree (linked) to roots
        while (true) {
            int thatOrder = nodeAllocator.getTag(thatTree);
            N thisTree = roots[thatOrder];
            if (thisTree == null) {
                roots[thatOrder] = thatTree;
                break;
            }
            assert nodeAllocator.getTag(thisTree) == thatOrder;
            roots[thatOrder] = null;
            if (comparator.compareSlot(thisTree, thatTree) < 0) {
                N tempTree = thisTree;
                thisTree = thatTree;
                thatTree = tempTree;
            }
            assert nodeAllocator.getSecondLink(thisTree) == null;
            assert nodeAllocator.getSecondLink(thatTree) == null;
            nodeAllocator.setSecondLink(thisTree, nodeAllocator.getFirstLink(thatTree));
            nodeAllocator.setFirstLink(thatTree, thisTree);
            nodeAllocator.setTag(thatTree, thatOrder + 1);
        }
    }

    /**
     * @param link0 linked binomial tree with unique order
     * @param link1 linked binomial tree with repeated order
     */
    @Nullable
    private N merge(@Nullable N link0, @Nullable N link1, int totalSize) {
        final int length = Integer.SIZE - Integer.numberOfLeadingZeros(totalSize);
        final N[] roots = nodeAllocator.newNodeArray(length);
        while (link0 != null) {
            N next = nodeAllocator.getSecondLink(link0);
            nodeAllocator.setSecondLink(link0, null);
            int order = nodeAllocator.getTag(link0);
            assert roots[order] == null;
            roots[order] = link0;
            link0 = next;
        }
        while (link1 != null) {
            N next = nodeAllocator.getSecondLink(link1);
            nodeAllocator.setSecondLink(link1, null);
            add(roots, link1);
            link1 = next;
        }
        int minIndex = -1;
        N head = null; // min tree
        for (int index = 0; index < length; index++) {
            N tree = roots[index];
            if (tree != null && (head == null || comparator.compareSlot(head, tree) > 0)) {
                minIndex = index;
                head = tree;
            }
        }
        if (head != null) {
            roots[minIndex] = null;
            N tail = head;
            for (N tree : roots) {
                if (tree == null) {
                    continue;
                }
                assert nodeAllocator.getSecondLink(tail) == null;
                nodeAllocator.setSecondLink(tail, tree);
                tail = tree;
            }
        }
        return head;
    }

    @Override
    public void getFirstItem(@NotNull S out) throws NoSuchElementException {
        if (root == null) {
            throw new NoSuchElementException();
        }
        comparator.move(root, out);
    }

    @Override
    public void removeFirstItem(@Nullable S out) throws NoSuchElementException {
        if (root == null) {
            throw new NoSuchElementException();
        }
        if (out != null) {
            comparator.move(root, out);
        }
        root = merge(
                nodeAllocator.getFirstLink(root),
                nodeAllocator.getSecondLink(root),
                nodeCount
        );
        nodeCount--;
        version++;
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
        nodeAllocator.setTag(node, 0);
        nodeAllocator.setSecondLink(node, null);
        comparator.move(in, node);
        if (root != null && comparator.compareSlot(node, root) > 0) {
            // insert link after root
            nodeAllocator.setSecondLink(node, nodeAllocator.getSecondLink(root));
            nodeAllocator.setSecondLink(root, node);
        } else {
            // insert link before root
            nodeAllocator.setSecondLink(node, root);
            root = node;
        }
        nodeCount++;
        version++;
    }
}
