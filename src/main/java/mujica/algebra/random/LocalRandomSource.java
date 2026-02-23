package mujica.algebra.random;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import mujica.reflect.modifier.ReferenceCode;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;

@CodeHistory(date = "2020/7/20", project = "JdkLcg")
@CodeHistory(date = "2022/4/2", project = "Ultramarine")
@CodeHistory(date = "2025/3/2")
@ReferenceCode(groupId = "oracle-jdk", artifactId = "java.base", fullyQualifiedName = "java.util.Random")
public class LocalRandomSource implements RandomSource, LongSupplier {

    @Name(value = "multiplier", language = "en")
    protected static final long A = 0x5deece66dL;

    @Name(value = "addend", language = "en")
    protected static final long C = 0xbL;

    protected static final int H = 48;

    @Name(value = "mask", language = "en")
    protected static final long M = (1L << H) - 1;

    @Name(value = "seed", language = "en")
    protected volatile long x;

    public LocalRandomSource() {
        this((GlobalRandomSource.INSTANCE.getAsLong() ^ System.currentTimeMillis()) & M);
    }

    public LocalRandomSource(long x) {
        super();
        this.x = x;
    }

    @NotNull
    @Override
    public LocalRandomSource clone() throws CloneNotSupportedException {
        return (LocalRandomSource) super.clone();
    }

    @Override
    public long getAsLong() {
        final long nx = (x * A + C) & M;
        this.x = nx;
        return nx;
    }

    @Override
    public long applyAsLong(int bitCount) {
        if (bitCount <= H) {
            return getAsLong() >>> (H - bitCount);
        } else {
            return (getAsLong() << (bitCount - H)) ^ getAsLong();
        }
    }

    @NotNull
    @Override
    public IntSupplier intBind(int bitCount) {
        if (bitCount == Integer.SIZE) {
            return () -> (int) getAsLong();
        }
        if (!(0 < bitCount && bitCount < Integer.SIZE)) {
            throw new IllegalArgumentException();
        }
        final int shift = H - bitCount;
        return () -> (int) (getAsLong() >>> shift);
    }

    @NotNull
    @Override
    public LongSupplier longBind(int bitCount) {
        if (!(0 < bitCount && bitCount <= Long.SIZE)) {
            throw new IllegalArgumentException();
        }
        final int shift = Math.abs(H - bitCount);
        if (bitCount < H) {
            return () -> getAsLong() >>> shift;
        }
        if (bitCount > H) {
            return () -> (getAsLong() << shift) ^ getAsLong();
        }
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof LocalRandomSource && x == ((LocalRandomSource) obj).x;
    }
}
