package mujica.ds.slot;

import mujica.ds.bit.Bit;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.function.Consumer;

@CodeHistory(date = "2026/8/16")
@Name(value = "二叉搜索树", language = "zh")
public class SearchSlotTree3<S, A, N extends S> extends SlotTree3<S, A, N> {

    public SearchSlotTree3(@NotNull SlotArrayComparator<S, A> comparator, @NotNull NodeAllocator3<N> nodeAllocator) {
        super(comparator, nodeAllocator);
    }

    SearchSlotTree3(@NotNull SlotArrayComparator<S, A> comparator, @NotNull NodeAllocator3<N> nodeAllocator, N root, int nodeCount) {
        super(comparator, nodeAllocator);
        this.root = root;
        this.nodeCount = nodeCount;
    }

    @NotNull
    @Override
    public SlotCollection<S, A> duplicateNew() {
        return new SearchSlotTree3<>(comparator, nodeAllocator);
    }

    @NotNull
    @Override
    public SlotCollection<S, A> duplicate() {
        return new SearchSlotTree3<>(comparator, nodeAllocator, duplicateTree(root, null, false), nodeCount);
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        if (checkReferenceHealth(consumer) && root != null) {
            S previousSlot = comparator.newSlot();
            try {
                checkHealth(new Bit(false), previousSlot, root, consumer);
            } finally {
                comparator.releaseSlot(previousSlot);
            }
        }
    }

    private void checkHealth(@NotNull Bit hasPrevious, @NotNull S previous, @NotNull N current, @NotNull Consumer<RuntimeException> consumer) {
        N child = nodeAllocator.getFirstLink(current);
        if (child != null) {
            if (nodeAllocator.getThirdLink(child) != this) {
                consumer.accept(new RuntimeException("parent " + current + " left child " + child));
            }
            checkHealth(hasPrevious, previous, child, consumer);
        }
        if (hasPrevious.getBit()) {
            if (comparator.compareSlot(previous, current) > 0) {
                consumer.accept(new RuntimeException("not sorted between " + previous + " and " + current));
            }
        } else {
            hasPrevious.setBit(true);
        }
        comparator.move(current, previous);
        child = nodeAllocator.getSecondLink(current);
        if (child != null) {
            if (nodeAllocator.getThirdLink(child) != this) {
                consumer.accept(new RuntimeException("parent " + current + " right child " + child));
            }
            checkHealth(hasPrevious, previous, child, consumer);
        }
    }

    @Override
    public boolean contains(@NotNull S in) {
        return contains(root, in);
    }

    private boolean contains(@Nullable N node, @NotNull S in) {
        if (node == null) {
            return false;
        }
        final int compareResult = comparator.compareSlot(in, node);
        if (compareResult < 0) {
            // search in left subtree
            return contains(nodeAllocator.getFirstLink(node), in);
        } else if (compareResult > 0) {
            // search in right subtree
            return contains(nodeAllocator.getSecondLink(node), in);
        } else {
            return true;
        }
    }

    @Override
    public int countItem(@NotNull S in) {
        return contains(in) ? 1 : 0;
    }

    @Override
    public void removeFirstItem(@Nullable S out) throws NoSuchElementException {
        N node = root;
        if (node == null) {
            throw new NoSuchElementException();
        }
        while (true) {
            N next = nodeAllocator.getFirstLink(node);
            if (next == null) {
                break;
            }
            node = next;
        }
        if (out != null) {
            comparator.move(node, out);
        }
        final N parent = nodeAllocator.getThirdLink(node);
        final N child = nodeAllocator.getSecondLink(node);
        if (parent != null) {
            nodeAllocator.setFirstLink(parent, child);
        } else {
            root = child;
        }
        if (child != null) {
            nodeAllocator.setThirdLink(child, parent);
        }
        nodeCount--;
        version++;
    }

    @Override
    public void removeLastItem(@Nullable S out) throws NoSuchElementException {
        N node = root;
        if (node == null) {
            throw new NoSuchElementException();
        }
        while (true) {
            N next = nodeAllocator.getSecondLink(node);
            if (next == null) {
                break;
            }
            node = next;
        }
        if (out != null) {
            comparator.move(node, out);
        }
        final N parent = nodeAllocator.getThirdLink(node);
        final N child = nodeAllocator.getFirstLink(node);
        if (parent != null) {
            nodeAllocator.setSecondLink(parent, child);
        } else {
            root = child;
        }
        if (child != null) {
            nodeAllocator.setSecondLink(child, parent);
        }
        nodeCount--;
        version++;
    }

    @Override
    public void insertFirstItem(@NotNull S in) {
        //
    }

    @Override
    public void insertLastItem(@NotNull S in) {
        //
    }
}
