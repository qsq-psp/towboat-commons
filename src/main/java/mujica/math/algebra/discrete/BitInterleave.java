package mujica.math.algebra.discrete;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DataType;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Arrays;

@CodeHistory(date = "2023/10/6", project = "Ultramarine", name = "BitInterleaveUU")
@CodeHistory(date = "2025/2/28")
public class BitInterleave implements DimensionCodec {

    @DataType("s8")
    public static byte unsignedToSigned(@DataType("u8") byte value) {
        if ((value & 1) == 0) {
            return (byte) (value >> 1);
        } else {
            return (byte) (-((value >> 1) + 1));
        }
    }

    @DataType("u8")
    public static byte signedToUnsigned(@DataType("s8") byte value) {
        if ((value & Byte.MIN_VALUE) == 0) {
            return (byte) (value << 1);
        } else {
            return (byte) (((-value) << 1) - 1);
        }
    }

    @DataType("s32")
    public static int unsignedToSigned(@DataType("u32") int value) {
        if ((value & 1) == 0) {
            return value >> 1;
        } else {
            return -((value >> 1) + 1);
        }
    }

    @DataType("u32")
    public static int signedToUnsigned(@DataType("s32") int value) {
        if ((value & Integer.MIN_VALUE) == 0) {
            return value >> 1;
        } else {
            return ((-value) << 1) - 1;
        }
    }

    @DataType("s64")
    public static long unsignedToSigned(@DataType("u64") long value) {
        if ((value & 1L) == 0L) {
            return value >> 1;
        } else {
            return -((value >> 1) + 1L);
        }
    }

    @DataType("u64")
    public static long signedToUnsigned(@DataType("s64") long value) {
        if ((value & Long.MIN_VALUE) == 0L) {
            return value >> 1;
        } else {
            return ((-value) << 1) - 1L;
        }
    }

    @NotNull
    public static BigInteger unsignedToSigned(@NotNull BigInteger value) {
        if (value.testBit(0)) {
            return value.shiftRight(1).add(BigInteger.ONE).negate();
        } else {
            return value.shiftRight(1);
        }
    }

    @NotNull
    public static BigInteger signedToUnsigned(@NotNull BigInteger value) {
        if (value.signum() < 0) {
            return value.negate().shiftLeft(1).subtract(BigInteger.ONE);
        } else {
            return value.shiftLeft(1);
        }
    }

    public static final BitInterleave INSTANCE = new BitInterleave();

    @Override
    public boolean vectorSigned() {
        return false;
    }

    @Override
    public boolean codeSigned() {
        return false;
    }

    @Override
    public long encode2(@NotNull int[] in) {
        final int dimension = Long.SIZE / Integer.SIZE; // 2, as in method name
        long out = 0L;
        for (int inIndex = 0; inIndex < dimension; inIndex++) {
            int outShift = inIndex;
            int inValue = in[inIndex];
            for (int inShift = 0; inShift < Integer.SIZE; inShift++) {
                if ((inValue & (1 << inShift)) != 0) {
                    out |= 1L << outShift;
                }
                outShift += dimension;
            }
        }
        return out;
    }

    @Override
    public void decode2(long in, @NotNull int[] out) {
        final int dimension = Long.SIZE / Integer.SIZE; // 2, as in method name
        for (int outIndex = 0; outIndex < dimension; outIndex++) {
            int inShift = outIndex;
            int value = 0;
            for (int outShift = 0; outShift < Integer.SIZE; outShift++) {
                if ((in & (1L << inShift)) != 0L) {
                    value |= 1 << outShift;
                }
                inShift += dimension;
            }
            out[outIndex] = value;
        }
    }

    @Override
    public int encode4(@NotNull byte[] in) {
        final int dimension = Integer.SIZE / Byte.SIZE; // 4, as in method name
        int out = 0;
        for (int inIndex = 0; inIndex < dimension; inIndex++) {
            int outShift = inIndex;
            int inValue = in[inIndex];
            for (int inShift = 0; inShift < Byte.SIZE; inShift++) {
                if ((inValue & (1 << inShift)) != 0) {
                    out |= 1 << outShift;
                }
                outShift += dimension;
            }
        }
        return out;
    }

    @Override
    public void decode4(int in, @NotNull byte[] out) {
        final int dimension = Integer.SIZE / Byte.SIZE; // 4, as in method name
        for (int outIndex = 0; outIndex < dimension; outIndex++) {
            int inShift = outIndex;
            int value = 0;
            for (int outShift = 0; outShift < Byte.SIZE; outShift++) {
                if ((in & (1 << inShift)) != 0) {
                    value |= 1 << outShift;
                }
                inShift += dimension;
            }
            out[outIndex] = (byte) value;
        }
    }

    @Override
    public long encode8(@NotNull byte[] in) {
        final int dimension = Long.SIZE / Byte.SIZE; // 8, as in method name
        long out = 0L;
        for (int inIndex = 0; inIndex < dimension; inIndex++) {
            int outShift = inIndex;
            int inValue = in[inIndex];
            for (int inShift = 0; inShift < Byte.SIZE; inShift++) {
                if ((inValue & (1 << inShift)) != 0) {
                    out |= 1L << outShift;
                }
                outShift += dimension;
            }
        }
        return out;
    }

    @Override
    public void decode8(long in, @NotNull byte[] out) {
        final int dimension = Long.SIZE / Byte.SIZE; // 8, as in method name
        for (int outIndex = 0; outIndex < dimension; outIndex++) {
            int inShift = outIndex;
            int value = 0;
            for (int outShift = 0; outShift < Byte.SIZE; outShift++) {
                if ((in & (1L << inShift)) != 0L) {
                    value |= 1 << outShift;
                }
                inShift += dimension;
            }
            out[outIndex] = (byte) value;
        }
    }

    @Override
    @NotNull
    public BigInteger encodeN(@NotNull BigInteger[] in) {
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
        return new BigInteger(1, magnitude); // signum = 1 is OK if magnitude is all zero
    }

    @Override
    public void decodeN(@NotNull BigInteger in, @NotNull BigInteger[] out) {
        final int dimension = out.length;
        if (dimension < 2) {
            if (dimension == 1) {
                out[0] = in; // no check for negative values
            }
            return;
        }
        final int bitLength = in.bitLength();
        final int byteLength = (bitLength - 1) / (dimension << 3);
        final byte[] magnitude = new byte[byteLength + 1];
        for (int outIndex = 0; outIndex < dimension; outIndex++) {
            if (outIndex != 0) {
                Arrays.fill(magnitude, (byte) 0);
            }
            int outShift = 0;
            for (int inShift = outIndex; inShift < bitLength; inShift += dimension) {
                if (in.testBit(inShift)) {
                    magnitude[byteLength - (outShift >> 3)] |= 1 << (outShift & 0x7);
                }
                outShift++;
            }
            out[outIndex] = new BigInteger(1, magnitude); // signum = 1 is OK if magnitude is all zero
        }
    }
}
