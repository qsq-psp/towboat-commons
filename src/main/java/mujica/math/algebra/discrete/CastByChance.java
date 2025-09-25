package mujica.math.algebra.discrete;

import mujica.math.algebra.random.RandomContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2023/12/13", project = "Ultramarine", name = "IntegralCast.Stochastic")
@CodeHistory(date = "2025/3/3")
public class CastByChance implements IntegralCast {

    @NotNull
    private final RandomContext rc;

    public CastByChance(@NotNull RandomContext rc) {
        super();
        this.rc = rc;
    }

    @Override
    public int mean(int x, int y) {
        final int sum = x + y;
        int carry = sum & 1;
        if (carry != 0 && rc.nextBoolean()) {
            carry = 0;
        }
        if (((x ^ sum) & (y ^ sum)) < 0) {
            if (sum < 0) {
                return (sum >>> 1) + carry;
            } else {
                return (Integer.MIN_VALUE | (sum >>> 1)) + carry;
            }
        }
        return (sum >> 1) + carry;
    }

    @Override
    public long mean(long x, long y) {
        final long sum = x + y;
        long carry = sum & 1L;
        if (carry != 0L && rc.nextBoolean()) {
            carry = 0L;
        }
        if (((x ^ sum) & (y ^ sum)) < 0L) {
            if (sum < 0L) {
                return (sum >>> 1) + carry;
            } else {
                return (Integer.MIN_VALUE | (sum >>> 1)) + carry;
            }
        }
        return (sum >> 1) + carry;
    }

    @Override
    public int d2i(double v) {
        double floor = Math.floor(v);
        int integral = (int) floor;
        if (v < floor + rc.nextDouble()) {
            integral++;
        }
        return integral;
    }

    @Override
    public long d2l(double v) {
        double floor = Math.floor(v);
        long integral = (long) floor;
        if (v < floor + rc.nextDouble()) {
            integral++;
        }
        return integral;
    }
}
