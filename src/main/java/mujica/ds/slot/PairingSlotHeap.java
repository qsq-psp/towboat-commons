package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.function.Consumer;

@CodeHistory(date = "2025/6/1", project = "Ultramarine", name = "PairingHeap")
@CodeHistory(date = "2025/6/5", name = "PairingHeap")
@CodeHistory(date = "2026/8/12")
@ReferencePage(title = "配对堆", href = "https://oi-wiki.org/ds/pairing-heap/")
@Name(value = "配对堆", language = "zh")
public class PairingSlotHeap<S, A, N extends S> extends SlotTree2<S, A, N> {

    private static final long serialVersionUID = 0x213186f1f0f32d9dL;

    public PairingSlotHeap(@NotNull SlotArrayComparator<S, A> comparator, @NotNull NodeAllocator2<N> nodeAllocator) {
        super(comparator, nodeAllocator);
    }

    PairingSlotHeap(@NotNull SlotArrayComparator<S, A> comparator, @NotNull NodeAllocator2<N> nodeAllocator, N root, int size) {
        super(comparator, nodeAllocator);
        this.root = root;
        this.nodeCount = size;
    }

    @NotNull
    @Override
    public SlotCollection<S, A> duplicateNew() {
        return new PairingSlotHeap<>(comparator, nodeAllocator);
    }

    @NotNull
    @Override
    public SlotCollection<S, A> duplicate() {
        return new PairingSlotHeap<>(comparator, nodeAllocator, duplicateTree(root, false), nodeCount);
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        super.checkHealth(consumer);
    }

    @NotNull
    private N meldNotNull(@NotNull N tree0, @NotNull N tree1) {
        assert nodeAllocator.getSecondLink(tree0) == null;
        assert nodeAllocator.getSecondLink(tree1) == null;
        if (comparator.compareSlot(tree0, tree1) > 0) {
            N tree2 = tree0;
            tree0 = tree1;
            tree1 = tree2;
        }
        nodeAllocator.setSecondLink(tree1, nodeAllocator.getFirstLink(tree0));
        nodeAllocator.setFirstLink(tree0, tree1);
        return tree0;
    }

    @Nullable
    private N meldNullable(@Nullable N tree0, @Nullable N tree1) {
        if (tree0 == null) {
            return tree1;
        }
        if (tree1 == null) {
            return tree0;
        }
        return meldNotNull(tree0, tree1);
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
        assert nodeAllocator.getSecondLink(root) == null;
        N head = null;
        N tree0 = null;
        N tree1 = null;
        for (N child = nodeAllocator.getFirstLink(root); child != null; child = nodeAllocator.getSecondLink(child)) {
            if (tree0 == null) {
                tree0 = child;
            } else if (tree1 == null) {
                tree1 = child;
            } else {
                nodeAllocator.setSecondLink(tree0, null);
                nodeAllocator.setSecondLink(tree1, null);
                N tree2 = meldNotNull(tree0, tree1);
                nodeAllocator.setSecondLink(tree2, head);
                head = tree2;
                tree0 = child;
                tree1 = null;
            }
        }
        if (tree0 != null) {
            if (tree1 != null) {
                nodeAllocator.setSecondLink(tree0, null);
                nodeAllocator.setSecondLink(tree1, null);
                N tree2 = meldNotNull(tree0, tree1);
                nodeAllocator.setSecondLink(tree2, head);
                head = tree2;
            } else {
                nodeAllocator.setSecondLink(tree0, head);
                head = tree0;
            }
        }
        if (head != null) {
            while (true) {
                {
                    N next = nodeAllocator.getSecondLink(head);
                    if (next == null) {
                        break;
                    }
                    tree0 = head;
                    tree1 = next;
                }
                head = nodeAllocator.getSecondLink(tree1);
                nodeAllocator.setSecondLink(tree0, null);
                nodeAllocator.setSecondLink(tree1, null);
                N tree2 = meldNotNull(tree0, tree1);
                nodeAllocator.setSecondLink(tree2, head);
                head = tree2;
            }
        }
        root = head;
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
        root = meldNullable(root, node);
        nodeCount++;
        version++;
    }
}
