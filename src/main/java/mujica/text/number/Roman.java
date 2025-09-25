package mujica.text.number;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

@CodeHistory(date = "2021/10/26", project = "va")
@CodeHistory(date = "2022/10/19", project = "Ultramarine")
@CodeHistory(date = "2025/2/26")
public class Roman implements IntegralToStringFunction {

    public static final Roman UPPER = new Roman('I', 'V', 'X', 'L', 'C', 'D', 'M');

    public static final Roman LOWER = new Roman('i', 'v', 'x', 'l', 'c', 'd', 'm');

    private static final char OVERLINE1 = '\u0305';

    private static final char OVERLINE2 = '\u033f';

    private final char n1, n5, n10, n50, n100, n500, n1000;

    private Roman(char n1, char n5, char n10, char n50, char n100, char n500, char n1000) {
        super();
        this.n1 = n1;
        this.n5 = n5;
        this.n10 = n10;
        this.n50 = n50;
        this.n100 = n100;
        this.n500 = n500;
        this.n1000 = n1000;
    }

    private void overline(int level, @NotNull StringBuilder out) {
        while (level >= 2) {
            out.append(OVERLINE2);
            level -= 2;
        }
        if (level >= 1) {
            out.append(OVERLINE1);
        }
    }

    private void append(long value, int level, @NotNull StringBuilder out) {
        if (value >= 4000L) {
            append(value / 1000L, level + 1, out);
            value %= 1000L;
        } else {
            while (value >= 1000L) {
                value -= 1000L;
                out.append(n1000);
                overline(level, out);
            }
        }
        if (value >= 900L) {
            value -= 900L;
            out.append(n100);
            overline(level, out);
            out.append(n1000);
            overline(level, out);
        }
        if (value >= 500L) {
            value -= 500L;
            out.append(n500);
            overline(level, out);
        }
        if (value >= 400L) {
            value -= 400L;
            out.append(n100);
            overline(level, out);
            out.append(n500);
            overline(level, out);
        }
        while (value >= 100L) {
            value -= 100L;
            out.append(n100);
            overline(level, out);
        }
        if (value >= 90L) {
            value -= 90L;
            out.append(n10);
            overline(level, out);
            out.append(n100);
            overline(level, out);
        }
        if (value >= 50L) {
            value -= 50L;
            out.append(n50);
            overline(level, out);
        }
        if (value >= 40L) {
            value -= 40L;
            out.append(n10);
            overline(level, out);
            out.append(n50);
            overline(level, out);
        }
        while (value >= 10L) {
            value -= 10L;
            out.append(n10);
            overline(level, out);
        }
        if (value >= 9L) {
            value -= 9L;
            out.append(n1);
            overline(level, out);
            out.append(n10);
        }
        if (value >= 5L) {
            value -= 5L;
            out.append(n5);
            overline(level, out);
        }
        if (value >= 4L) {
            value -= 4L;
            out.append(n1);
            overline(level, out);
            out.append(n5);
            overline(level, out);
        }
        while (value >= 1L) {
            value--;
            out.append(n1);
            overline(level, out);
        }
    }

    @Override
    public void append(int value, @NotNull StringBuilder out) {
        if (value <= 0) {
            throw new IllegalArgumentException();
        }
        append(value, 0, out);
    }

    @NotNull
    @Override
    public String stringify(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException();
        }
        final StringBuilder sb = new StringBuilder();
        append(value, 0, sb);
        return sb.toString();
    }

    @Override
    public void append(long value, @NotNull StringBuilder out) {
        if (value <= 0L) {
            throw new IllegalArgumentException();
        }
        append(value, 0, out);
    }

    @NotNull
    @Override
    public String stringify(long value) {
        if (value <= 0) {
            throw new IllegalArgumentException();
        }
        final StringBuilder sb = new StringBuilder();
        append(value, 0, sb);
        return sb.toString();
    }

    @Override
    public void append(@NotNull BigInteger value, @NotNull StringBuilder out) {
        //
    }

    @NotNull
    @Override
    public String stringify(@NotNull BigInteger value) {
        return "";
    }
}
