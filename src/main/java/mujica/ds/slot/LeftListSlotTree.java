package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

@CodeHistory(date = "2025/5/30", project = "Ultramarine", name = "LeftListHeap")
@CodeHistory(date = "2025/6/3", name = "LeftListTree")
@CodeHistory(date = "2026/8/13")
@Name(value = "左偏树", language = "zh")
public class LeftListSlotTree<S, A, N extends S> extends SlotTree2<S, A, N> {

    private static final long serialVersionUID = 0xf90573cb1382c780L;

    public LeftListSlotTree(@NotNull SlotArrayComparator<S, A> comparator, @NotNull NodeAllocator2<N> nodeAllocator) {
        super(comparator, nodeAllocator);
    }

    LeftListSlotTree(@NotNull SlotArrayComparator<S, A> comparator, @NotNull NodeAllocator2<N> nodeAllocator, N root, int size) {
        super(comparator, nodeAllocator);
        this.root = root;
        this.nodeCount = size;
    }

    @NotNull
    @Override
    public SlotCollection<S, A> duplicateNew() {
        return new LeftListSlotTree<>(comparator, nodeAllocator);
    }

    @NotNull
    @Override
    public SlotCollection<S, A> duplicate() {
        return new LeftListSlotTree<>(comparator, nodeAllocator, duplicateTree(root, true), nodeCount);
    }

    private int distanceOf(@Nullable N node) {
        if (node != null) {
            return nodeAllocator.getTag(node);
        } else {
            return -1;
        }
    }

    private void balanceNode(@NotNull N node) {
        final N deepChild = nodeAllocator.getFirstLink(node);
        final N shallowChild = nodeAllocator.getSecondLink(node);
        if (distanceOf(deepChild) < distanceOf(shallowChild)) {
            nodeAllocator.setFirstLink(node, shallowChild);
            nodeAllocator.setSecondLink(node, deepChild);
            nodeAllocator.setTag(node, distanceOf(deepChild) + 1);
        } else {
            nodeAllocator.setTag(node, distanceOf(shallowChild) + 1);
        }
    }

    private void balanceTree(@NotNull N tree) {
        final N deepChild = nodeAllocator.getFirstLink(tree);
        final N shallowChild = nodeAllocator.getSecondLink(tree);
        if (deepChild != null) {
            balanceTree(deepChild);
        }
        if (shallowChild != null) {
            balanceTree(shallowChild);
        }
        balanceNode(tree);
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
        nodeAllocator.setSecondLink(tree0, merge(nodeAllocator.getSecondLink(tree0), tree1));
        balanceNode(tree0);
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
