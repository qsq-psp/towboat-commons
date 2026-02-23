package mujica.algebra.random;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferenceCode;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;

@CodeHistory(date = "2024/6/15", project = "Ultramarine")
@CodeHistory(date = "2025/12/16")
@ReferencePage(title = "Mersenne Twister: A 623-dimensionally equidistributed uniform pseudorandom number generator", href = "http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/ARTICLES/mt.pdf")
@ReferenceCode(groupId = "org.apache.commons", artifactId = "commons-rng-core", version = "1.6", fullyQualifiedName = "org.apache.commons.rng.core.source32.MersenneTwister")
public class MersenneTwister implements RandomSource, IntSupplier {

    private static final int POOL_SIZE = 624;

    private static final int PERIOD = 397;

    private static final int[] PARAMS = {
            0x0,
            0x9908b0df
    };

    @NotNull
    private final int[] pool;

    private int poolIndex;

    public MersenneTwister() {
        super();
        pool = new int[POOL_SIZE];
    }

    @NotNull
    @Override
    public MersenneTwister clone() throws CloneNotSupportedException {
        return (MersenneTwister) super.clone();
    }

    @Override
    public int getAsInt() {
        int y;
        if (poolIndex >= POOL_SIZE) { // generate N words at one time
            int mtNext = pool[0];
            for (int k = 0; k < POOL_SIZE - PERIOD; ++k) {
                int mtCurr = mtNext;
                mtNext = pool[k + 1];
                y = (mtCurr & 0x80000000) | (mtNext & 0x7fffffff);
                pool[k] = pool[k + PERIOD] ^ (y >>> 1) ^ PARAMS[y & 0x1];
            }
            for (int k = POOL_SIZE - PERIOD; k < POOL_SIZE - 1; k++) {
                int mtCurr = mtNext;
                mtNext = pool[k + 1];
                y = (mtCurr & 0x80000000) | (mtNext & 0x7fffffff);
                pool[k] = pool[k + (PERIOD - POOL_SIZE)] ^ (y >>> 1) ^ PARAMS[y & 0x1];
            }
            y = (mtNext & 0x80000000) | (pool[0] & 0x7fffffff);
            pool[POOL_SIZE - 1] = pool[PERIOD - 1] ^ (y >>> 1) ^ PARAMS[y & 0x1];
            poolIndex = 0;
        }
        y = pool[poolIndex++];
        y ^=  y >>> 11;
        y ^= (y << 7) & 0x9d2c5680;
        y ^= (y << 15) & 0xefc60000;
        y ^=  y >>> 18;
        return y;
    }

    @Override
    public long applyAsLong(int bitCount) {
        assert bitCount > 0;
        if (bitCount <= Integer.SIZE) {
            return getAsInt() >>> (Integer.SIZE - bitCount);
        } else {
            return ((0xffffffffL & getAsInt()) << (bitCount - Integer.SIZE)) ^ (0xffffffffL & getAsInt());
        }
    }

    @NotNull
    @Override
    public IntSupplier intBind(int bitCount) {
        if (bitCount == Integer.SIZE) {
            return this;
        }
        if (!(0 < bitCount && bitCount < Integer.SIZE)) {
            throw new IllegalArgumentException();
        }
        final int shift = Integer.SIZE - bitCount;
        return () -> getAsInt() >>> shift;
    }

    @NotNull
    @Override
    public LongSupplier longBind(int bitCount) {
        if (!(0 < bitCount && bitCount <= Long.SIZE)) {
            throw new IllegalArgumentException();
        }
        final int shift = Math.abs(Integer.SIZE - bitCount);
        if (bitCount <= Integer.SIZE) {
            return () -> (0xffffffffL & getAsInt()) >> shift;
        } else {
            return () -> ((0xffffffffL & getAsInt()) << shift) ^ (0xffffffffL & getAsInt());
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MersenneTwister)) {
            return false;
        }
        final MersenneTwister that = (MersenneTwister) obj;
        return this.poolIndex == that.poolIndex && Arrays.equals(this.pool, that.pool);
    }
}
