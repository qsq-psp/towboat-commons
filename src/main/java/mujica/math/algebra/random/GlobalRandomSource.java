package mujica.math.algebra.random;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicLongFieldUpdater;

@CodeHistory(date = "2022/4/2", project = "Ultramarine")
@CodeHistory(date = "2025/3/2")
public class GlobalRandomSource extends LocalRandomSource {

    private static final AtomicLongFieldUpdater<LocalRandomSource> X_UPDATER
            = AtomicLongFieldUpdater.newUpdater(LocalRandomSource.class, "x");

    @NotNull
    @Override
    public GlobalRandomSource clone() throws CloneNotSupportedException {
        return (GlobalRandomSource) super.clone();
    }

    @Override
    protected long next32(int bitCount) {
        while (true) {
            long x = this.x;
            long nx = (x * A + C) & M;
            if (X_UPDATER.compareAndSet(this, x, nx)) {
                return nx >>> (H - bitCount);
            }
        }
    }
}
