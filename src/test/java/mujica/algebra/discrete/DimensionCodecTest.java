package mujica.algebra.discrete;

import mujica.algebra.random.FuzzyContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;

@CodeHistory(date = "2023/10/6")
@CodeHistory(date = "2026/4/25")
public class DimensionCodecTest {

    private static final int REPEAT = 205;

    private static final int SMALL = 1000;

    private final FuzzyContext fc = new FuzzyContext();

    @Test
    public void caseEncodeUnsignedSquare() {
        final DimensionCodec codec = UnsignedSquare.INSTANCE;
        Assert.assertEquals(0L, codec.encode2(new int[] {0, 0}));
        Assert.assertEquals(1L, codec.encode2(new int[] {1, 0}));
        Assert.assertEquals(2L, codec.encode2(new int[] {1, 1}));
        Assert.assertEquals(3L, codec.encode2(new int[] {0, 1}));
        Assert.assertEquals(4L, codec.encode2(new int[] {2, 0}));
        Assert.assertEquals(5L, codec.encode2(new int[] {2, 1}));
        Assert.assertEquals(6L, codec.encode2(new int[] {2, 2}));
        Assert.assertEquals(7L, codec.encode2(new int[] {1, 2}));
        Assert.assertEquals(8L, codec.encode2(new int[] {0, 2}));
        Assert.assertEquals(-1L, codec.encode2(new int[] {0, -1}));
    }

    @Test
    public void caseEncodeUnsignedTriangle() {
        final DimensionCodec codec = UnsignedTriangle.INSTANCE;
        Assert.assertEquals(0L, codec.encode2(new int[] {0, 0}));
        Assert.assertEquals(1L, codec.encode2(new int[] {1, 0}));
        Assert.assertEquals(2L, codec.encode2(new int[] {0, 1}));
        Assert.assertEquals(3L, codec.encode2(new int[] {2, 0}));
        Assert.assertEquals(4L, codec.encode2(new int[] {1, 1}));
        Assert.assertEquals(5L, codec.encode2(new int[] {0, 2}));
        Assert.assertEquals(-3L, codec.encode2(new int[] {-1, -2}));
        Assert.assertEquals(-2L, codec.encode2(new int[] {-2, -1}));
        Assert.assertEquals(-1L, codec.encode2(new int[] {-1, -1}));
    }

    private void checkCastEncode(@NotNull DimensionCodec codec) {
        final int[] v = new int[2];
        final BigInteger[] w = new BigInteger[2];
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            v[0] = fc.nextInt(SMALL);
            v[1] = fc.nextInt(SMALL);
            long g = codec.encode2(v);
            w[0] = BigInteger.valueOf(v[0]);
            w[1] = BigInteger.valueOf(v[1]);
            BigInteger h = codec.encodeN(w);
            Assert.assertEquals(BigInteger.valueOf(g), h);
        }
    }

    private void checkCastDecode(@NotNull DimensionCodec codec) {
        final int[] v = new int[2];
        final BigInteger[] w = new BigInteger[2];
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            long g = fc.nextLong(SMALL);
            codec.decode2(g, v);
            codec.decodeN(BigInteger.valueOf(g), w);
            Assert.assertEquals(BigInteger.valueOf(v[0]), w[0]);
            Assert.assertEquals(BigInteger.valueOf(v[1]), w[1]);
        }
    }

    @Test
    public void checkCast() {
        checkCastEncode(BitInterleave.INSTANCE);
        checkCastDecode(BitInterleave.INSTANCE);
        checkCastEncode(UnsignedSquare.INSTANCE);
        checkCastDecode(UnsignedSquare.INSTANCE);
        checkCastEncode(UnsignedTriangle.INSTANCE);
        checkCastDecode(UnsignedTriangle.INSTANCE);
    }

    private void fuzzAround64(@NotNull DimensionCodec codec) {
        final int[] in = new int[2];
        final int[] out = new int[2];
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            in[0] = fc.nextInt();
            in[1] = fc.nextInt();
            long g = codec.encode2(in);
            codec.decode2(g, out);
            try {
                Assert.assertArrayEquals(in, out);
            } catch (AssertionError e) {
                System.out.println(Arrays.toString(in) + " -> " + g + " -> " + Arrays.toString(out));
                throw e;
            }
        }
    }

    @Test
    public void fuzzAround64() {
        fuzzAround64(BitInterleave.INSTANCE);
        fuzzAround64(UnsignedSquare.INSTANCE);
        fuzzAround64(UnsignedTriangle.INSTANCE);
    }
}
