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

@CodeHistory(date = "2025/5/30", project = "Ultramarine", name = "LeftListNode")
@CodeHistory(date = "2025/6/1")
class BiasedNode<E> implements Serializable {

    private static final long serialVersionUID = 0x9801141a7e674267L;

    final E element;

    BiasedNode<E> deepChild;

    BiasedNode<E> shallowChild;

    int distance; // only used in LeftListHeap

    static int distanceOf(@Nullable BiasedNode<?> node) {
        if (node != null) {
            return node.distance;
        } else {
            return -1;
        }
    }

    BiasedNode(E element) {
        super();
        this.element = element;
    }

    @NotNull
    BiasedNode<E> duplicate() {
        return duplicate(UnaryOperator.identity());
    }

    @NotNull
    BiasedNode<E> duplicate(@NotNull UnaryOperator<E> operator) {
        final BiasedNode<E> that = new BiasedNode<>(operator.apply(element));
        if (this.deepChild != null) {
            that.deepChild = this.deepChild.duplicate(operator);
        }
        if (this.shallowChild != null) {
            that.shallowChild = this.shallowChild.duplicate(operator);
        }
        that.distance = this.distance;
        return that;
    }

    int checkLeftListHealth(
            @NotNull Comparator<E> comparator, @NotNull Consumer<RuntimeException> consumer
    ) {
        final Map<BiasedNode<E>, String> map = new HashMap<>();
        map.put(this, "on-stack");
        if (deepChild != null) {
            deepChild.checkLeftListHealth(map, comparator, this, consumer);
        }
        if (shallowChild != null) {
            shallowChild.checkLeftListHealth(map, comparator, this, consumer);
        }
        if (distanceOf(deepChild) < distanceOf(shallowChild)) {
            consumer.accept(new InvariantException("node not balanced; deep = " + distanceOf(deepChild) + ", shallow = " + distanceOf(shallowChild)));
        }
        if (distanceOf(shallowChild) + 1 != distance) {
            consumer.accept(new ConsistencyException("distance", distanceOf(shallowChild) + 1, distance));
        }
        map.put(this, "past");
        return map.size();
    }

    void checkLeftListHealth(
            @NotNull Map<BiasedNode<E>, String> map, @NotNull Comparator<E> comparator,
            @NotNull BiasedNode<E> parent, @NotNull Consumer<RuntimeException> consumer
    ) {
        {
            String type = map.put(this, "on-stack");
            if (type != null) {
                consumer.accept(new ReferenceException("revisit " + type + " node with element " + element));
                return; // prevent infinite loop
            }
        }
        if (comparator.compare(element, parent.element) < 0) {
            consumer.accept(new InvariantException("node with element " + element + " smaller than parent"));
        }
        if (deepChild != null) {
            deepChild.checkLeftListHealth(map, comparator, this, consumer);
        }
        if (shallowChild != null) {
            shallowChild.checkLeftListHealth(map, comparator, this, consumer);
        }
        if (distanceOf(deepChild) < distanceOf(shallowChild)) {
            consumer.accept(new InvariantException("node not balanced; deep = " + distanceOf(deepChild) + ", shallow = " + distanceOf(shallowChild)));
        }
        if (distanceOf(shallowChild) + 1 != distance) {
            consumer.accept(new ConsistencyException("distance", distanceOf(shallowChild) + 1, distance));
        }
        map.put(this, "past");
    }

    int checkSkewHealth(
            @NotNull Comparator<E> comparator, @NotNull Consumer<RuntimeException> consumer
    ) {
        final Map<BiasedNode<E>, String> map = new HashMap<>();
        map.put(this, "on-stack");
        if (deepChild != null) {
            deepChild.checkSkewHealth(map, comparator, this, consumer);
        }
        if (shallowChild != null) {
            shallowChild.checkSkewHealth(map, comparator, this, consumer);
        }
        map.put(this, "past");
        return map.size();
    }

    void checkSkewHealth(
            @NotNull Map<BiasedNode<E>, String> map, @NotNull Comparator<E> comparator,
            @NotNull BiasedNode<E> parent, @NotNull Consumer<RuntimeException> consumer
    ) {
        {
            String type = map.put(this, "on-stack");
            if (type != null) {
                consumer.accept(new ReferenceException("revisit " + type + " node with element " + element));
                return; // prevent infinite loop
            }
        }
        if (comparator.compare(element, parent.element) < 0) {
            consumer.accept(new InvariantException("node with element " + element + " smaller than parent"));
        }
        if (deepChild != null) {
            deepChild.checkSkewHealth(map, comparator, this, consumer);
        }
        if (shallowChild != null) {
            shallowChild.checkSkewHealth(map, comparator, this, consumer);
        }
        map.put(this, "past");
    }

    long sumOfDepth(int depth) {
        long sum = depth++;
        if (deepChild != null) {
            sum += deepChild.sumOfDepth(depth);
        }
        if (shallowChild != null) {
            sum += shallowChild.sumOfDepth(depth);
        }
        return sum;
    }

    void balanceNode() {
        if (distanceOf(deepChild) < distanceOf(shallowChild)) {
            BiasedNode<E> child = deepChild;
            deepChild = shallowChild;
            shallowChild = child;
        }
        distance = distanceOf(shallowChild) + 1;
    }

    void balanceTree() {
        if (deepChild != null) {
            deepChild.balanceTree();
        }
        if (shallowChild != null) {
            shallowChild.balanceTree();
        }
        balanceNode();
    }

    void forEach(@NotNull Consumer<? super E> action) {
        action.accept(element);
        if (deepChild != null) {
            deepChild.forEach(action);
        }
        if (shallowChild != null) {
            shallowChild.forEach(action);
        }
    }

    @CodeHistory(date = "2025/6/1")
    class Frame {

        @Nullable
        final Frame parent;

        int stage;

        Frame(@Nullable Frame parent) {
            super();
            this.parent = parent;
        }

        @NotNull
        BiasedNode<E> node() {
            return BiasedNode.this;
        }

        @Nullable
        Frame next() {
            if (stage == 0) {
                stage = 1;
                if (deepChild != null) {
                    return deepChild.new Frame(this);
                }
            }
            if (stage == 1) {
                stage = 2;
                if (shallowChild != null) {
                    return shallowChild.new Frame(this);
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
