package mujica.ds.generic.heap;

import mujica.ds.ConsistencyException;
import mujica.ds.InvariantException;
import mujica.ds.ReferenceException;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

@CodeHistory(date = "2025/6/1")
class SisterNode<E> implements Serializable {

    private static final long serialVersionUID = 0xee76af765045411aL;

    final E element;

    SisterNode<E> nextSibling;

    SisterNode<E> firstChild;

    int order; // only used in FibonacciHeap

    SisterNode(E element) {
        super();
        this.element = element;
    }

    @NotNull
    SisterNode<E> duplicate() {
        return duplicate(UnaryOperator.identity());
    }

    @NotNull
    SisterNode<E> duplicate(@NotNull UnaryOperator<E> operator) {
        final SisterNode<E> that = new SisterNode<>(operator.apply(element));
        if (this.nextSibling != null) {
            that.nextSibling = this.nextSibling.duplicate(operator);
        }
        if (this.firstChild != null) {
            that.firstChild = this.firstChild.duplicate(operator);
        }
        that.order = this.order;
        return that;
    }

    int checkHealth(@NotNull Map<SisterNode<E>, String> map, @NotNull Consumer<RuntimeException> consumer) {
        {
            String type = map.put(this, "on-stack"); // tag?
            if (type != null) {
                consumer.accept(new ReferenceException("revisit " + type + " node with element " + element));
                return 0; // prevent infinite loop
            }
        }
        int elementCount = 1;
        if (nextSibling != null) {
            elementCount += nextSibling.checkHealth(map, consumer);
        }
        if (firstChild != null) {
            elementCount += firstChild.checkHealth(map, consumer);
        }
        map.put(this, "past"); // tag?
        return elementCount;
    }

    int checkPairingHeapHealth(@NotNull Comparator<E> comparator, @NotNull Consumer<RuntimeException> consumer) {
        final Map<SisterNode<E>, String> map = new HashMap<>();
        map.put(this, "on-stack");
        int elementCount = 1;
        if (nextSibling != null) {
            consumer.accept(new InvariantException("root has sibling(s)"));
            elementCount += nextSibling.checkHealth(map, consumer);
        }
        if (firstChild != null) {
            elementCount += firstChild.checkPairingHeapHealth(map, comparator, this, consumer);
        }
        map.put(this, "past");
        return elementCount; // why not return map size?
    }

    int checkPairingHeapHealth(
            @NotNull Map<SisterNode<E>, String> map, @NotNull Comparator<E> comparator, @NotNull SisterNode<E> parent,
            @NotNull Consumer<RuntimeException> consumer
    ) {
        {
            String type = map.put(this, "on-stack");
            if (type != null) {
                consumer.accept(new ReferenceException("revisit " + type + " node with element " + element));
                return 0; // prevent infinite loop
            }
        }
        if (comparator.compare(element, parent.element) < 0) {
            consumer.accept(new InvariantException("node with element " + element + " smaller than parent"));
        }
        int elementCount = 1;
        if (nextSibling != null) {
            elementCount += nextSibling.checkPairingHeapHealth(map, comparator, parent, consumer);
        }
        if (firstChild != null) {
            elementCount += firstChild.checkPairingHeapHealth(map, comparator, this, consumer);
        }
        map.put(this, "past");
        return elementCount;
    }

    int checkFibonacciHeapHealth(@NotNull Comparator<E> comparator, @NotNull Consumer<RuntimeException> consumer) {
        final Map<SisterNode<E>, String> map = new HashMap<>();
        SisterNode<E> node = this;
        do {
            {
                String type = map.put(node, "on-stack");
                if (type != null) {
                    consumer.accept(new ReferenceException("revisit " + type + " node with element " + element));
                    break; // prevent infinite loop
                }
            }
            if (node.firstChild != null) {
                node.firstChild.checkFibonacciHeapHealth(map, comparator, node, consumer);
            }
            map.put(node, "past");
            node = node.nextSibling;
        } while (node != null);
        return map.size();
    }

    int checkFibonacciHeapHealth(
            @NotNull Map<SisterNode<E>, String> map, @NotNull Comparator<E> comparator, @NotNull SisterNode<E> parent,
            @NotNull Consumer<RuntimeException> consumer
    ) {
        {
            String type = map.put(this, "on-stack");
            if (type != null) {
                consumer.accept(new ReferenceException("revisit " + type + " node with element " + element));
                return 0; // prevent infinite loop
            }
        }
        if (comparator.compare(element, parent.element) < 0) {
            consumer.accept(new InvariantException("node with element " + element + " smaller than parent"));
        }
        int elementCount = 1;
        if (nextSibling != null) {
            elementCount += nextSibling.checkFibonacciHeapHealth(map, comparator, parent, consumer);
            if (firstChild != null) {
                elementCount += firstChild.checkFibonacciHeapHealth(map, comparator, this, consumer);
                if (nextSibling.order != firstChild.order) {
                    consumer.accept(new InvariantException("order not balanced; this.order = " + order + ", nextSibling.order = " + nextSibling.order + ", firstChild.order = " + firstChild.order));
                } else if (nextSibling.order + 1 != order) {
                    consumer.accept(new ConsistencyException("order", nextSibling.order + 1, order));
                }
            } else {
                consumer.accept(new InvariantException("node with sibling but no child; element = " + element + ", order = " + order));
            }
        } else {
            if (firstChild != null) {
                elementCount += firstChild.checkFibonacciHeapHealth(map, comparator, this, consumer);
                consumer.accept(new InvariantException("node with child but no sibling; element = " + element + ", order = " + order));
            } else {
                if (order != 0) {
                    consumer.accept(new ConsistencyException("order", 0, order));
                }
            }
        }
        map.put(this, "past");
        return elementCount;
    }

    void checkBinomialQueueHealth(
            @NotNull Map<SisterNode<E>, String> map, @NotNull String onStackTag, @NotNull String pastTag,
            @NotNull Comparator<E> comparator, @NotNull Consumer<RuntimeException> consumer
    ) {
        {
            String type = map.put(this, onStackTag);
            if (type != null) {
                consumer.accept(new ReferenceException("revisit " + type + " node with element " + element));
                return; // prevent infinite loop
            }
        }
        if (nextSibling != null) {
            consumer.accept(new InvariantException("root has sibling(s)"));
            nextSibling.checkHealth(map, consumer);
        }
        if (firstChild != null) {
            firstChild.checkBinomialQueueHealth(map, onStackTag, pastTag, comparator, this, consumer);
        }
        map.put(this, pastTag);
    }

    void checkBinomialQueueHealth(
            @NotNull Map<SisterNode<E>, String> map, @NotNull String onStackTag, @NotNull String pastTag,
            @NotNull Comparator<E> comparator, @NotNull SisterNode<E> parent,
            @NotNull Consumer<RuntimeException> consumer
    ) {
        {
            String type = map.put(this, onStackTag);
            if (type != null) {
                consumer.accept(new ReferenceException("revisit " + type + " node with element " + element));
                return; // prevent infinite loop
            }
        }
        if (comparator.compare(element, parent.element) < 0) {
            consumer.accept(new InvariantException("node with element " + element + " smaller than parent"));
        }
        int elementCount = 1;
        if (nextSibling != null) {
            nextSibling.checkBinomialQueueHealth(map, onStackTag, pastTag, comparator, parent, consumer);
            if (firstChild != null) {
                firstChild.checkBinomialQueueHealth(map, onStackTag, pastTag, comparator, this, consumer);
                if (nextSibling.order != firstChild.order) {
                    consumer.accept(new InvariantException("order not balanced; nextSibling.order = " + nextSibling.order + ", firstChild.order = " + firstChild.order));
                }
                order = Math.max(nextSibling.order, firstChild.order) + 1;
            } else {
                consumer.accept(new InvariantException("node with sibling but no child; element = " + element));
                order = nextSibling.order + 1;
            }
        } else {
            if (firstChild != null) {
                firstChild.checkBinomialQueueHealth(map, onStackTag, pastTag, comparator, this, consumer);
                consumer.accept(new InvariantException("node with child but no sibling; element = " + element));
                order = firstChild.order + 1;
            } else {
                order = 0;
            }
        }
        map.put(this, pastTag);
    }

    long sumOfDepth(int depth) {
        long sum = depth++;
        if (nextSibling != null) {
            sum += nextSibling.sumOfDepth(depth);
        }
        if (firstChild != null) {
            sum += firstChild.sumOfDepth(depth);
        }
        return sum;
    }

    int calculateOrder() {
        if (nextSibling != null && firstChild != null) {
            nextSibling.calculateOrder();
            return firstChild.calculateOrder() + 1;
        } else {
            return 0;
        }
    }

    void forEach(@NotNull Consumer<? super E> action) {
        action.accept(element);
        if (nextSibling != null) {
            nextSibling.forEach(action);
        }
        if (firstChild != null) {
            firstChild.forEach(action);
        }
    }

    @CodeHistory(date = "2025/6/5")
    class Frame {

        @Nullable
        final Frame parent;

        int stage;

        Frame(@Nullable Frame parent) {
            super();
            this.parent = parent;
        }

        @NotNull
        SisterNode<E> node() {
            return SisterNode.this;
        }

        @Nullable
        Frame next() {
            if (stage == 0) {
                stage = 1;
                if (nextSibling != null) {
                    return nextSibling.new Frame(this);
                }
            }
            if (stage == 1) {
                stage = 2;
                if (firstChild != null) {
                    return firstChild.new Frame(this);
                }
            }
            if (parent != null) {
                return parent.next();
            } else {
                return null;
            }
        }
    }
}
