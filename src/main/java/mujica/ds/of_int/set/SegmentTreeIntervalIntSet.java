package mujica.ds.of_int.set;

import mujica.algebra.discrete.CastToZero;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

@CodeHistory(date = "2026/1/19")
public class SegmentTreeIntervalIntSet extends IntervalIntSet {

    @CodeHistory(date = "2026/1/19")
    private static class SegmentTreeNode {

        SegmentTreeNode left;

        SegmentTreeNode right;

        static final int STATE_ALL_FALSE = 0;
        static final int STATE_ALL_TRUE = 1;
        static final int STATE_OR_FALSE = 2;
        static final int STATE_OR_TRUE = 3;
        static final int STATE_FLIP_OR_FALSE = 4;
        static final int STATE_FLIP_OR_TRUE = 5;

        int state;

        SegmentTreeNode() {
            super();
        }

        SegmentTreeNode(int state) {
            super();
            this.state = state;
        }

        @NotNull
        SegmentTreeNode duplicate() {
            final SegmentTreeNode that = new SegmentTreeNode(state);
            if (STATE_ALL_TRUE < state) {
                if (this.left != null) {
                    that.left = this.left.duplicate();
                }
                if (this.right != null) {
                    that.right = this.right.duplicate();
                }
            }
            return that;
        }

        static int split(int min, int max) {
            return CastToZero.INSTANCE.mean(min, max);
        }

        long longLength(int min, int max) {
            if (state == STATE_ALL_FALSE) {
                return 0;
            }
            final long full = max + 1L - min;
            if (state == STATE_ALL_TRUE) {
                return full;
            }
            final int mid = split(min, max);
            long len = 0L;
            if (left != null) {
                len += left.longLength(min, mid);
            } else if (state == STATE_OR_TRUE || state == STATE_FLIP_OR_FALSE) {
                len += mid + 1L - min;
            }
            if (right != null) {
                len += right.longLength(mid + 1, max);
            } else if (state == STATE_OR_TRUE || state == STATE_FLIP_OR_FALSE) {
                len += ((long) max) - mid;
            }
            if (state == STATE_FLIP_OR_TRUE || state == STATE_FLIP_OR_FALSE) {
                len = full - len;
            }
            return len;
        }

        boolean isEmpty() {
            switch (state) {
                case STATE_ALL_FALSE:
                    return true;
                case STATE_ALL_TRUE:
                    return false;
                case STATE_OR_FALSE:
                    return (left == null || left.isEmpty()) && (right == null || right.isEmpty());
                case STATE_OR_TRUE:
                    return left != null && left.isEmpty() && right != null && right.isEmpty();
                case STATE_FLIP_OR_FALSE:
                    return (left == null || left.isFull()) && (right == null || right.isFull());
                case STATE_FLIP_OR_TRUE:
                    return left != null && left.isFull() && right != null && right.isFull();
                default:
                    throw new IllegalStateException();
            }
        }

        boolean isFull() {
            switch (state) {
                case STATE_ALL_FALSE:
                    return false;
                case STATE_ALL_TRUE:
                    return true;
                case STATE_OR_FALSE:
                    return left != null && left.isFull() && right != null && right.isFull();
                case STATE_OR_TRUE:
                    return (left == null || left.isFull()) && (right == null || right.isFull());
                case STATE_FLIP_OR_FALSE:
                    return left != null && left.isEmpty() && right != null && right.isEmpty();
                case STATE_FLIP_OR_TRUE:
                    return (left == null || left.isEmpty()) && (right == null || right.isEmpty());
                default:
                    throw new IllegalStateException();
            }
        }

        boolean contains(int min, int max, int t) {
            boolean none;
            boolean flip;
            switch (state) {
                case STATE_ALL_FALSE:
                    return false;
                case STATE_ALL_TRUE:
                    return true;
                case STATE_OR_FALSE:
                    none = false;
                    flip = false;
                    break;
                case STATE_OR_TRUE:
                    none = true;
                    flip = false;
                    break;
                case STATE_FLIP_OR_FALSE:
                    none = false;
                    flip = true;
                    break;
                case STATE_FLIP_OR_TRUE:
                    none = true;
                    flip = true;
                    break;
                default:
                    throw new IllegalStateException();
            }
            final int mid = split(min, max);
            if (t <= mid) {
                if (left != null) {
                    return flip ^ left.contains(min, mid, t);
                }
            } else {
                if (right != null) {
                    return flip ^ right.contains(mid + 1, max, t);
                }
            }
            return none;
        }
    }

    @NotNull
    final SegmentTreeNode root;

    final int min, max;

    SegmentTreeIntervalIntSet(@NotNull SegmentTreeNode root, int min, int max) {
        super();
        if (min > max) {
            throw new IllegalArgumentException();
        }
        this.root = root;
        this.min = min;
        this.max = max;
    }

    public SegmentTreeIntervalIntSet(int min, int max) {
        this(new SegmentTreeNode(), min, max);
    }

    public SegmentTreeIntervalIntSet() {
        this(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    @NotNull
    @Override
    public SegmentTreeIntervalIntSet duplicate() {
        return new SegmentTreeIntervalIntSet(root.duplicate(), min, max);
    }

    @Override
    public long longLength() {
        return root.longLength(min, max);
    }

    @Override
    public boolean isEmpty() {
        return root.isEmpty();
    }

    @Override
    public boolean isFull() {
        return root.isFull();
    }

    @Override
    public boolean contains(int t) {
        return root.contains(min, max, t);
    }

    @NotNull
    @Override
    public Iterator<Integer> iterator() {
        return null;
    }
}
