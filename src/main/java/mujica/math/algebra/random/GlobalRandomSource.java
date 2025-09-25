package mujica.math.algebra.random;

import mujica.reflect.modifier.CodeHistory;

import java.util.concurrent.atomic.AtomicLongFieldUpdater;

/**
 * Created in Ultramarine on 2022/4/2. The thread safe random source.
 * Moved here on 2025/3/2.
 */
@CodeHistory(date = "2022/4/2", project = "Ultramarine")
@CodeHistory(date = "2025/3/2")
public class GlobalRandomSource extends LocalRandomSource {

    private static final long serialVersionUID = 0x61d976f4a712671bL;

    private static final AtomicLongFieldUpdater<LocalRandomSource> X_UPDATER = AtomicLongFieldUpdater.newUpdater(LocalRandomSource.class, "x");

    @Override
    public int next(int bitCount) {
        while (true) {
            long x = this.x;
            long nx = (x * C_MUT + C_ADD) & C_MASK;
            if (X_UPDATER.compareAndSet(this, x, nx)) {
                return (int) (nx >>> (48 - bitCount));
            }
        }
    }
}
