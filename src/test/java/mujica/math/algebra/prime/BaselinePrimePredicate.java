package mujica.math.algebra.prime;

/**
 * Created on 2025/10/4.
 */
public class BaselinePrimePredicate extends PrimePredicate {

    public static final BaselinePrimePredicate INSTANCE = new BaselinePrimePredicate();

    @Override
    public boolean isPrime(int value) {
        if (value < 2) {
            return false;
        }
        for (int num = 2; num < value; num++) {
            if (value % num == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int previousPrime(int value) {
        while (value > 2) {
            value--;
            if (isPrime(value)) {
                return value;
            }
        }
        throw new ArithmeticException();
    }

    @Override
    public int nextPrime(int value) {
        if (value < 2) {
            return 2;
        }
        while (value >= 0) {
            value++;
            if (isPrime(value)) {
                return value;
            }
        }
        throw new ArithmeticException();
    }
}
