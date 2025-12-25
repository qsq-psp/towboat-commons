package mujica.math.algebra.random;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import mujica.reflect.modifier.ReferenceCode;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/12/15.
 */
@CodeHistory(date = "2025/12/15")
@ReferenceCode(groupId = "it.unimi.dsi", artifactId = "dsiutils", version = "2.7.4", fullyQualifiedName = "it.unimi.dsi.util.XoRoShiRo128PlusRandom")
@Name(value = "xoroshiro128+", language = "en")
public class XoRoShiRo128Plus implements RandomSource {

    private long s0, s1;

    public XoRoShiRo128Plus(long s0, long s1) {
        super();
        this.s0 = s0;
        this.s1 = s1;
    }

    public XoRoShiRo128Plus(@NotNull RandomSource source) {
        this(source.next(Long.SIZE), source.next(Long.SIZE));
    }

    @NotNull
    @Override
    public XoRoShiRo128Plus clone() throws CloneNotSupportedException {
        return (XoRoShiRo128Plus) super.clone();
    }

    @Override
    public long next(int bitCount) {
        final long result = s0 + s1;
        s1 ^= s0;
        s0 = Long.rotateLeft(s0, 24) ^ s1 ^ (s1 << 16);
        s1 = Long.rotateLeft(s1, 37);
        return result >>> (Long.SIZE - bitCount);
    }
}
