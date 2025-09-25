package mujica.math.algebra.prime;

/**
 * Created on 2025/3/20.
 */
public class RabinMiller extends PrimePredicate {

    private static final int[] A = {
            2, 325, 9375, 28178, 450775, 9780504, 1795265022
    };

    /**
     * https://zhuanlan.zhihu.com/p/220203643
     */
    @Override
    public boolean isPrime(int x) {
        if (x < 3) {
            return x == 2;
        }
        if (x % 2 == 0) {
            return false;
        }
        int d = x - 1;
        int r = 0;
        while (d % 2 == 0) {
            d /= 2;
            r++;
        }
        for (int a : A) {
            int v = power(a, d, x);
            if (v <= 1 || v == x - 1) {
                continue;
            }
            for (int i = 0; i < r; i++) {
                v = (int) (((long) v) * v % x); // mod square
                if (v == x - 1 && i != r - 1) {
                    v = 1;
                    break;
                }
                if (v == 1) {
                    break;
                }
            }
            if (v != 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param base actually within int range
     * @param exponent actually within int range
     * @param modulus actually within int range, and positive
     * @return (base ** exponent) % modulus
     */
    private int power(long base, long exponent, long modulus) {
        long power = 1L;
        while (exponent != 0) {
            if ((exponent & 1) != 0) {
                power = power * base % modulus;
            }
            power = power * power % modulus;
            exponent >>= 1;
        }
        return (int) power;
    }
}
