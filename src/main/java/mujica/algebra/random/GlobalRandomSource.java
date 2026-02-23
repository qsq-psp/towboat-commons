package mujica.algebra.random;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicLongFieldUpdater;

@CodeHistory(date = "2022/4/2", project = "Ultramarine")
@CodeHistory(date = "2025/3/2")
public class GlobalRandomSource extends LocalRandomSource {

    public static final GlobalRandomSource INSTANCE = new GlobalRandomSource(0x99b2f581486c9adfL);

    private static final AtomicLongFieldUpdater<LocalRandomSource> X_UPDATER
            = AtomicLongFieldUpdater.newUpdater(LocalRandomSource.class, "x");

    public GlobalRandomSource() {
        super();
    }

    public GlobalRandomSource(long x) {
        super(x);
    }

    @NotNull
    @Override
    public GlobalRandomSource clone() throws CloneNotSupportedException {
        return (GlobalRandomSource) super.clone();
    }

    @Override
    public long getAsLong() {
        while (true) {
            long x = this.x;
            long nx = (x * A + C) & M;
            if (X_UPDATER.compareAndSet(this, x, nx)) {
                return nx;
            }
        }
    }
}
