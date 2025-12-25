package mujica.math.algebra.random;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/12/16.
 */
@CodeHistory(date = "2024/6/15", project = "Ultramarine")
@CodeHistory(date = "2025/12/16")
@ReferencePage(title = "", href = "http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/ARTICLES/mt.pdf")
public class MersenneTwister implements RandomSource {

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

    protected long next32() {
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
        return 0xffffffffL & y;
    }

    @Override
    public long next(int bitCount) {
        assert bitCount > 0;
        if (bitCount <= Integer.SIZE) {
            return next32() >>> (Integer.SIZE - bitCount);
        } else if (bitCount <= Long.SIZE) {
            return next32() | ((next32() >>> (Long.SIZE - bitCount)) << Integer.SIZE);
        } else {
            throw new IllegalArgumentException();
        }
    }
}
