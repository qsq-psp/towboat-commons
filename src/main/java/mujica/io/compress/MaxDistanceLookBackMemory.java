package mujica.io.compress;

/**
 * Created on 2025/11/3.
 */
public abstract class MaxDistanceLookBackMemory implements LookBackMemory {

    protected final int maxDistance;

    protected MaxDistanceLookBackMemory(int maxDistance) {
        super();
        if (maxDistance <= 0) {
            throw new IllegalArgumentException();
        }
        this.maxDistance = maxDistance;
    }
}
