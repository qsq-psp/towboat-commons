package mujica.math.algebra.prime;

import mujica.reflect.function.PredicateMistake;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/3/18.
 */
public class PrimePredicate {

    public static final PrimePredicate INSTANCE = new PrimePredicate();

    @NotNull
    public PredicateMistake primeTestProperty() {
        return PredicateMistake.NONE;
    }

    public boolean isPrime(int value) {
        if (value < 2) {
            return false;
        }
        if (value % 2 == 0 || value % 3 == 0) {
            return value < 4;
        }
        if (value == 5 || value == 7) {
            return true;
        }
        int base = 0;
        for (int batch = 1;; batch <<= 1) {
            for (int index = 0; index < batch; index++) {
                base += 6;
                if (value % (base - 1) == 0 || value % (base + 1) == 0) {
                    return false;
                }
            }
            if (base * base > value) {
                return true;
            }
        }
    }

    public int previousPrime(int value) {
        while (value > 2) {
            value--;
            if (isPrime(value)) {
                return value;
            }
        }
        throw new ArithmeticException();
    }

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
