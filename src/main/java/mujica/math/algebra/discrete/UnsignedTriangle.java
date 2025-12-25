package mujica.math.algebra.discrete;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

/**
 * Created in Ultramarine on 2023/10/6, named TriangleUU.
 * Recreated on 2025/2/28.
 */
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

    private static final long POSITIVE_LIMIT = (1L << (Long.SIZE - 1)) - (1L << Integer.SIZE);

    private static final long NEGATIVE_LIMIT = (1L << (Long.SIZE - 1)) + (1L << Integer.SIZE);

    @Override
    public void decode2(long in, @NotNull int[] out) {
        if (in >= 0) {
            if (in < POSITIVE_LIMIT) {
                //
            }
        } else {
            if (NEGATIVE_LIMIT <= in) {
                //
            }
        }
    }

    @Override
    public int encode4(@NotNull byte[] in) {
        return 0;
    }

    @Override
    public void decode4(int in, @NotNull byte[] out) {

    }

    @Override
    public long encode8(@NotNull byte[] in) {
        return 0;
    }

    @Override
    public void decode8(long in, @NotNull byte[] out) {

    }

    @NotNull
    @Override
    public BigInteger encodeN(@NotNull BigInteger[] in) {
        return null;
    }

    @Override
    public void decodeN(@NotNull BigInteger in, @NotNull BigInteger[] out) {

    }
}
