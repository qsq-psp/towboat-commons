package mujica.math.algebra.random;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/3/11")
public class FreeFloatingPointContext extends RandomContext {

    public static final int FP_NORMAL         = 0x01;
    public static final int FP_SUBNORMAL      = 0x02;
    public static final int FP_ZERO           = 0x04;
    public static final int FP_INFINITY       = 0x08;
    public static final int FP_NAN            = 0x10;

    final int flags;

    public FreeFloatingPointContext(@NotNull RandomSource source, int flags) {
        super(source);
        flags &= FP_NORMAL | FP_SUBNORMAL | FP_ZERO | FP_INFINITY | FP_NAN;
        if (flags == 0) {
            throw new IllegalArgumentException(); // at least one type to generate
        }
        this.flags = flags;
    }

    public FreeFloatingPointContext(@NotNull RandomSource source) {
        this(source, -1); // all types can be generated
    }

    public FreeFloatingPointContext(int flags) {
        this(new LocalRandomSource(), flags);
    }

    public FreeFloatingPointContext() {
        this(new LocalRandomSource());
    }

    private long nextBlock(int bitCount) {
        if (bitCount >= Integer.SIZE) {
            int lowBitCount = bitCount >> 1;
            int highBitCount = bitCount - lowBitCount;
            long high = source.next(highBitCount);
            high &= (1L << highBitCount) -1L;
            long low = source.next(lowBitCount);
            low &= (1L << lowBitCount) -1L;
            return (high << lowBitCount) | low;
        } else {
            return source.next(bitCount);
        }
    }
    
    private long nextBit(int shiftDistance) {
        return ((long) source.next(1)) << shiftDistance;
    }

    protected long nextUnsignedIEEE754(int exponentBits, int mantissaBits, int flags) {
        final long maxExponent = (1L << exponentBits) - 1L;
        switch (flags) {
            case FP_NORMAL:
                while (true) {
                    long exponent = nextBlock(exponentBits);
                    if (exponent == 0L || exponent == maxExponent) {
                        continue;
                    }
                    return (exponent << mantissaBits) | nextBlock(mantissaBits);
                }
            case FP_SUBNORMAL:
                while (true) {
                    long mantissa = nextBlock(mantissaBits);
                    if (mantissa == 0L) {
                        continue;
                    }
                    return mantissa;
                }
            case FP_NORMAL | FP_SUBNORMAL:
                while (true) {
                    long exponent = nextBlock(exponentBits);
                    long mantissa = nextBlock(mantissaBits);
                    if (exponent == 0L && mantissa == 0L || exponent == maxExponent) {
                        continue;
                    }
                    return (exponent << mantissaBits) | mantissa;
                }
            case FP_ZERO:
                return 0L;
            case FP_NORMAL | FP_ZERO:
                while (true) {
                    long exponent = nextBlock(exponentBits);
                    long mantissa = nextBlock(mantissaBits);
                    if (exponent == 0L && mantissa != 0L || exponent == maxExponent) {
                        continue;
                    }
                    return (exponent << mantissaBits) | mantissa;
                }
            case FP_SUBNORMAL | FP_ZERO:
                return nextBlock(mantissaBits);
            case FP_NORMAL | FP_SUBNORMAL | FP_ZERO:
                while (true) {
                    long exponent = nextBlock(exponentBits);
                    if (exponent == maxExponent) {
                        continue;
                    }
                    return (exponent << mantissaBits) | nextBlock(mantissaBits);
                }
            case FP_INFINITY:
                return maxExponent << mantissaBits;
            case FP_NORMAL | FP_INFINITY:
                while (true) {
                    long exponent = nextBlock(exponentBits);
                    long mantissa = nextBlock(mantissaBits);
                    if (exponent == 0L || exponent == maxExponent && mantissa != 0L) {
                        continue;
                    }
                    return (exponent << mantissaBits) | mantissa;
                }
            case FP_SUBNORMAL | FP_INFINITY: {
                long mantissa = nextBlock(mantissaBits);
                if (mantissa == 0L) {
                    return maxExponent << mantissaBits;
                } else {
                    return mantissa;
                }
            }
            case FP_NORMAL | FP_SUBNORMAL | FP_INFINITY:
                while (true) {
                    long exponent = nextBlock(exponentBits);
                    long mantissa = nextBlock(mantissaBits);
                    if (exponent == 0L && mantissa == 0L || exponent == maxExponent && mantissa != 0L) {
                        continue;
                    }
                    return (exponent << mantissaBits) | mantissa;
                }
            case FP_ZERO | FP_INFINITY:
                if (nextBoolean()) {
                    return 0L;
                } else {
                    return maxExponent << mantissaBits;
                }
            case FP_NORMAL | FP_ZERO | FP_INFINITY:
                while (true) {
                    long exponent = nextBlock(exponentBits);
                    long mantissa = nextBlock(mantissaBits);
                    if (exponent == 0L && mantissa != 0L || exponent == maxExponent && mantissa != 0L) {
                        continue;
                    }
                    return (exponent << mantissaBits) | mantissa;
                }
            case FP_SUBNORMAL | FP_ZERO | FP_INFINITY:
                if (nextBlock(mantissaBits) == 0L) {
                    return (maxExponent << mantissaBits);
                } else {
                    return nextBlock(mantissaBits);
                }
            case FP_NORMAL | FP_SUBNORMAL | FP_ZERO | FP_INFINITY:
                while (true) {
                    long exponent = nextBlock(exponentBits);
                    long mantissa = nextBlock(mantissaBits);
                    if (exponent == maxExponent && mantissa != 0L) {
                        continue;
                    }
                    return (exponent << mantissaBits) | mantissa;
                }
            case FP_NAN:
                while (true) {
                    long mantissa = nextBlock(mantissaBits);
                    if (mantissa == 0L) {
                        continue;
                    }
                    return (maxExponent << mantissaBits) | mantissa;
                }
            case FP_NORMAL | FP_NAN:
                while (true) {
                    long exponent = nextBlock(exponentBits);
                    long mantissa = nextBlock(mantissaBits);
                    if (exponent == 0L || exponent == maxExponent && mantissa == 0L) {
                        continue;
                    }
                    return (exponent << mantissaBits) | mantissa;
                }
            case FP_SUBNORMAL | FP_NAN:
                while (true) {
                    long mantissa = nextBlock(mantissaBits);
                    if (mantissa == 0L) {
                        continue;
                    }
                    return ((nextBit(exponentBits) - 1L) << mantissaBits) | mantissa; // wrong
                }
            case FP_NORMAL | FP_SUBNORMAL | FP_NAN:
                while (true) {
                    long exponent = nextBlock(exponentBits);
                    long mantissa = nextBlock(mantissaBits);
                    if (exponent == 0L && mantissa == 0L || exponent == maxExponent && mantissa == 0L) {
                        continue;
                    }
                    return (exponent << mantissaBits) | mantissa;
                }
            case FP_ZERO | FP_NAN: {
                long mantissa = nextBlock(mantissaBits);
                if (mantissa == 0L) {
                    return 0L;
                } else {
                    return (maxExponent << mantissaBits) | mantissa;
                }
            }
            case FP_NORMAL | FP_ZERO | FP_NAN:
                while (true) {
                    long exponent = nextBlock(exponentBits);
                    long mantissa = nextBlock(mantissaBits);
                    if (exponent == 0L && mantissa != 0L || exponent == maxExponent && mantissa == 0L) {
                        continue;
                    }
                    return (exponent << mantissaBits) | mantissa;
                }
            case FP_SUBNORMAL | FP_ZERO | FP_NAN:
                if (nextBlock(exponentBits) == 0L) { // todo
                    while (true) {
                        long mantissa = nextBlock(mantissaBits);
                        if (mantissa == 0L) {
                            continue;
                        }
                        return (maxExponent << mantissaBits) | mantissa;
                    }
                } else {
                    return nextBlock(mantissaBits);
                }
            case FP_NORMAL | FP_SUBNORMAL | FP_ZERO | FP_NAN:
                while (true) {
                    long exponent = nextBlock(exponentBits);
                    long mantissa = nextBlock(mantissaBits);
                    if (exponent == maxExponent && mantissa == 0L) {
                        continue;
                    }
                    return (exponent << mantissaBits) | mantissa;
                }
            case FP_INFINITY | FP_NAN:
                return (maxExponent << mantissaBits) | nextBlock(mantissaBits);
            case FP_NORMAL | FP_INFINITY | FP_NAN:
                while (true) {
                    long exponent = nextBlock(exponentBits);
                    if (exponent == 0L) {
                        continue;
                    }
                    return (exponent << mantissaBits) | nextBlock(mantissaBits);
                }
            case FP_SUBNORMAL | FP_INFINITY | FP_NAN:
                if (nextBlock(1) == 0L) {
                    while (true) {
                        long mantissa = nextBlock(mantissaBits);
                        if (mantissa == 0L) {
                            continue;
                        }
                        return mantissa;
                    }
                } else {
                    return (maxExponent << mantissaBits) | nextBlock(mantissaBits);
                }
            case FP_NORMAL | FP_SUBNORMAL | FP_INFINITY | FP_NAN:
                while (true) {
                    long exponentAndMantissa = nextBlock(exponentBits + mantissaBits);
                    if (exponentAndMantissa == 0L) {
                        continue;
                    }
                    return exponentAndMantissa;
                }
            case FP_ZERO | FP_INFINITY | FP_NAN:
                if (nextBlock(mantissaBits) == 0L) {
                    return 0L;
                } else {
                    return (maxExponent << mantissaBits) | nextBlock(mantissaBits);
                }
            case FP_NORMAL | FP_ZERO | FP_INFINITY | FP_NAN:
                while (true) {
                    long exponent = nextBlock(exponentBits);
                    long mantissa = nextBlock(mantissaBits);
                    if (exponent == 0L && mantissa != 0L) {
                        continue;
                    }
                    return (exponent << mantissaBits) | mantissa;
                }
            case FP_SUBNORMAL | FP_ZERO | FP_INFINITY | FP_NAN:
                return ((nextBit(exponentBits) - 1L) << mantissaBits) | nextBlock(mantissaBits); // wrong
            case FP_NORMAL | FP_SUBNORMAL | FP_ZERO | FP_INFINITY | FP_NAN:
                return nextBlock(mantissaBits + exponentBits);
            default:
                throw new IllegalStateException();
        }
    }

    protected long nextIEEE754(int exponentBits, int mantissaBits, int flags) {
        long value = nextUnsignedIEEE754(exponentBits, mantissaBits, flags);
        if (nextBoolean()) {
            value |= 1L << (exponentBits + mantissaBits);
        }
        return value;
    }

    protected long nextFloatBits() {
        return nextIEEE754(8, 23, flags);
    }

    @Override
    public float nextFloat() {
        return Float.intBitsToFloat((int) nextFloatBits());
    }

    protected long nextDoubleBits() {
        return nextIEEE754(11, 52, flags);
    }

    @Override
    public double nextDouble() {
        return Double.longBitsToDouble(nextDoubleBits());
    }
}
