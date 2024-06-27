package indi.um.util.math;

import indi.um.util.value.BitCount32;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * Created on 2023/10/6.
 */
public abstract class DimensionCodec {

    public static BigInteger valueOfUnsigned(int value) {
        return BigInteger.valueOf(0xffffffffL & value);
    }

    public static final BigInteger LONG_RANGE = BigInteger.ONE.shiftLeft(Long.SIZE);

    public static BigInteger valueOfUnsigned(long value) {
        BigInteger bigInteger = BigInteger.valueOf(value);
        if (bigInteger.signum() < 0) {
            bigInteger = bigInteger.add(LONG_RANGE);
        }
        return bigInteger;
    }

    public abstract boolean vectorSigned();

    public abstract boolean codeSigned();

    public abstract long encode2(int[] in);

    public abstract void decode2(long code, int[] out);

    public abstract BigInteger encode2(BigInteger[] in);

    public abstract void decode2(BigInteger code, BigInteger[] out);

    public abstract int encode3(byte[] in);

    public abstract void decode3(int code, byte[] out);

    public abstract BigInteger encode3(BigInteger[] in);

    public abstract void decode3(BigInteger code, BigInteger[] out);

    public abstract int encode4(byte[] in);

    public abstract void decode4(int code, byte[] out);

    public abstract BigInteger encodeN(BigInteger[] in);

    public abstract void decodeN(BigInteger code, BigInteger[] out);

    public static class BitInterleaveUU extends DimensionCodec {

        public static final BitInterleaveUU INSTANCE = new BitInterleaveUU();

        @Override
        public boolean vectorSigned() {
            return false;
        }

        @Override
        public boolean codeSigned() {
            return false;
        }

        @Override
        public long encode2(int[] in) {
            long out = 0L;
            int outShift = 0;
            for (int inShift = 0; inShift < Integer.SIZE; inShift++) {
                if ((in[0] & (1 << inShift)) != 0) {
                    out |= 1L << outShift;
                }
                outShift += 2;
            }
            outShift = 1;
            for (int inShift = 0; inShift < Integer.SIZE; inShift++) {
                if ((in[1] & (1 << inShift)) != 0) {
                    out |= 1L << outShift;
                }
                outShift += 2;
            }
            return out;
        }

        @Override
        public void decode2(long code, int[] out) {
            int value = 0;
            int inShift = 0;
            for (int outShift = 0; outShift < Integer.SIZE; outShift++) {
                if ((code & (1L << inShift)) != 0) {
                    value |= 1 << outShift;
                }
                inShift += 2;
            }
            out[0] = value;
            value = 0;
            inShift = 1;
            for (int outShift = 0; outShift < Integer.SIZE; outShift++) {
                if ((code & (1L << inShift)) != 0) {
                    value |= 1 << outShift;
                }
                inShift += 2;
            }
            out[1] = value;
        }

        @Override
        public BigInteger encode2(BigInteger[] in) {
            final BigInteger b0 = in[0];
            final BigInteger b1 = in[1];
            if (b0.signum() < 0 || b1.signum() < 0) {
                throw new ArithmeticException();
            }
            final int length0 = b0.bitLength();
            final int length1 = b1.bitLength();
            final byte[] magnitude = new byte[(Math.max(length0, length1) + 3) / 4];
            for (int shift = 0; shift < length0; shift++) {
                if (b0.testBit(shift)) {
                    magnitude[shift >> 2] |= 1 << ((shift << 1) & 0x7);
                }
            }
            for (int shift = 0; shift < length1; shift++) {
                if (b1.testBit(shift)) {
                    magnitude[shift >> 2] |= 1 << (((shift << 1) & 0x7) | 1);
                }
            }
            return new BigInteger(1, magnitude);
        }

        @Override
        public void decode2(BigInteger code, BigInteger[] out) {
            if (code.signum() < 0) {
                throw new ArithmeticException();
            }
            final int bitLength = code.bitLength();
            final int byteLength = (bitLength + 15) >> 4;
            final byte[] magnitude = new byte[byteLength];
            for (int shift = 0; shift < bitLength; shift += 2) {
                if (code.testBit(shift)) {
                    magnitude[shift >> 4] |= 1 << ((shift >> 1) & 0x7);
                }
            }
            out[0] = new BigInteger(1, magnitude);
            Arrays.fill(magnitude, (byte) 0); // clear it and use it twice
            for (int shift = 1; shift < bitLength; shift += 2) {
                if (code.testBit(shift)) {
                    magnitude[shift >> 4] |= 1 << ((shift >> 1) & 0x7);
                }
            }
            out[1] = new BigInteger(1, magnitude);
        }

        @Override
        public int encode3(byte[] in) {
            int out = 0;
            for (int inIndex = 0; inIndex < 3; inIndex++) {
                int outShift = inIndex;
                for (int inShift = 0; inShift < Byte.SIZE; inShift++) {
                    if ((in[inIndex] & (1 << inShift)) != 0) {
                        out |= 1 << outShift;
                    }
                    outShift += 3;
                }
            }
            return out;
        }

        @Override
        public void decode3(int code, byte[] out) {
            for (int outIndex = 0; outIndex < 3; outIndex++) {
                int inShift = outIndex;
                int value = 0;
                for (int outShift = 0; outShift < Byte.SIZE; outShift++) {
                    if ((code & (1 << inShift)) != 0) {
                        value |= 1 << outShift;
                    }
                    inShift += 3;
                }
                out[outIndex] = (byte) value;
            }
        }

        @Override
        public BigInteger encode3(BigInteger[] in) {
            final BigInteger b0 = in[0];
            final BigInteger b1 = in[1];
            final BigInteger b2 = in[2];
            if (b0.signum() < 0 || b1.signum() < 0 || b2.signum() < 0) {
                throw new ArithmeticException();
            }
            final int length0 = b0.bitLength();
            final int length1 = b1.bitLength();
            final int length2 = b2.bitLength();
            final byte[] magnitude = new byte[(Math.max(length0, Math.max(length1, length2)) * 3 + 7) / 8];
            int outShift = 0;
            for (int inShift = 0; inShift < length0; inShift++) {
                if (b0.testBit(inShift)) {
                    magnitude[outShift >> 3] |= 1 << (outShift & 0x7);
                }
                outShift += 3;
            }
            outShift = 1;
            for (int inShift = 0; inShift < length0; inShift++) {
                if (b1.testBit(inShift)) {
                    magnitude[outShift >> 3] |= 1 << (outShift & 0x7);
                }
                outShift += 3;
            }
            outShift = 2;
            for (int inShift = 0; inShift < length0; inShift++) {
                if (b2.testBit(inShift)) {
                    magnitude[outShift >> 3] |= 1 << (outShift & 0x7);
                }
                outShift += 3;
            }
            return new BigInteger(1, magnitude);
        }

        @Override
        public void decode3(BigInteger code, BigInteger[] out) {
            if (code.signum() < 0) {
                throw new ArithmeticException();
            }
            final int bitLength = code.bitLength();
            final int byteLength = (bitLength + 23) / 24;
            final byte[] magnitude = new byte[byteLength];
            int inShift = 0;
            for (int outShift = 0; outShift < bitLength; outShift += 3) {
                if (code.testBit(outShift)) {
                    magnitude[inShift >> 3] |= 1 << (inShift & 0x7);
                }
                inShift++;
            }
            out[0] = new BigInteger(1, magnitude);
            Arrays.fill(magnitude, (byte) 0);
            inShift = 0;
            for (int outShift = 1; outShift < bitLength; outShift += 3) {
                if (code.testBit(outShift)) {
                    magnitude[inShift >> 3] |= 1 << (inShift & 0x7);
                }
                inShift++;
            }
            out[1] = new BigInteger(1, magnitude);
            Arrays.fill(magnitude, (byte) 0);
            inShift = 0;
            for (int outShift = 2; outShift < bitLength; outShift += 3) {
                if (code.testBit(outShift)) {
                    magnitude[inShift >> 3] |= 1 << (inShift & 0x7);
                }
                inShift++;
            }
            out[2] = new BigInteger(1, magnitude);
        }

        @Override
        public int encode4(byte[] in) {
            int out = 0;
            for (int inIndex = 0; inIndex < 4; inIndex++) {
                int outShift = inIndex;
                for (int inShift = 0; inShift < Byte.SIZE; inShift++) {
                    if ((in[inIndex] & (1 << inShift)) != 0) {
                        out |= 1 << outShift;
                    }
                    outShift += 4;
                }
            }
            return out;
        }

        @Override
        public void decode4(int code, byte[] out) {
            for (int outIndex = 0; outIndex < 4; outIndex++) {
                int inShift = outIndex;
                int value = 0;
                for (int outShift = 0; outShift < Byte.SIZE; outShift++) {
                    if ((code & (1 << inShift)) != 0) {
                        value |= 1 << outShift;
                    }
                    inShift += 4;
                }
                out[outIndex] = (byte) value;
            }
        }

        @Override
        public BigInteger encodeN(BigInteger[] in) {
            final int dimension = in.length;
            if (dimension < 2) {
                if (dimension == 1) {
                    return in[0]; // no check for negative values
                } else {
                    return BigInteger.ZERO;
                }
            }
            int bitLength = 0;
            for (int i = 0; i < in.length; i++) {
                BigInteger v = in[i];
                if (v.signum() < 0) {
                    throw new ArithmeticException();
                }
                bitLength = Math.max(bitLength, v.bitLength() * dimension + i);
            }
            final int byteLength = (bitLength - 1) / Byte.SIZE;
            final byte[] magnitude = new byte[byteLength + 1];
            for (int inIndex = 0; inIndex < in.length; inIndex++) {
                BigInteger v = in[inIndex];
                bitLength = v.bitLength();
                int outShift = inIndex;
                for (int inShift = 0; inShift < bitLength; inShift++) {
                    if (v.testBit(inShift)) {
                        magnitude[byteLength - (outShift >> 3)] |= 1 << (outShift & 0x7);
                    }
                    outShift += dimension;
                }
            }
            return new BigInteger(1, magnitude); // signum = 1 is okay if magnitude is all zero
        }

        @Override
        public void decodeN(BigInteger code, BigInteger[] out) {
            final int dimension = out.length;
            if (dimension < 2) {
                if (dimension == 1) {
                    out[0] = code; // no check for negative values
                }
                return;
            }
            final int bitLength = code.bitLength();
            final int byteLength = (bitLength - 1) / (dimension << 3);
            final byte[] magnitude = new byte[byteLength + 1];
            for (int outIndex = 0; outIndex < dimension; outIndex++) {
                if (outIndex != 0) {
                    Arrays.fill(magnitude, (byte) 0);
                }
                int outShift = 0;
                for (int inShift = outIndex; inShift < bitLength; inShift += dimension) {
                    if (code.testBit(inShift)) {
                        magnitude[byteLength - (outShift >> 3)] |= 1 << (outShift & 0x7);
                    }
                    outShift++;
                }
                out[outIndex] = new BigInteger(1, magnitude); // signum = 1 is okay if magnitude is all zero
            }
        }
    }

    public static class TriangleUU extends DimensionCodec {

        public static final TriangleUU INSTANCE = new TriangleUU();

        @Override
        public boolean vectorSigned() {
            return false;
        }

        @Override
        public boolean codeSigned() {
            return false;
        }

        /**
         * Prevent overflow
         */
        private long f2(long s) {
            if ((s & 1L) == 0L) {
                return (s >> 1) * (s + 1);
            } else {
                return s * ((s + 1) >> 1);
            }
        }

        @Override
        public long encode2(int[] in) {
            final long x = 0xffffffffL & in[0];
            final long y = 0xffffffffL & in[1];
            final long s = x + y;
            return f2(s) + y;
        }

        @Override
        public void decode2(long code, int[] out) {
            if (code <= 2L) {
                if (code < 0L) {
                    throw new ArithmeticException();
                } else if (code == 0L) {
                    out[0] = 0;
                    out[1] = 0;
                } else if (code == 1L) {
                    out[0] = 1;
                    out[1] = 0;
                } else {
                    out[0] = 0;
                    out[1] = 1;
                }
            }
            long low = 0;
            long high = Math.min(code, 6074001000L);
            while (low < high - 1L) {
                long mid = (low + high) >> 1;
                long f = f2(mid);
                if (f <= code) {
                    low = mid;
                    if (f == code) {
                        break;
                    }
                } else {
                    high = mid;
                }
            }
            final long y = code - f2(low);
            final long x = low - y;
            if (0x0 <= x && x < 0x100000000L && 0x0 <= y && y < 0x100000000L) {
                out[0] = (int) x;
                out[1] = (int) y;
            } else {
                throw new ArithmeticException();
            }
        }

        @Override
        public BigInteger encode2(BigInteger[] in) {
            final BigInteger x = in[0];
            final BigInteger y = in[1];
            if (x.signum() < 0 || y.signum() < 0) {
                throw new ArithmeticException();
            }
            final BigInteger s = x.add(y);
            return s.multiply(s.add(BigInteger.ONE)).divide(BigInteger.TWO).add(y);
        }

        private static final BigInteger I_8 = BigInteger.valueOf(8);

        @Override
        public void decode2(BigInteger code, BigInteger[] out) {
            final BigInteger s = code.multiply(I_8).add(BigInteger.ONE).sqrt().subtract(BigInteger.ONE).divide(BigInteger.TWO);
            code = code.subtract(s.multiply(s.add(BigInteger.ONE)).divide(BigInteger.TWO));
            out[0] = s.subtract(code);
            out[1] = code;
        }

        @Override
        public int encode3(byte[] in) {
            final int x = 0xff & in[0];
            final int y = 0xff & in[1];
            final int z = 0xff & in[2];
            final int s = y + z;
            final int v = x + s;
            return v * (v + 1) * (v + 2) / 6 + s * (s + 1) / 2 + z; // no overflow
        }

        @Override
        public void decode3(int code, byte[] out) {

        }

        private static final BigInteger I_6 = BigInteger.valueOf(6);

        @Override
        public BigInteger encode3(BigInteger[] in) {
            final BigInteger x = in[0];
            final BigInteger y = in[1];
            final BigInteger z = in[2];
            final BigInteger s = y.add(z);
            final BigInteger v = x.add(s);
            return v.multiply(v.add(BigInteger.ONE)).multiply(v.add(BigInteger.TWO)).divide(I_6).add(s.multiply(s.add(BigInteger.ONE)).divide(BigInteger.TWO)).add(z);
        }

        @Override
        public void decode3(BigInteger code, BigInteger[] out) {

        }

        @Override
        public int encode4(byte[] in) {
            return 0;
        }

        @Override
        public void decode4(int code, byte[] out) {

        }

        @SuppressWarnings("SuspiciousNameCombination")
        private BigInteger fn(BigInteger x, int dimension) {
            BigInteger y = x;
            for (int index = 1; index < dimension; index++) {
                y = y.multiply(x.add(BigInteger.valueOf(index)));
            }
            for (int index = 2; index <= dimension; index++) {
                y = y.divide(BigInteger.valueOf(index));
            }
            return y;
        }

        @Override
        public BigInteger encodeN(BigInteger[] in) {
            BigInteger sum = BigInteger.ZERO;
            int dimension;
            for (dimension = 0; dimension < in.length; dimension++) {
                sum = sum.add(in[dimension]);
            }
            BigInteger code = BigInteger.ZERO;
            for (; dimension > 0; dimension--) {
                code = code.add(fn(sum, dimension));
                if (dimension > 2) {
                    sum = sum.subtract(in[dimension - 1]);
                } else {
                    sum = in[0];
                }
            }
            return code;
        }

        private BigInteger gn(BigInteger y, int dimension) {
            if (dimension < 2) {
                return y;
            }
            BigInteger low = BigInteger.ZERO; // inclusive
            BigInteger high = y.add(BigInteger.ONE); // exclusive
            while (high.compareTo(low.add(BigInteger.ONE)) > 0) {
                BigInteger mid = low.add(high).shiftRight(1);
                BigInteger midValue = fn(mid, dimension);
                int compare = y.compareTo(midValue);
                if (compare >= 0) {
                    low = mid;
                    if (compare == 0) {
                        break;
                    }
                } else {
                    high = mid;
                }
            }
            return low;
        }

        @Override
        public void decodeN(BigInteger code, BigInteger[] out) {
            BigInteger x0 = null;
            for (int dimension = out.length; dimension > 0; dimension--) {
                BigInteger x1 = gn(code, dimension);
                if (x0 != null) {
                    out[dimension] = x0.subtract(x1);
                }
                x0 = x1;
                code = code.subtract(fn(x1, dimension));
            }
            if (x0 != null) {
                out[0] = x0;
            }
        }
    }

    public static class SquareUU extends DimensionCodec {

        public static final SquareUU INSTANCE = new SquareUU();

        @Override
        public boolean vectorSigned() {
            return false;
        }

        @Override
        public boolean codeSigned() {
            return false;
        }

        @Override
        public long encode2(int[] in) {
            final long x = 0xffffffffL & in[0];
            final long y = 0xffffffffL & in[1];
            if (x > y) {
                return x * x + y;
            } else {
                return y * (y + 1L) + x;
            }
        }

        @Override
        public void decode2(long code, int[] out) {

        }

        @Override
        public BigInteger encode2(BigInteger[] in) {
            final BigInteger x = in[0];
            final BigInteger y = in[1];
            if (x.compareTo(y) > 0) {
                return x.multiply(x).add(y);
            } else {
                return y.multiply(y.add(BigInteger.ONE)).add(x);
            }
        }

        @Override
        public void decode2(BigInteger code, BigInteger[] out) {
            final BigInteger[] m = code.sqrtAndRemainder();
            if (m[0].compareTo(m[1]) > 0) {
                out[0] = m[0];
                out[1] = m[1];
            } else {
                out[0] = m[1].subtract(m[0]);
                out[1] = m[0];
            }
        }

        @Override
        public int encode3(byte[] in) {
            return 0;
        }

        @Override
        public void decode3(int code, byte[] out) {

        }

        @Override
        public BigInteger encode3(BigInteger[] in) {
            return null;
        }

        @Override
        public void decode3(BigInteger code, BigInteger[] out) {

        }

        @Override
        public int encode4(byte[] in) {
            return 0;
        }

        @Override
        public void decode4(int code, byte[] out) {

        }

        @Override
        public BigInteger encodeN(BigInteger[] in) {
            final int dimension = in.length;
            if (dimension < 2) {
                if (dimension == 1) {
                    return in[0]; // no check for negative values
                } else {
                    return BigInteger.ZERO;
                }
            }
            if (dimension >= Integer.SIZE) {
                throw new ArithmeticException();
            }
            BigInteger maxValue = in[0];
            int maxMask = 0x1;
            for (int index = 1; index < dimension; index++) {
                int compare = in[index].compareTo(maxValue);
                if (compare >= 0) {
                    maxValue = in[index];
                    if (compare > 0) {
                        maxMask = 0;
                    }
                    maxMask |= 1 << index;
                }
            }
            final int maxCount = Integer.bitCount(maxMask);
            if (maxCount == dimension) {
                return maxValue.add(BigInteger.ONE).pow(dimension).subtract(BigInteger.ONE);
            }
            BigInteger code = maxValue.pow(dimension);
            for (int index = 1; index < maxCount; index++) {
                code = code.add(maxValue.pow(dimension - index).multiply(DiscreteMath.bigCombination(dimension, index)));
            }
            code = code.add(maxValue.pow(dimension - maxCount).multiply(BigInteger.valueOf((new BitCount32(dimension, maxCount)).indexOf(maxMask))));
            BigInteger vector = BigInteger.ZERO;
            for (BigInteger v : in) {
                if (v.compareTo(maxValue) < 0) {
                    vector = vector.multiply(maxValue).add(v);
                }
            }
            return code.add(vector);
        }

        @Override
        public void decodeN(BigInteger code, BigInteger[] out) {

        }
    }
}
