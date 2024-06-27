package indi.qsq.util.math;

import indi.qsq.util.ds.BigIntegerArray;
import indi.qsq.util.random.FuzzyContext;
import indi.qsq.util.random.RandomContext;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.*;

/**
 * Created on 2023/10/6.
 */
public class DimensionCodecTest {

    private final FuzzyContext fc = new FuzzyContext();

    private static final int DIMENSION = 6;

    private static final int NUMBER = 490;

    @Test
    public void caseEncode2TriangleUU() {
        final DimensionCodec codec = DimensionCodec.TriangleUU.INSTANCE;
        assertEquals(0L, codec.encode2(new int[] {0, 0}));
        assertEquals(1L, codec.encode2(new int[] {1, 0}));
        assertEquals(2L, codec.encode2(new int[] {0, 1}));
        assertEquals(3L, codec.encode2(new int[] {2, 0}));
        assertEquals(4L, codec.encode2(new int[] {1, 1}));
        assertEquals(5L, codec.encode2(new int[] {0, 2}));
    }

    @Test
    public void caseEncode3TriangleUU() {
        final DimensionCodec codec = DimensionCodec.TriangleUU.INSTANCE;
        assertEquals(0, codec.encode3(new byte[] {0, 0, 0}));
        assertEquals(1, codec.encode3(new byte[] {1, 0, 0}));
        assertEquals(2, codec.encode3(new byte[] {0, 1, 0}));
        assertEquals(3, codec.encode3(new byte[] {0, 0, 1}));
        assertEquals(4, codec.encode3(new byte[] {2, 0, 0}));
        assertEquals(5, codec.encode3(new byte[] {1, 1, 0}));
        assertEquals(6, codec.encode3(new byte[] {1, 0, 1}));
        assertEquals(7, codec.encode3(new byte[] {0, 2, 0}));
        assertEquals(8, codec.encode3(new byte[] {0, 1, 1}));
        assertEquals(9, codec.encode3(new byte[] {0, 0, 2}));
    }

    @Test
    public void caseEncode2SquareUU() {
        final DimensionCodec codec = DimensionCodec.SquareUU.INSTANCE;
        assertEquals(0L, codec.encode2(new int[] {0, 0}));
        assertEquals(1L, codec.encode2(new int[] {1, 0}));
        assertEquals(2L, codec.encode2(new int[] {0, 1}));
        assertEquals(3L, codec.encode2(new int[] {1, 1}));
        assertEquals(4L, codec.encode2(new int[] {2, 0}));
        assertEquals(5L, codec.encode2(new int[] {2, 1}));
        assertEquals(6L, codec.encode2(new int[] {0, 2}));
        assertEquals(7L, codec.encode2(new int[] {1, 2}));
        assertEquals(8L, codec.encode2(new int[] {2, 2}));
    }
    /*

    private long pack(int x, int y) {
        return (((long) x) << Integer.SIZE) | (0xffffffffL & y);
    }

    private void testUniqueEncode2(DimensionCodec codec) {
        final HashSet<Long> coords = new HashSet<>();
        final HashSet<Long> codes = new HashSet<>();
        final int[] in = new int[2];
        for (int index = 0; index < NUMBER; index++) {
            in[0] = fc.fuzzyInt();
            in[1] = fc.fuzzyInt();
            try {
                assertEquals(coords.add(pack(in[0], in[1])), codes.add(codec.encode2(in)));
            } catch (ArithmeticException ignore) {}
        }
    }

    @Test
    public void testUniqueEncode2() {
        testUniqueEncode2(DimensionCodec.BitInterleaveUU.INSTANCE);
        testUniqueEncode2(DimensionCodec.TriangleUU.INSTANCE);
        testUniqueEncode2(DimensionCodec.SquareUU.INSTANCE);
    }

    private void testUniqueDecode2(DimensionCodec codec) {
        final HashSet<Long> coords = new HashSet<>();
        final HashSet<Long> codes = new HashSet<>();
        final int[] out = new int[2];
        for (int index = 0; index < NUMBER; index++) {
            long code = fc.fuzzyLong();
            try {
                codec.decode2(code, out);
            } catch (ArithmeticException ignore) {
                continue;
            }
            assertEquals(codes.add(code), coords.add(pack(out[0], out[1])));
        }
    }

    @Test
    public void testUniqueDecode2() {
        testUniqueDecode2(DimensionCodec.BitInterleaveUU.INSTANCE);
        testUniqueDecode2(DimensionCodec.TriangleUU.INSTANCE);
    }

    private void testAround2(DimensionCodec codec) {
        final int[] array = new int[2];
        for (int index = 0; index < NUMBER; index++) {
            long c0 = fc.fuzzyLong();
            try {
                codec.decode2(c0, array);
                assertEquals(c0, codec.encode2(array));
            } catch (ArithmeticException ignore) {}
        }
    }

    @Test
    public void testAround2() {
        testAround2(DimensionCodec.BitInterleaveUU.INSTANCE);
        testAround2(DimensionCodec.TriangleUU.INSTANCE);
    }

    private void testAround2B(DimensionCodec codec) {
        final BigInteger[] array = new BigInteger[2];
        for (int index = 0; index < NUMBER; index++) {
            BigInteger c0 = BigInteger.valueOf(fc.fuzzyLong());
            if (c0.signum() < 0 && !codec.inputSigned()) {
                continue;
            }
            codec.decode2(c0, array);
            try {
                if (!codec.outputSigned()) {
                    assertTrue(array[0].signum() >= 0);
                    assertTrue(array[1].signum() >= 0);
                }
                assertEquals(c0, codec.encode2(array));
            } catch (AssertionError e) {
                System.out.println(Arrays.toString(array));
                throw e;
            }
        }
    }

    @Test
    public void testAround2B() {
        testAround2B(DimensionCodec.BitInterleaveUU.INSTANCE);
        testAround2B(DimensionCodec.TriangleUU.INSTANCE);
        testAround2B(DimensionCodec.SquareUU.INSTANCE);
    }

    private void testAround3(DimensionCodec codec) {
        final byte[] in = new byte[3];
        final byte[] out = new byte[3];
        for (int index = 0; index < NUMBER; index++) {
            fc.fuzzyBytes(in, 0, 3);
            try {
                codec.decode3(codec.encode3(in), out);
                assertArrayEquals(in, out);
            } catch (ArithmeticException ignore) {}
        }
    }

    @Test
    public void testAround3() {
        testAround3(DimensionCodec.BitInterleaveUU.INSTANCE);
    }

    private void testAround3B(DimensionCodec codec) {
        final BigInteger[] array = new BigInteger[3];
        for (int index = 0; index < NUMBER; index++) {
            BigInteger c0 = BigInteger.valueOf(fc.fuzzyLong());
            if (c0.signum() < 0 && !codec.inputSigned()) {
                continue;
            }
            codec.decode3(c0, array);
            try {
                if (!codec.outputSigned()) {
                    assertTrue(array[0].signum() >= 0);
                    assertTrue(array[1].signum() >= 0);
                    assertTrue(array[2].signum() >= 0);
                }
                assertEquals(c0, codec.encode3(array));
            } catch (AssertionError e) {
                System.out.println(Arrays.toString(array));
                throw e;
            }
        }
    }

    @Test
    public void testAround3B() {
        testAround3B(DimensionCodec.BitInterleaveUU.INSTANCE);
    }

    private void testAround4(DimensionCodec codec) {
        final byte[] array = new byte[4];
        for (int index = 0; index < NUMBER; index++) {
            int c0 = fc.fuzzyInt();
            try {
                codec.decode4(c0, array);
                assertEquals(c0, codec.encode4(array));
            } catch (ArithmeticException ignore) {}
        }
    }

    @Test
    public void testAround4() {
        testAround4(DimensionCodec.BitInterleaveUU.INSTANCE);
    }
    */

    private void testUniqueEncodeNB(DimensionCodec codec) {
        final HashSet<BigIntegerArray> vectors = new HashSet<>();
        final HashSet<BigInteger> codes = new HashSet<>();
        for (int dimension = 1; dimension <= DIMENSION; dimension++) {
            BigInteger[] array = new BigInteger[dimension];
            for (int testIndex = 0; testIndex < NUMBER; testIndex++) {
                for (int index = 0; index < dimension; index++) {
                    long value = fc.fuzzyLong();
                    if (!codec.vectorSigned()) {
                        value &= Long.MAX_VALUE;
                    }
                    array[index] = BigInteger.valueOf(value);
                }
                BigInteger[] copy = Arrays.copyOf(array, dimension);
                BigInteger code = null;
                try {
                    code = codec.encodeN(array);
                    if (!codec.codeSigned()) {
                        assertTrue(code.signum() >= 0);
                    }
                    assertEquals(vectors.add(new BigIntegerArray(copy)), codes.add(code));
                } catch (AssertionError e) {
                    System.out.println(code);
                    System.out.println(Arrays.toString(array));
                    throw e;
                }
            }
            vectors.clear();
            codes.clear();
        }
    }

    @Test
    @Category(RandomContext.class)
    public void testUniqueEncodeNB() {
        testUniqueEncodeNB(DimensionCodec.BitInterleaveUU.INSTANCE);
        testUniqueEncodeNB(DimensionCodec.TriangleUU.INSTANCE);
    }

    private void testUniqueDecodeNB(DimensionCodec codec) {
        final HashSet<BigInteger> codes = new HashSet<>();
        final HashSet<BigIntegerArray> vectors = new HashSet<>();
        for (int dimension = 1; dimension <= DIMENSION; dimension++) {
            BigInteger[] array = new BigInteger[dimension];
            for (int testIndex = 0; testIndex < NUMBER; testIndex++) {
                long value = fc.fuzzyLong();
                if (!codec.vectorSigned()) {
                    value &= Long.MAX_VALUE;
                }
                BigInteger code = BigInteger.valueOf(value);
                try {
                    codec.decodeN(code, array);
                    BigInteger[] copy = Arrays.copyOf(array, dimension);
                    assertEquals(codes.add(code), vectors.add(new BigIntegerArray(copy)));
                } catch (AssertionError e) {
                    System.out.println(code);
                    System.out.println(Arrays.toString(array));
                    throw e;
                }
            }
            codes.clear();
            vectors.clear();
        }
    }

    @Test
    @Category(RandomContext.class)
    public void testUniqueDecodeNB() {
        testUniqueDecodeNB(DimensionCodec.BitInterleaveUU.INSTANCE);
        testUniqueDecodeNB(DimensionCodec.TriangleUU.INSTANCE);
    }

    private void testAroundNB(DimensionCodec codec) {
        for (int dimension = 1; dimension <= DIMENSION; dimension++) {
            BigInteger[] array = new BigInteger[dimension];
            for (int testIndex = 0; testIndex < NUMBER; testIndex++) {
                BigInteger code = BigInteger.valueOf(fc.fuzzyLong());
                if (code.signum() < 0 && !codec.vectorSigned()) {
                    continue;
                }
                codec.decodeN(code, array);
                try {
                    if (!codec.codeSigned()) {
                        for (BigInteger v : array) {
                            assertTrue(v.signum() >= 0);
                        }
                    }
                    assertEquals(code, codec.encodeN(array));
                } catch (AssertionError e) {
                    System.out.println(code);
                    System.out.println(Arrays.toString(array));
                    throw e;
                }
            }
        }
    }

    @Test
    @Category(RandomContext.class)
    public void testAroundNB() {
        testAroundNB(DimensionCodec.BitInterleaveUU.INSTANCE);
        testAroundNB(DimensionCodec.TriangleUU.INSTANCE);
    }

    /*
    private void testConsistenceEncode2(DimensionCodec codec) {
        final int[] primitiveIn = new int[2];
        final BigInteger[] bigIn = new BigInteger[2];
        for (int index = 0; index < NUMBER; index++) {
            primitiveIn[0] = fc.fuzzyInt();
            primitiveIn[1] = fc.fuzzyInt();
            bigIn[0] = BigInteger.valueOf(0xffffffffL & primitiveIn[0]);
            bigIn[1] = BigInteger.valueOf(0xffffffffL & primitiveIn[1]);
            try {
                assertEquals(codec.encode2(bigIn), new BigInteger(Long.toUnsignedString(codec.encode2(primitiveIn))));
            } catch (ArithmeticException ignore) {}
        }
    }

    @Test
    public void testConsistenceEncode2() {
        testConsistenceEncode2(DimensionCodec.TriangleUU.INSTANCE);
        testConsistenceEncode2(DimensionCodec.SquareUU.INSTANCE);
    }

    private void testConsistenceEncode3(DimensionCodec codec) {
        final byte[] in = new byte[3];
        final BigInteger[] inB = new BigInteger[3];
        for (int index = 0; index < NUMBER; index++) {
            in[0] = (byte) fc.fuzzyByte();
            in[1] = (byte) fc.fuzzyByte();
            in[2] = (byte) fc.fuzzyByte();
            inB[0] = BigInteger.valueOf(0xffL & in[0]);
            inB[1] = BigInteger.valueOf(0xffL & in[1]);
            inB[2] = BigInteger.valueOf(0xffL & in[2]);
            try {
                assertEquals(codec.encode3(inB), new BigInteger(Long.toUnsignedString(codec.encode3(in))));
            } catch (ArithmeticException ignore) {}
        }
    }

    @Test
    public void testConsistenceEncode3() {
        testConsistenceEncode3(DimensionCodec.TriangleUU.INSTANCE);
    }
    */
}
