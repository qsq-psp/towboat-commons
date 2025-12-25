package mujica.math.algebra.random;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import mujica.reflect.modifier.ReferenceCode;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicLong;

@CodeHistory(date = "2020/7/20", project = "JdkLcg")
@CodeHistory(date = "2022/4/2", project = "Ultramarine")
@CodeHistory(date = "2025/3/2")
@ReferenceCode(groupId = "oracle-jdk", artifactId = "java.base", fullyQualifiedName = "java.util.Random")
public class LocalRandomSource implements RandomSource {

    @Name(value = "seed uniquifier", language = "en")
    private static final AtomicLong SX = new AtomicLong(0xb68a5c10441e9194L);

    private static long nextSX() {
        while (true) {
            long current = SX.get();
            long next = current * 0x5851f42d4c957f2dL + 0x14057b7ef767814fL;
            if (SX.compareAndSet(current, next)) {
                return next;
            }
        }
    }

    @Name(value = "multiplier", language = "en")
    protected static final long A = 0x5deece66dL;

    @Name(value = "addend", language = "en")
    protected static final long C = 0xbL;

    protected static final long H = 48;

    @Name(value = "mask", language = "en")
    protected static final long M = (1L << H) - 1;

    @Name(value = "seed", language = "en")
    protected volatile long x;

    public LocalRandomSource() {
        super();
        this.x = nextSX() ^ System.currentTimeMillis();
    }

    @NotNull
    @Override
    public LocalRandomSource clone() throws CloneNotSupportedException {
        return (LocalRandomSource) super.clone();
    }

    protected long next32(int bitCount) {
        final long nx = (x * A + C) & M;
        this.x = nx;
        return nx >>> (H - bitCount);
    }

    @Override
    public long next(int bitCount) {
        assert bitCount > 0;
        if (bitCount <= Integer.SIZE) {
            return next32(bitCount);
        } else if (bitCount <= Long.SIZE) {
            return next32(Integer.SIZE) | (next32(bitCount - Integer.SIZE) << Integer.SIZE);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof LocalRandomSource && x == ((LocalRandomSource) obj).x;
    }
}
