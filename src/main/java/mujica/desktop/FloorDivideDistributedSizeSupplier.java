package mujica.desktop;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;

import java.io.Serializable;
import java.util.function.IntSupplier;

@CodeHistory(date = "2026/10/15")
public class FloorDivideDistributedSizeSupplier implements IntSupplier, Serializable {

    private static final long serialVersionUID = 0xF70E29ACE801E9ECL;

    @Name(value = "被除数", language = "zh")
    @Name(value = "分子", language = "zh")
    final int numerator;

    @Name(value = "除数", language = "zh")
    @Name(value = "分母", language = "zh")
    final int denominator;

    int index;

    public FloorDivideDistributedSizeSupplier(int numerator, int denominator) {
        super();
        if (denominator == 0) {
            throw new ArithmeticException("divide by zero");
        }
        this.numerator = numerator;
        this.denominator = denominator;
    }

    @Override
    public int getAsInt() {
        final int value = numerator * (index + 1) / denominator - numerator * index / denominator;
        index++;
        return value;
    }
}
