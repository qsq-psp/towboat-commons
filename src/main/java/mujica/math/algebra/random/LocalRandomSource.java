package mujica.math.algebra.random;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created in va on 2020/7/20, named JdkLcg.
 * Recreated in Ultramarine on 2022/4/2.
 * Moved here on 2025/3/2.
 */
@CodeHistory(date = "2020/7/20", project = "JdkLcg")
@CodeHistory(date = "2022/4/2", project = "Ultramarine")
@CodeHistory(date = "2025/3/2")
public class LocalRandomSource implements RandomSource, Serializable {

    private static final long serialVersionUID = 0x9699ef914d0e3c86L;

    private static final AtomicLong SX = new AtomicLong(0xB68A5C10441E9194L);

    private static long nextSX() {
        while (true) {
            long current = SX.get();
            long next = current * 0x5851F42D4C957F2DL + 0x14057B7EF767814FL;
            if (SX.compareAndSet(current, next)) {
                return next;
            }
        }
    }

    protected volatile long x;

    protected static final long C_MUT = 0x5DEECE66DL;

    protected static final long C_ADD = 0xBL;

    protected static final long C_MASK = (1L << 48) - 1;

    public LocalRandomSource() {
        super();
        this.x = nextSX() ^ System.currentTimeMillis();
    }

    public LocalRandomSource(@NotNull LocalRandomSource that) {
        super();
        this.x = that.x;
    }

    @Override
    public int next(int bitCount) {
        final long nx = (x * C_MUT + C_ADD) & C_MASK;
        this.x = nx;
        return (int) (nx >>> (48 - bitCount));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof LocalRandomSource && x == ((LocalRandomSource) obj).x;
    }
}
