package mujica.math.algebra.discrete;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

/**
 * Created in Ultramarine on 2023/10/6, named SquareUU.
 * Recreated on 2025/2/28.
 */
public class UnsignedSquare implements DimensionCodec {

    public static final UnsignedSquare INSTANCE = new UnsignedSquare();

    @Override
    public boolean vectorSigned() {
        return false;
    }

    @Override
    public boolean codeSigned() {
        return false;
    }

    private static final long MASK = (1L << Integer.SIZE) - 1L;

    @Override
    public long encode2(@NotNull int[] in) {
        final long x = MASK & in[0];
        final long y = MASK & in[1];
        if (x > y) {
            return x * x + y;
        } else {
            return y * (y + 2L) - x;
        }
    }

    @Override
    public void decode2(long in, @NotNull int[] out) {
        long low = 0L;
        long high = MASK;
        while (low < high) {
            long mid = (low + high + 1L) >> 1;
            if (mid * mid <= in) {
                low = mid;
            } else {
                high = mid - 1L;
            }
        }
        high = in - low * low;
        assert 0 <= high;
        assert high <= 2L * low;
        if (high <= low) {
            out[0] = (int) low;
            out[1] = (int) high;
        } else {
            out[0] = (int) (2L * low - high);
            out[1] = (int) low;
        }
    }

    @Override
    public int encode4(@NotNull byte[] in) {
        return 0;
    }

    @Override
    public void decode4(int in, @NotNull byte[] out) {
        //
    }

    @Override
    public long encode8(@NotNull byte[] in) {
        return 0;
    }

    @Override
    public void decode8(long in, @NotNull byte[] out) {
        //
    }

    @NotNull
    @Override
    public BigInteger encodeN(@NotNull BigInteger[] in) {
        return in[0];
    }

    @Override
    public void decodeN(@NotNull BigInteger in, @NotNull BigInteger[] out) {
        //
    }
}
