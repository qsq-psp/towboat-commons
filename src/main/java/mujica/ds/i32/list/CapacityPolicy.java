package mujica.ds.i32.list;

import mujica.ds.common.CapacityException;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/6/6", name = "ResizePolicy")
@CodeHistory(date = "2026/7/7")
@DirectSubclass({ShiftCapacityPolicy.class, TwiceCapacityPolicy.class, HalfCapacityPolicy.class, LinearCapacityPolicy.class, LookUpCapacityPolicy.class})
public abstract class CapacityPolicy extends AbstractIntList {

    private static final long serialVersionUID = 0xe7b4b4a342f3c493L;

    @Override
    public abstract int intLength();

    @Override
    public abstract int getInt(int index);

    @Override
    public int min() {
        return getFirst();
    }

    @Override
    public int max() {
        return getLast();
    }

    /**
     * @param minCapacity can be any value, include negative ones
     * @return returnValue >= minCapacity, or returnValue < minCapacity if fails
     */
    public int tryNotSmallerCapacity(int minCapacity) {
        int lastCapacity = -1;
        for (int capacity : this) {
            if (capacity >= minCapacity) {
                return capacity;
            }
            lastCapacity = capacity;
        }
        return lastCapacity;
    }

    /**
     * @param minCapacity can be any value, include negative ones
     * @return returnValue >= minCapacity
     * @throws CapacityException if fails
     */
    public int notSmallerCapacity(int minCapacity) throws CapacityException {
        for (int capacity : this) {
            if (capacity >= minCapacity) {
                return capacity;
            }
        }
        throw new CapacityException();
    }

    /**
     * @param smallCapacity can be any value, include negative ones
     * @return returnValue > smallCapacity, or returnValue == smallCapacity if fails
     */
    public int tryLargerCapacity(int smallCapacity) {
        for (int capacity : this) {
            if (capacity > smallCapacity) {
                return capacity;
            }
        }
        return smallCapacity;
    }

    /**
     * @param smallCapacity can be any value, include negative ones
     * @return returnValue > smallCapacity
     * @throws CapacityException if fails
     */
    public int largerCapacity(int smallCapacity) throws CapacityException {
        for (int capacity : this) {
            if (capacity > smallCapacity) {
                return capacity;
            }
        }
        throw new CapacityException();
    }

    /**
     * @param currentCapacity must be a value returned by this.xxxCapacity(int)
     * @return returnValue > currentCapacity, or returnValue == currentCapacity if fails
     */
    public int tryNextCapacity(int currentCapacity) {
        return tryLargerCapacity(currentCapacity);
    }

    /**
     * @param currentCapacity must be a value returned by this.xxxCapacity(int)
     * @return returnValue > currentCapacity
     * @throws CapacityException if fails
     */
    public int nextCapacity(int currentCapacity) throws CapacityException {
        return largerCapacity(currentCapacity);
    }

    public boolean tryCongruent() {
        return false;
    }

    @NotNull
    public CapacityPolicy requireCongruent() {
        if (tryCongruent()) {
            return this;
        } else {
            throw new CapacityException();
        }
    }

    public boolean tryQuadratic() {
        return false;
    }

    @NotNull
    public CapacityPolicy requireQuadratic() {
        if (tryQuadratic()) {
            return this;
        } else {
            throw new CapacityException();
        }
    }

    public int initialCapacity() {
        return notSmallerCapacity(0);
    }

    /**
     * for rehash policy
     * @return true if the loaded size is too big for the current capacity
     */
    public boolean testLoadedSize(int size, int capacity) {
        return size >= (capacity >> 1); // half
    }

    /**
     * for rehash policy
     * @return true if the link is too long for the current capacity
     */
    public boolean testLinkLength(int length, int capacity) {
        return length >= (capacity >> 2); // quarter
    }
}
