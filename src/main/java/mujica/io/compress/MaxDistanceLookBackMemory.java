package mujica.io.compress;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/11/3.
 */
@CodeHistory(date = "2025/11/3")
public abstract class MaxDistanceLookBackMemory implements LookBackMemory {

    protected final int maxDistance;

    protected MaxDistanceLookBackMemory(int maxDistance) {
        super();
        if (maxDistance <= 0) {
            throw new IllegalArgumentException();
        }
        this.maxDistance = maxDistance;
    }

    @Override
    public int write(@NotNull byte[] array, int offset, int length) {
        if (length > 0) {
            write(array[offset]);
            return 1;
        } else {
            return 0;
        }
    }

    public void writeFully(@NotNull byte[] array, int offset, int length) {
        while (length > 0) {
            int count = write(array, offset, length);
            assert count > 0;
            offset += count;
            length -= count;
        }
        assert length == 0;
    }

    @Override
    public int copyAndWrite(int distance, @NotNull byte[] array, int offset, int length) {
        if (length > 0) {
            array[offset] = copyAndWrite(distance);
            return 1;
        } else {
            return 0;
        }
    }

    public void copyAndWriteFully(int distance, @NotNull byte[] array, int offset, int length) {
        while (length > 0) {
            int count = copyAndWrite(distance, array, offset, length);
            assert count > 0;
            offset += count;
            length -= count;
        }
        assert length == 0;
    }
}
