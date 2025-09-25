package mujica.math.algebra.discrete;

/**
 * Created in Ultramarine on 2023/12/13.
 * Recreated on 2025/3/3.
 */
public interface IntegralCast {

    default int mean(int x, int y) {
        return d2i(0.5 * (((double) x) + y));
    }

    default long mean(long x, long y) {
        return d2l(0.5 * (((double) x) + y));
    }

    default int divide(int a, int b) {
        return d2i(((double) a) / b);
    }

    default long divide(long a, long b) {
        return d2l(((double) a) / b);
    }

    default int f2i(float v) {
        return d2i(v);
    }

    default long f2l(float v) {
        return d2l(v);
    }

    int d2i(double v);

    long d2l(double v);
}
