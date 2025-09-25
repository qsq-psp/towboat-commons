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
            return true;
        }
        int num = 0;
        for (int batch = 1;; batch <<= 1) {
            for (int i = 0; i < batch; i++) {
                num += 6;
                if (value % (num - 1) == 0 || value % (num + 1) == 0) {
                    return true;
                }
            }
            if (num * num > value) {
                return false;
            }
        }
    }

    public int previousPrime(int value) {
        return value;
    }

    public int nextPrime(int value) {
        return value;
    }
}
