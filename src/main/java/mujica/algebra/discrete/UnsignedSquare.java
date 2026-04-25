package mujica.algebra.discrete;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

@CodeHistory(date = "2023/10/6", project = "Ultramarine", name = "SquareUU")
@CodeHistory(date = "2025/2/28")
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

    @NotNull
    @Override
    public BigInteger encodeN(@NotNull BigInteger[] in) {
        if (in.length != 2) {
            throw new UnsupportedOperationException();
        }
        final BigInteger x = in[0];
        final BigInteger y = in[1];
        if (x.compareTo(y) > 0) {
            return x.multiply(x).add(y);
        } else {
            return y.multiply(y.add(BigInteger.TWO)).add(x);
        }
    }

    @Override
    public void decodeN(@NotNull BigInteger in, @NotNull BigInteger[] out) {
        final BigInteger[] m = in.sqrtAndRemainder();
        if (m[0].compareTo(m[1]) > 0) {
            out[0] = m[0];
            out[1] = m[1];
        } else {
            out[0] = m[1].subtract(m[0]);
            out[1] = m[0];
        }
    }
}
