package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.function.Consumer;

@CodeHistory(date = "2026/8/10")
@DirectSubclass({CountSlotTree2.class})
@Name(value = "二叉搜索树", language = "zh")
public class SearchSlotTree2<S, A, N extends S> extends SlotTree2<S, A, N> {

    private static final long serialVersionUID = 0x4330fcae5f8942eaL;

    public SearchSlotTree2(@NotNull SlotArrayComparator<S, A> comparator, @NotNull NodeAllocator2<N> nodeAllocator) {
        super(comparator, nodeAllocator);
    }

    SearchSlotTree2(@NotNull SlotArrayComparator<S, A> comparator, @NotNull NodeAllocator2<N> nodeAllocator, N root, int size) {
        super(comparator, nodeAllocator);
        this.root = root;
        this.nodeCount = size;
    }

    @NotNull
    @Override
    public SlotCollection<S, A> duplicateNew() {
        return new SearchSlotTree2<>(comparator, nodeAllocator);
    }

    @NotNull
    @Override
    public SlotCollection<S, A> duplicate() {
        return new SearchSlotTree2<>(comparator, nodeAllocator, duplicateTree(root, false), nodeCount);
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        if (checkReferenceHealth(consumer)) {
            checkHealth(null, root, null, consumer);
        }
    }

    private void checkHealth(@Nullable N leftBound, @Nullable N current, @Nullable N rightBound, @NotNull Consumer<RuntimeException> consumer) {
        if (current == null) {
            return;
        }
        final N leftChild = nodeAllocator.getFirstLink(current);
        final N rightChild = nodeAllocator.getSecondLink(current);
        if (leftChild != null) {
            checkHealth(leftBound, leftChild, current, consumer);
        }
        if (rightChild != null) {
            checkHealth(current, rightChild, rightBound, consumer);
        }
        if (leftBound != null && comparator.compareSlot(current, leftBound) < 0) {
            consumer.accept(new RuntimeException("not sorted between " + current + " and " + leftBound));
        }
        if (rightBound != null && comparator.compareSlot(current, rightBound) > 0) {
            consumer.accept(new RuntimeException("not sorted between " + current + " and " + rightBound));
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
        // todo
        return super.countItem(in);
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
        if (parent != null) {
            nodeAllocator.setFirstLink(parent, nodeAllocator.getSecondLink(current));
        } else {
            root = nodeAllocator.getSecondLink(current);
        }
        nodeCount--;
        version++;
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
        if (parent != null) {
            nodeAllocator.setSecondLink(parent, nodeAllocator.getFirstLink(current));
        } else {
            root = nodeAllocator.getFirstLink(current);
        }
        nodeCount--;
        version++;
    }

    @Override
    public void insertFirstItem(@NotNull S in) {
        if (root != null) {
            insertFirstItem(root, in);
        } else {
            root = nodeAllocator.newNode();
            comparator.move(in, root);
        }
        nodeCount++;
        version++;
    }

    private void insertFirstItem(@NotNull N node, @NotNull S in) {
        if (comparator.compareSlot(in, node) <= 0) {
            N left = nodeAllocator.getFirstLink(node);
            if (left != null) {
                insertFirstItem(left, in);
            } else {
                left = nodeAllocator.newNode();
                comparator.move(in, left);
                nodeAllocator.setFirstLink(node, left);
            }
        } else {
            N right = nodeAllocator.getSecondLink(node);
            if (right != null) {
                insertFirstItem(right, in);
            } else {
                right = nodeAllocator.newNode();
                comparator.move(in, right);
                nodeAllocator.setSecondLink(node, right);
            }
        }
    }

    @Override
    public void insertLastItem(@NotNull S in) {
        if (root != null) {
            insertLastItem(root, in);
        } else {
            N node = nodeAllocator.newNode();
            comparator.move(in, node);
            root = node;
        }
        nodeCount++;
        version++;
    }

    private void insertLastItem(@NotNull N node, @NotNull S in) {
        if (comparator.compareSlot(in, node) < 0) {
            N left = nodeAllocator.getFirstLink(node);
            if (left != null) {
                insertLastItem(left, in);
            } else {
                left = nodeAllocator.newNode();
                comparator.move(in, left);
                nodeAllocator.setFirstLink(node, left);
            }
        } else {
            N right = nodeAllocator.getSecondLink(node);
            if (right != null) {
                insertLastItem(right, in);
            } else {
                right = nodeAllocator.newNode();
                comparator.move(in, right);
                nodeAllocator.setSecondLink(node, right);
            }
        }
    }
}
