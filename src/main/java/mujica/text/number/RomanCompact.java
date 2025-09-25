package mujica.text.number;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

@CodeHistory(date = "2021/10/26", project = "va")
@CodeHistory(date = "2022/10/19", project = "Ultramarine")
@CodeHistory(date = "2025/2/28")
public class RomanCompact implements IntegralToStringFunction {

    public static final RomanCompact UPPER = new RomanCompact(
            new char[] {'\u2160', '\u2161', '\u2162', '\u2163', '\u2164', '\u2165', '\u2166', '\u2167', '\u2168', '\u2169', '\u216a', '\u216b'},
            '\u216c', '\u216d', '\u216e', '\u216f'
    );

    public static final RomanCompact LOWER = new RomanCompact(
            new char[] {'\u2170', '\u2171', '\u2172', '\u2173', '\u2174', '\u2175', '\u2176', '\u2177', '\u2178', '\u2179', '\u217a', '\u217b'},
            '\u217c', '\u217d', '\u217e', '\u217f'
    );

    private static final char OVERLINE1 = '\u0305';

    private static final char OVERLINE2 = '\u033f';

    @NotNull
    final char[] m12;

    final char n50, n100, n500, n1000;

    public RomanCompact(@NotNull char[] m12, char n50, char n100, char n500, char n1000) {
        super();
        this.m12 = m12;
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

    private void number(long value, int level, @NotNull StringBuilder out) {
        if (value >= 4000L) {
            number(value / 1000L, level + 1, out);
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
            out.append(m12[9]);
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
            out.append(m12[9]);
            overline(level, out);
            out.append(n50);
            overline(level, out);
        }
        while (true) {
            if (value <= 12L) {
                out.append(m12[(int) value - 1]);
                break;
            }
            value -= 10L;
            out.append(m12[9]);
            overline(level, out);
        }
    }

    @Override
    public void append(int value, @NotNull StringBuilder out) {
        if (value <= 0L) {
            throw new IllegalArgumentException();
        }
        number(value, 0, out);
    }

    @NotNull
    @Override
    public String stringify(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException();
        }
        final StringBuilder sb = new StringBuilder();
        number(value, 0, sb);
        return sb.toString();
    }

    @Override
    public void append(long value, @NotNull StringBuilder out) {
        if (value <= 0L) {
            throw new IllegalArgumentException();
        }
        number(value, 0, out);
    }

    @NotNull
    @Override
    public String stringify(long value) {
        if (value <= 0) {
            throw new IllegalArgumentException();
        }
        final StringBuilder sb = new StringBuilder();
        number(value, 0, sb);
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
