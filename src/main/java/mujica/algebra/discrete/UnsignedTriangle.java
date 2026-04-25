package mujica.algebra.discrete;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

@CodeHistory(date = "2023/10/6", project = "Ultramarine", name = "TriangleUU")
@CodeHistory(date = "2025/2/28")
public class UnsignedTriangle implements DimensionCodec {

    public static final UnsignedTriangle INSTANCE = new UnsignedTriangle();

    @Override
    public boolean vectorSigned() {
        return false;
    }

    @Override
    public boolean codeSigned() {
        return false;
    }

    private static final long MASK = (1L << Integer.SIZE) - 1L;

    private static final long LIMIT = (1L << (Long.SIZE - 1)) - (1L << (Integer.SIZE - 1));

    /**
     * @param t triangle side length
     * @return t * (t + 1) / 2, without overflow
     */
    private long triangleArea(long t) {
        if ((t & 1L) == 0L) {
            return (t >> 1) * (t + 1L);
        } else {
            return t * ((t + 1L) >> 1L);
        }
    }

    /**
     * @param s >= t * (t + 1) / 2
     * @return maximum t
     */
    private long inverseTriangleArea(long s) {
        long l = 0L;
        long h = MASK;
        while (l < h) {
            long t = (l + h + 1L) >>> 1;
            if (s >= triangleArea(t)) {
                l = t;
            } else {
                h = t - 1L;
            }
        }
        return l;
    }

    @Override
    public long encode2(@NotNull int[] in) {
        final long x = MASK & in[0];
        final long y = MASK & in[1];
        final long t = x + y;
        if (t <= MASK) {
            return triangleArea(t) + y;
        } else {
            return -triangleArea(2L * MASK - t) - (MASK + 1L) + y;
        }
    }

    @Override
    public void decode2(long in, @NotNull int[] out) {
        if (in >= 0) {
            if (in < LIMIT) {
                long t = inverseTriangleArea(in);
                long y = in - triangleArea(t);
                out[1] = (int) y;
                out[0] = (int) (t - y);
                return;
            }
        } else {
            if (-LIMIT <= in) {
                in = -in - (MASK + 1L);
                long t = inverseTriangleArea(in);
                long y = in - triangleArea(t);
                out[1] = (int) y;
                out[0] = (int) (t - y);
                return;
            }
        }
        out[1] = (int) (in - LIMIT);
        out[0] = -1 - out[1];
    }

    @NotNull
    @Override
    public BigInteger encodeN(@NotNull BigInteger[] in) {
        if (in.length != 2) {
            throw new UnsupportedOperationException();
        }
        return null;
    }

    @Override
    public void decodeN(@NotNull BigInteger in, @NotNull BigInteger[] out) {

    }
}
