package mujica.math.algebra.random;

import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/3/11.
 */
public class FreeFloatContext extends RandomContext {

    public static final int FLAG_NORMAL         = 0x01;
    public static final int FLAG_SUBNORMAL      = 0x02;
    public static final int FLAG_INFINITY       = 0x03;
    public static final int FLAG_NAN            = 0x04;

    final int flags;

    public FreeFloatContext(@NotNull RandomSource source, int flags) {
        super(source);
        flags &= FLAG_NORMAL | FLAG_SUBNORMAL | FLAG_INFINITY | FLAG_NAN;
        if (flags == 0) {
            throw new IllegalArgumentException();
        }
        this.flags = flags;
    }

    public FreeFloatContext(@NotNull RandomSource source) {
        this(source, -1);
    }

    public FreeFloatContext(int flags) {
        this(new LocalRandomSource(), flags);
    }

    public FreeFloatContext() {
        this(new LocalRandomSource());
    }

    @Override
    public float nextFloat() {
        return super.nextFloat();
    }

    @Override
    public double nextDouble() {
        return super.nextDouble();
    }
}
