package mujica.algebra.random;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import mujica.reflect.modifier.ReferenceCode;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/12/30.
 */
@CodeHistory(date = "2025/12/30")
@ReferencePage(title = "A PRNG shootout", href = "http://xoshiro.di.unimi.it/")
@ReferenceCode(groupId = "it.unimi.dsi", artifactId = "dsiutils", version = "2.7.4", fullyQualifiedName = "it.unimi.dsi.util.XoRoShiRo128PlusPlusRandom")
@ReferenceCode(groupId = "org.apache.commons", artifactId = "commons-rng-core", version = "1.6", fullyQualifiedName = "org.apache.commons.rng.core.source64.XoRoShiRo128PlusPlus")
@Name(value = "xoroshiro128++", language = "en")
public class XoRoShiRo128PlusPlus implements RandomSource {

    private long s0, s1;

    public XoRoShiRo128PlusPlus(long s0, long s1) {
        super();
        this.s0 = s0;
        this.s1 = s1;
    }

    public XoRoShiRo128PlusPlus(@NotNull RandomSource source) {
        this(source.applyAsLong(Long.SIZE), source.applyAsLong(Long.SIZE));
    }

    @NotNull
    @Override
    public XoRoShiRo128Plus clone() throws CloneNotSupportedException {
        return (XoRoShiRo128Plus) super.clone();
    }

    private long next() {
        final long s0 = this.s0;
        long s1 = this.s1;
        final long result = Long.rotateLeft(s0 + s1, 17) + s0;
        s1 ^= s0;
        this.s0 = Long.rotateLeft(s0, 49) ^ s1 ^ (s1 << 21);
        this.s1 = Long.rotateLeft(s1, 28);
        return result;
    }

    @Override
    public long applyAsLong(int bitCount) {
        return next() >>> (Long.SIZE - bitCount);
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
            0x2bd7a6a6e99c2ddcL,
            0x0992ccaf6a6fca05L
    };

    public void jump() {
        jump(JUMP_COEFFICIENTS);
    }

    private static final long[] LONG_JUMP_COEFFICIENTS = {
            0x360fd5f2cf8d5d99L,
            0x9c6e6877736c46e3L
    };

    public void longJump() {
        jump(LONG_JUMP_COEFFICIENTS);
    }
}
