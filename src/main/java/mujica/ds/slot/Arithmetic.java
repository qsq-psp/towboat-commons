package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;

@CodeHistory(date = "2025/6/11", name = "NumberStorage")
@CodeHistory(date = "2025/6/14", name = "NumericContext")
@CodeHistory(date = "2026/6/5")
public interface Arithmetic<S> extends SlotAllocator<S> {

    @NotNull
    @Override
    S newSlot();

    @Name(value = "零元", language = "zh")
    void zero(@NotNull S out);

    @Name(value = "幺元", language = "zh")
    void one(@NotNull S out);

    void two(@NotNull S out);

    int compareSlot(@NotNull S left, @NotNull S right);

    int sign(@NotNull S in);

    @Override
    void move(@NotNull S in, @NotNull S out);

    void negate(@NotNull S in, @NotNull S out);

    @Name(value = "绝对值", language = "zh")
    void absolute(@NotNull S in, @NotNull S out);

    @Name(value = "共轭", language = "zh")
    void conjugate(@NotNull S variable, @NotNull S result);

    @Name(value = "转置", language = "zh")
    void transpose(@NotNull S variable, @NotNull S result);

    @Name(value = "共轭转置", language = "zh")
    void conjugateTranspose(@NotNull S in, @NotNull S out);

    @Name(value = "可逆", language = "zh")
    boolean isInvertible(@NotNull S variable);

    @Name(value = "乘法逆元", language = "zh")
    void invert(@NotNull S in, @NotNull S out);

    @Name(value = "秩", language = "zh")
    int rank(@NotNull S in);

    /**
     * in < out and do not exist a number mid where in < mid < out, if out do not exist, an Exception is thrown
     */
    void increase(@NotNull S in, @NotNull S out);

    /**
     * out < in and do not exist a number mid where out < mid < in, if out do not exist, an Exception is thrown
     */
    void decrease(@NotNull S in, @NotNull S out);

    void add(@NotNull S left, @NotNull S right, @NotNull S out);

    void subtract(@NotNull S left, @NotNull S right, @NotNull S out);

    void multiply(@NotNull S left, @NotNull S right, @NotNull S out);

    @Name(value = "平方", language = "zh")
    void square(@NotNull S in, @NotNull S out);

    @Name(value = "三角数列", language = "zh")
    void triangle(@NotNull S in, @NotNull S out);

    void divide(@NotNull S left, @NotNull S right, @NotNull S result, @NotNull RoundingMode mode);

    void divide(@NotNull S left, @NotNull S right, @Nullable S quotient, @Nullable S remainder);

    @Name(value = "算术平均", language = "zh")
    void mean(@NotNull S left, @NotNull S right, @NotNull S result, @NotNull RoundingMode mode);

    @Name(value = "greatest common divisor", language = "en")
    @Name(value = "最大公约数", language = "zh")
    @Name(value = "最大公因数", language = "zh")
    void gcd(@NotNull S left, @NotNull S right, @NotNull S result);

    @Name(value = "least common multiple", language = "en")
    @Name(value = "最小公倍数", language = "zh")
    void lcm(@NotNull S left, @NotNull S right, @NotNull S result);

    @Name(value = "阶乘", language = "zh")
    void factorial(@NotNull S variable, @NotNull S result);

    @Name(value = "双阶乘", language = "zh")
    void doubleFactorial(@NotNull S variable, @NotNull S result);

    @Name(value = "斐波那契数列", language = "zh")
    void fibonacci(@NotNull S variable, @NotNull S result);

    @Name(value = "排列", language = "zh")
    void arrangement(@NotNull S nSlot, @NotNull S mSlot, @NotNull S result);

    @Name(value = "组合", language = "zh")
    void combination(@NotNull S nSlot, @NotNull S mSlot, @NotNull S result);

    @Name(value = "平方根", language = "zh")
    void squareRoot(@NotNull S variable, @NotNull S result, @NotNull RoundingMode mode);

    void log2(@NotNull S variable, @NotNull S result, @NotNull RoundingMode mode);
}
