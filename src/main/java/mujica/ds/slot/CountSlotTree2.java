package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.function.Consumer;

@CodeHistory(date = "2026/8/12")
@Name(value = "计数二叉搜索树", language = "zh")
public class CountSlotTree2<S, A, N extends S> extends SearchSlotTree2<S, A, N> {

    private static final long serialVersionUID = 0x98fea61bd7b2f883L;

    long itemCount;

    public CountSlotTree2(@NotNull SlotArrayComparator<S, A> comparator, @NotNull NodeAllocator2<N> nodeAllocator) {
        super(comparator, nodeAllocator);
    }

    CountSlotTree2(@NotNull SlotArrayComparator<S, A> comparator, @NotNull NodeAllocator2<N> nodeAllocator, N root, int nodeCount, long itemCount) {
        super(comparator, nodeAllocator, root, nodeCount);
        this.itemCount = itemCount;
    }

    @NotNull
    @Override
    public SlotCollection<S, A> duplicateNew() {
        return new CountSlotTree2<>(comparator, nodeAllocator);
    }

    @NotNull
    @Override
    public SlotCollection<S, A> duplicate() {
        return new CountSlotTree2<>(comparator, nodeAllocator, duplicateTree(root, true), nodeCount, itemCount);
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        if (checkReferenceHealth(consumer)) {
            long totalItemCount = checkHealth(null, root, null, consumer);
            if (itemCount != totalItemCount) {
                consumer.accept(new RuntimeException("node count mismatch accumulated " + nodeCount + " sum " + totalItemCount));
            }
        }
    }

    private long checkHealth(@Nullable N leftBound, @Nullable N current, @Nullable N rightBound, @NotNull Consumer<RuntimeException> consumer) {
        if (current == null) {
            return 0L;
        }
        long count = nodeAllocator.getTag(current);
        if (count <= 0L) {
            consumer.accept(new RuntimeException("non-positive count " + count + " on node " + current));
        }
        final N leftChild = nodeAllocator.getFirstLink(current);
        final N rightChild = nodeAllocator.getSecondLink(current);
        if (leftChild != null) {
            count = Math.addExact(count, checkHealth(leftBound, leftChild, current, consumer));
        }
        if (rightChild != null) {
            count = Math.addExact(count, checkHealth(current, rightChild, rightBound, consumer));
        }
        if (leftBound != null && comparator.compareSlot(current, leftBound) <= 0) {
            consumer.accept(new RuntimeException("not strict sorted between " + current + " and " + leftBound));
        }
        if (rightBound != null && comparator.compareSlot(current, rightBound) >= 0) {
            consumer.accept(new RuntimeException("not strict sorted between " + current + " and " + rightBound));
        }
        return count;
    }

    @Override
    public int getSize() {
        if (itemCount < Integer.MAX_VALUE) {
            return (int) itemCount;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    private int countItem(@Nullable N node, @NotNull S in) {
        if (node == null) {
            return 0;
        }
        final int compareResult = comparator.compareSlot(in, node);
        if (compareResult < 0) {
            // search in left subtree
            return countItem(nodeAllocator.getFirstLink(node), in);
        } else if (compareResult > 0) {
            // search in right subtree
            return countItem(nodeAllocator.getSecondLink(node), in);
        } else {
            return nodeAllocator.getTag(node);
        }
    }

    @Override
    public void removeFirstItem(@Nullable S out) throws NoSuchElementException {
        N current = root;
        if (current == null) {
            throw new NoSuchElementException();
        }
        N parent = null;
        while (true) {
            N child = nodeAllocator.getFirstLink(current);
            if (child != null) {
                parent = current;
                current = child;
            } else {
                break;
            }
        }
        if (out != null) {
            comparator.move(current, out);
        }
        itemCount--;
        {
            int count = nodeAllocator.getTag(current) - 1;
            if (count > 0) {
                nodeAllocator.setTag(current, count);
                return;
            }
        }
        nodeCount--;
        version++;
        if (parent != null) {
            nodeAllocator.setFirstLink(parent, nodeAllocator.getSecondLink(current));
        } else {
            root = nodeAllocator.getSecondLink(current);
        }
    }

    @Override
    public void removeLastItem(@Nullable S out) throws NoSuchElementException {
        N current = root;
        if (current == null) {
            throw new NoSuchElementException();
        }
        N parent = null;
        while (true) {
            N child = nodeAllocator.getSecondLink(current);
            if (child != null) {
                parent = current;
                current = child;
            } else {
                break;
            }
        }
        if (out != null) {
            comparator.move(current, out);
        }
        itemCount--;
        {
            int count = nodeAllocator.getTag(current) - 1;
            if (count > 0) {
                nodeAllocator.setTag(current, count);
                return;
            }
        }
        nodeCount--;
        version++;
        if (parent != null) {
            nodeAllocator.setSecondLink(parent, nodeAllocator.getFirstLink(current));
        } else {
            root = nodeAllocator.getFirstLink(current);
        }
    }

    @Override
    public void insertItemBefore(int gapIndex, @NotNull S in) {
        insertLastItem(in);
    }

    @Override
    public void insertFirstItem(@NotNull S in) {
        insertLastItem(in);
    }

    @Override
    public void insertLastItem(@NotNull S in) {
        if (root != null) {
            insertLastItem(root, in);
        } else {
            N node = nodeAllocator.newNode();
            nodeAllocator.setTag(node, 1);
            comparator.move(in, node);
            root = node;
            nodeCount++;
            version++;
        }
        itemCount++;
    }

    private void insertLastItem(@NotNull N node, @NotNull S in) {
        final int compareResult = comparator.compareSlot(in, node);
        if (compareResult < 0) {
            N left = nodeAllocator.getFirstLink(node);
            if (left != null) {
                insertLastItem(left, in);
            } else {
                left = nodeAllocator.newNode();
                nodeAllocator.setTag(left, 1);
                comparator.move(in, left);
                nodeAllocator.setFirstLink(node, left);
                nodeCount++;
                version++;
            }
        } else if (compareResult > 0) {
            N right = nodeAllocator.getSecondLink(node);
            if (right != null) {
                insertLastItem(right, in);
            } else {
                right = nodeAllocator.newNode();
                nodeAllocator.setTag(right, 1);
                comparator.move(in, right);
                nodeAllocator.setSecondLink(node, right);
                nodeCount++;
                version++;
            }
        } else {
            nodeAllocator.setTag(node, Math.incrementExact(nodeAllocator.getTag(node)));
        }
    }

    @Override
    public void clear() {
        root = null;
        nodeCount = 0;
        itemCount = 0L;
        version++;
    }

    @Override
    public int countItem(@NotNull S in) {
        return countItem(root, in);
    }

    @NotNull
    @Override
    public String summaryToString() {
        return "nodeCount = " + nodeCount + ", itemCount = " + itemCount + ", maxDepth = " + maxDepth() + ", version = " + version;
    }
}
