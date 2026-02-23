package mujica.algebra.random;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import mujica.reflect.modifier.ReferenceCode;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntSupplier;
import java.util.function.LongSupplier;

@CodeHistory(date = "2025/12/14")
@ReferenceCode(groupId = "oracle-jdk", artifactId = "java.base", version = "12", fullyQualifiedName = "java.util.SplittableRandom")
public class SimpleSplitSource implements SplitSource<SimpleSplitSource>, LongSupplier {

    private static final long GOLDEN_GAMMA = 0x9e3779b97f4a7c15L;

    /**
     * Computes Stafford variant 13 of 64bit mix function.
     */
    private static long mix64(long z) {
        z = (z ^ (z >>> 30)) * 0xbf58476d1ce4e5b9L;
        z = (z ^ (z >>> 27)) * 0x94d049bb133111ebL;
        return z ^ (z >>> 31);
    }

    /**
     * Returns the gamma value to use for a new split instance.
     */
    private static long mixGamma(long z) {
        z = (z ^ (z >>> 33)) * 0xff51afd7ed558ccdL; // MurmurHash3 mix constants
        z = (z ^ (z >>> 33)) * 0xc4ceb9fe1a85ec53L;
        z = (z ^ (z >>> 33)) | 1L; // force to be odd
        final int n = Long.bitCount(z ^ (z >>> 1)); // ensure enough transitions
        return (n < 24) ? z ^ 0xaaaaaaaaaaaaaaaaL : z;
    }

    @Name(value = "gamma", language = "en")
    private final long c;

    @Name(value = "seed", language = "en")
    private long x;

    private SimpleSplitSource(long c, long x) {
        super();
        this.c = c;
        this.x = x;
    }

    public SimpleSplitSource(long x) {
        this(GOLDEN_GAMMA, x);
    }

    private SimpleSplitSource(@NotNull SimpleSplitSource that) {
        this(that.c, that.x);
    }

    @NotNull
    @Override
    public SimpleSplitSource clone() throws CloneNotSupportedException {
        return (SimpleSplitSource) super.clone();
    }

    @NotNull
    @Override
    public SimpleSplitSource duplicate() {
        return new SimpleSplitSource(this);
    }

    @NotNull
    public SimpleSplitSource split() {
        x += c;
        long newSeed = mix64(x);
        x += c;
        long newGamma = mixGamma(x);
        return new SimpleSplitSource(newGamma, newSeed);
    }

    @Override
    public long getAsLong() {
        x += c;
        return mix64(x);
    }

    @Override
    public long applyAsLong(int bitCount) {
        return getAsLong() >>> (Long.SIZE - bitCount);
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
        final int shift = Long.SIZE - bitCount;
        return () -> (int) (getAsLong() >>> shift);
    }

    @NotNull
    @Override
    public LongSupplier longBind(int bitCount) {
        if (bitCount == Long.SIZE) {
            return this;
        }
        if (!(0 < bitCount && bitCount <= Long.SIZE)) {
            throw new IllegalArgumentException();
        }
        final int shift = Long.SIZE - bitCount;
        return () -> (int) (getAsLong() >>> shift);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SimpleSplitSource)) {
            return false;
        }
        final SimpleSplitSource that = (SimpleSplitSource) obj;
        return this.c == that.c && this.x == that.x;
    }
}
