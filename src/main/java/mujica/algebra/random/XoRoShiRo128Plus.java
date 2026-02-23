package mujica.algebra.random;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import mujica.reflect.modifier.ReferenceCode;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntSupplier;
import java.util.function.LongSupplier;

@CodeHistory(date = "2025/12/15")
@ReferencePage(title = "A PRNG shootout", href = "http://xoshiro.di.unimi.it/")
@ReferenceCode(groupId = "it.unimi.dsi", artifactId = "dsiutils", version = "2.7.4", fullyQualifiedName = "it.unimi.dsi.util.XoRoShiRo128PlusRandom")
@ReferenceCode(groupId = "org.apache.commons", artifactId = "commons-rng-core", version = "1.6", fullyQualifiedName = "org.apache.commons.rng.core.source64.XoRoShiRo128Plus")
@Name(value = "xoroshiro128+", language = "en")
public class XoRoShiRo128Plus implements RandomSource, LongSupplier {

    private long s0, s1;

    public XoRoShiRo128Plus(long s0, long s1) {
        super();
        this.s0 = s0;
        this.s1 = s1;
    }

    public XoRoShiRo128Plus(@NotNull RandomSource source) {
        this(source.applyAsLong(Long.SIZE), source.applyAsLong(Long.SIZE));
    }

    public XoRoShiRo128Plus() {
        this(GlobalRandomSource.INSTANCE);
    }

    @NotNull
    @Override
    public XoRoShiRo128Plus clone() throws CloneNotSupportedException {
        return (XoRoShiRo128Plus) super.clone();
    }

    private void next() {
        s1 ^= s0;
        s0 = Long.rotateLeft(s0, 24) ^ s1 ^ (s1 << 16);
        s1 = Long.rotateLeft(s1, 37);
    }

    @Override
    public long getAsLong() {
        final long result = s0 + s1;
        next();
        return result;
    }

    @Override
    public long applyAsLong(int bitCount) {
        return getAsLong() >>> (Long.SIZE - bitCount);
    }

    @NotNull
    @Override
    public IntSupplier intBind(int bitCount) {
        if (bitCount == Integer.SIZE) {
            return () -> (int) getAsLong();
        }
        if (!(0 < bitCount && bitCount < Integer.SIZE)) {
            throw new IllegalArgumentException();
        }
        final int shift = Long.SIZE - bitCount;
        return () -> (int) (getAsLong() >>> shift);
    }

    @NotNull
    @Override
    public LongSupplier longBind(int bitCount) {
        if (bitCount == Long.SIZE) {
            return this;
        }
        if (!(0 < bitCount && bitCount <= Long.SIZE)) {
            throw new IllegalArgumentException();
        }
        final int shift = Long.SIZE - bitCount;
        return () -> (int) (getAsLong() >>> shift);
    }

    private void jump(long[] jump) {
        long s0 = 0;
        long s1 = 0;
        for (long value : jump) {
            for (int shift = 0; shift < 64; shift++) {
                if ((value & 1L << shift) != 0) {
                    s0 ^= this.s0;
                    s1 ^= this.s1;
                }
                next();
            }
        }
        this.s0 = s0;
        this.s1 = s1;
    }

    private static final long[] JUMP_COEFFICIENTS = {
            0xdf900294d8f554a5L,
            0x170865df4b3201fcL
    };

    public void jump() {
        jump(JUMP_COEFFICIENTS);
    }

    private static final long[] LONG_JUMP_COEFFICIENTS = {
            0xd2a98b26625eee7bL,
            0xdddf9b1090aa7ac1L
    };

    public void longJump() {
        jump(LONG_JUMP_COEFFICIENTS);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof XoRoShiRo128Plus)) {
            return false;
        }
        final XoRoShiRo128Plus that = (XoRoShiRo128Plus) obj;
        return this.s0 == that.s0 && this.s1 == that.s1;
    }
}
