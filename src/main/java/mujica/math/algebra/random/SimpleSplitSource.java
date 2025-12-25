package mujica.math.algebra.random;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferenceCode;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/12/14")
@ReferenceCode(groupId = "oracle-jdk", artifactId = "java.base", fullyQualifiedName = "java.util.SplittableRandom")
public class SimpleSplitSource implements RandomSource {

    private static final long GOLDEN_GAMMA = 0x9e3779b97f4a7c15L;

    /**
     * Computes Stafford variant 13 of 64bit mix function.
     */
    private static long mix64(long z) {
        z = (z ^ (z >>> 30)) * 0xbf58476d1ce4e5b9L;
        z = (z ^ (z >>> 27)) * 0x94d049bb133111ebL;
        return z ^ (z >>> 31);
    }

    /**
     * Returns the gamma value to use for a new split instance.
     */
    private static long mixGamma(long z) {
        z = (z ^ (z >>> 33)) * 0xff51afd7ed558ccdL; // MurmurHash3 mix constants
        z = (z ^ (z >>> 33)) * 0xc4ceb9fe1a85ec53L;
        z = (z ^ (z >>> 33)) | 1L; // force to be odd
        final int n = Long.bitCount(z ^ (z >>> 1)); // ensure enough transitions
        return (n < 24) ? z ^ 0xaaaaaaaaaaaaaaaaL : z;
    }

    private final long gamma; // gamma

    private long seed; // seed

    private SimpleSplitSource(long gamma, long seed) {
        super();
        this.gamma = gamma;
        this.seed = seed;
    }

    public SimpleSplitSource(@NotNull SimpleSplitSource that) {
        this(that.gamma, that.seed); // make a copy, not a split
    }

    public SimpleSplitSource(long seed) {
        this(GOLDEN_GAMMA, seed);
    }

    @NotNull
    @Override
    public SimpleSplitSource clone() throws CloneNotSupportedException {
        return (SimpleSplitSource) super.clone();
    }

    @Override
    public long next(int bitCount) {
        seed += gamma;
        return mix64(seed) >>> (Long.SIZE - bitCount);
    }

    @NotNull
    public SimpleSplitSource split() {
        seed += gamma;
        long newSeed = mix64(seed);
        seed += gamma;
        long newGamma = mixGamma(seed);
        return new SimpleSplitSource(newGamma, newSeed);
    }
}
