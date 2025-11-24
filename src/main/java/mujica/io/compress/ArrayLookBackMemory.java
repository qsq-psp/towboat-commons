package mujica.io.compress;

import mujica.ds.of_int.list.ResizePolicy;
import mujica.reflect.modifier.CodeHistory;

import java.io.Serializable;

@CodeHistory(date = "2025/11/1", name = "ArrayLookBackMemory")
public abstract class ArrayLookBackMemory extends MaxDistanceLookBackMemory implements Serializable {

    private static final long serialVersionUID = 0xB0B0048ACDA9F050L;

    final ResizePolicy policy;

    byte[] array;

    public ArrayLookBackMemory(int maxDistance, ResizePolicy policy) {
        super(maxDistance);
        this.policy = policy;
    }

    @Override
    public boolean release() {
        array = null;
        return true;
    }

    @Override
    public String toString() {
        if (array != null) {
            return getClass().getSimpleName() + "[policy = " + policy.summaryToString() + ", array.length = " + array.length + "]";
        } else {
            return getClass().getSimpleName() + "[policy = " + policy.summaryToString() + "]";
        }
    }
}
