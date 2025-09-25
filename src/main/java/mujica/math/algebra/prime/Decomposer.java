package mujica.math.algebra.prime;

import mujica.ds.of_int.PublicIntSlot;
import mujica.math.algebra.discrete.IntegralMath;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2025/3/19.
 */
public class Decomposer {

    public static final Decomposer INSTANCE = new Decomposer();

    public void factorize(int value, @NotNull IntFactorConsumer consumer) {
        if (value < 2) {
            return;
        }
        {
            int times2 = Integer.numberOfTrailingZeros(value);
            if (times2 != 0) {
                consumer.accept(2, times2);
                value >>= times2;
                if (value == 1) {
                    return;
                }
            }
        }
        value = factorize(value, 3, consumer);
        for (int num = 6; value != 1; num += 6) {
            value = factorize(value, num - 1, consumer);
            value = factorize(value, num + 1, consumer);
        }
    }

    public int factorize(int value, int factor, @NotNull IntFactorConsumer consumer) {
        int times = 0;
        while (value % factor == 0) {
            times++;
            value /= factor;
        }
        if (times != 0) {
            consumer.accept(factor, times);
        }
        return value;
    }

    public int combination(int n, int m) {
        if (!(0 <= m && m <= n)) {
            throw new ArithmeticException("Bad combination arguments");
        }
        final IntegralMath math = IntegralMath.INSTANCE;
        m = Math.min(m, n - m); // C(n, m) = C(n, n - m)
        switch (m) {
            case 0:
                return 1;
            case 1:
                return n;
            case 2:
                return math.triangle(n - 1);
        }
        if (n > 16) {
            HashMap<Integer, PublicIntSlot> wm = new HashMap<>();
            for (int i = 0; i < m; i++) {
                factorize(n - i, ((factor, times) -> PublicIntSlot.add(wm, factor, times))); // multiply
                factorize(i + 1, ((factor, times) -> PublicIntSlot.add(wm, factor, -times))); // divide
            }
            int w = 1;
            for (Map.Entry<Integer, PublicIntSlot> entry : wm.entrySet()) {
                w = math.multiply(w, math.power(entry.getKey(), entry.getValue().value)); // negative exponents detected and throw; no assert
            }
            return w;
        } else {
            int w = n;
            for (int i = 1; i < m; i++) {
                w *= n - i;
            }
            for (int i = 2; i <= m; i++) {
                assert w % i == 0;
                w /= i;
            }
            return w;
        }
    }

    public long combination(long n, long m) {
        if (!(0L <= m && m <= n)) {
            throw new ArithmeticException("Bad combination arguments");
        }
        final IntegralMath math = IntegralMath.INSTANCE;
        m = Math.min(m, n - m); // C(n, m) = C(n, n - m)
        if (m <= 2L) { // long value can not use switch
            if (m == 0L) {
                return 1L;
            } else if (m == 1L) {
                return n;
            } else {
                return math.triangle(n - 1L);
            }
        }
        if (n > 28L) {
            if (n > 3810779L) {
                throw new ArithmeticException();
            }
            int in = (int) n;
            int im = (int) m;
            HashMap<Integer, PublicIntSlot> wm = new HashMap<>();
            for (int i = 0; i < im; i++) {
                factorize(in - i, ((factor, times) -> PublicIntSlot.add(wm, factor, times))); // multiply
                factorize(i + 1, ((factor, times) -> PublicIntSlot.add(wm, factor, -times))); // divide
            }
            long w = 1L;
            for (Map.Entry<Integer, PublicIntSlot> entry : wm.entrySet()) {
                w = math.multiply(w, math.power(entry.getKey(), entry.getValue().value)); // negative exponents detected and throw; no assert
            }
            return w;
        } else {
            long w = n;
            for (long i = 1; i < m; i++) {
                w *= n - i;
            }
            for (long i = 2; i <= m; i++) {
                assert w % i == 0;
                w /= i;
            }
            return w;
        }
    }
}
