package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;

@CodeHistory(date = "2025/6/11", name = "NumberStorage")
@CodeHistory(date = "2025/6/14", name = "NumericContext")
@CodeHistory(date = "2026/6/5")
public interface Arithmetic<S> {

    @NotNull
    S newSlot();

    @Name(value = "零元", language = "zh")
    void zero(@NotNull S result);

    @Name(value = "幺元", language = "zh")
    void one(@NotNull S result);

    void two(@NotNull S result);

    int compareSlot(@NotNull S a, @NotNull S b);

    int sign(@NotNull S variable);

    void move(@NotNull S src, @NotNull S dst);

    void negate(@NotNull S variable, @NotNull S result);

    @Name(value = "绝对值", language = "zh")
    void absolute(@NotNull S variable, @NotNull S result);

    @Name(value = "共轭", language = "zh")
    void conjugate(@NotNull S variable, @NotNull S result);

    @Name(value = "转置", language = "zh")
    void transpose(@NotNull S variable, @NotNull S result);

    @Name(value = "共轭转置", language = "zh")
    void conjugateTranspose(@NotNull S variable, @NotNull S result);

    @Name(value = "可逆", language = "zh")
    boolean isInvertible(@NotNull S variable);

    void invert(@NotNull S variable, @NotNull S result);

    void increase(@NotNull S variable, @NotNull S result);

    void decrease(@NotNull S variable, @NotNull S result);

    void add(@NotNull S left, @NotNull S right, @NotNull S result);

    void subtract(@NotNull S left, @NotNull S right, @NotNull S result);

    void multiply(@NotNull S left, @NotNull S right, @NotNull S result);

    @Name(value = "平方", language = "zh")
    void square(@NotNull S variable, @NotNull S result);

    @Name(value = "三角数列", language = "zh")
    void triangle(@NotNull S variable, @NotNull S result);

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
