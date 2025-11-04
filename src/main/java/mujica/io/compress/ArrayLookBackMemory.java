package mujica.io.compress;

import mujica.ds.of_int.list.ResizePolicy;
import mujica.reflect.modifier.CodeHistory;

import java.io.Serializable;

@CodeHistory(date = "2025/11/1", name = "ArrayLookBackMemory")
public abstract class ArrayLookBackMemory extends MaxDistanceLookBackMemory implements Serializable { // ArrayCopyLookBackMemory

    private static final long serialVersionUID = 0xB0B0048ACDA9F050L;

    final ResizePolicy policy;

    byte[] array;

    public ArrayLookBackMemory(int maxDistance, ResizePolicy policy) {
        super(maxDistance);
        this.policy = policy;
    }

    @Override
    public void release() {
        array = null;
    }
}
