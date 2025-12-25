package mujica.io.compress;

import mujica.ds.of_int.list.ResizePolicy;
import mujica.reflect.modifier.CodeHistory;

import java.io.Serializable;

@CodeHistory(date = "2025/11/1", name = "ArrayLookBackMemory")
@CodeHistory(date = "2025/11/26")
public abstract class ArrayRunBuffer extends RunBuffer implements Serializable {

    private static final long serialVersionUID = 0xb0b0048acda9f050L;

    final ResizePolicy policy;

    byte[] array;

    public ArrayRunBuffer(int maxDistance, ResizePolicy policy) {
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
        final StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append("[maxDistance = ").append(maxDistance);
        sb.append(", policy = ").append(policy.summaryToString());
        if (array != null) {
            sb.append(", array.length = ").append(array.length);
        }
        return sb.append("]").toString();
    }
}
