package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

@CodeHistory(date = "2025/6/1", project = "Ultramarine", name = "SkewHeap")
@CodeHistory(date = "2025/6/3", name = "SkewHeap")
@CodeHistory(date = "2026/8/13")
@Name(value = "斜堆", language = "zh")
public class SkewSlotHeap<S, A, N extends S> extends SlotTree2<S, A, N> {

    private static final long serialVersionUID = 0xE1387A097306033CL;

    public SkewSlotHeap(@NotNull SlotArrayComparator<S, A> comparator, @NotNull NodeAllocator2<N> nodeAllocator) {
        super(comparator, nodeAllocator);
    }

    SkewSlotHeap(@NotNull SlotArrayComparator<S, A> comparator, @NotNull NodeAllocator2<N> nodeAllocator, N root, int size) {
        super(comparator, nodeAllocator);
        this.root = root;
        this.nodeCount = size;
    }

    @NotNull
    @Override
    public SlotCollection<S, A> duplicateNew() {
        return new SkewSlotHeap<>(comparator, nodeAllocator);
    }

    @NotNull
    @Override
    public SlotCollection<S, A> duplicate() {
        return new SkewSlotHeap<>(comparator, nodeAllocator, duplicateTree(root, false), nodeCount);
    }

    @Nullable
    private N merge(@Nullable N tree0, @Nullable N tree1) {
        if (tree0 == null) {
            return tree1;
        }
        if (tree1 == null) {
            return tree0;
        }
        if (comparator.compareSlot(tree0, tree1) > 0) {
            N tree2 = tree0;
            tree0 = tree1;
            tree1 = tree2;
        }
        // now tree0 is smaller
        final N child = merge(nodeAllocator.getSecondLink(tree0), tree1);
        nodeAllocator.setSecondLink(tree0, nodeAllocator.getFirstLink(tree0));
        nodeAllocator.setFirstLink(tree0, child);
        return tree0;
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
                nodeAllocator.getSecondLink(root)
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
        nodeAllocator.setSecondLink(node, null);
        comparator.move(in, node);
        root = merge(root, node);
        nodeCount++;
        version++;
    }
}
