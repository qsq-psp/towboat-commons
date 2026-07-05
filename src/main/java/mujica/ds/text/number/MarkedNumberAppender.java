package mujica.ds.text.number;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

@CodeHistory(date = "2026/3/9", name = "MarkedIntegralAppender")
@CodeHistory(date = "2026/4/16")
public class MarkedNumberAppender extends DefaultNumberAppender {

    private static final long serialVersionUID = 0x1F2AC87A4C3FC5C4L;

    public static final MarkedNumberAppender INSTANCE = new MarkedNumberAppender(DefaultNumberAppender.INSTANCE);

    @NotNull
    protected final IntegralAppender integralAppender;

    @NotNull
    protected final DecimalAppender decimalAppender;

    public MarkedNumberAppender(@NotNull IntegralAppender integralAppender, @NotNull DecimalAppender decimalAppender) {
        super();
        this.integralAppender = integralAppender;
        this.decimalAppender = decimalAppender;
    }

    public MarkedNumberAppender(@NotNull IntegralAppender integralAppender) {
        this(integralAppender, DefaultNumberAppender.INSTANCE);
    }

    public MarkedNumberAppender(@NotNull DecimalAppender decimalAppender) {
        this(DefaultNumberAppender.INSTANCE, decimalAppender);
    }

    public MarkedNumberAppender(@NotNull DefaultNumberAppender appender) {
        this(appender, appender);
    }

    @Override
    public void append(byte value, @NotNull StringBuilder out) {
        integralAppender.append(value, out.append("(byte) "));
    }

    @Override
    public void append(short value, @NotNull StringBuilder out) {
        integralAppender.append(value, out.append("(short) "));
    }

    @Override
    public void append(char value, @NotNull StringBuilder out) {
        integralAppender.append(value, out.append("(char) "));
    }

    @Override
    public void append(long value, @NotNull StringBuilder out) {
        integralAppender.append(value, out);
        out.append("L");
    }

    @Override
    public void append(@NotNull BigInteger value, @NotNull StringBuilder out) {
        integralAppender.append(value, out.append("(big) "));
    }

    @Override
    public void append(float value, @NotNull StringBuilder out) {
        decimalAppender.append(value, out);
        out.append("F");
    }

    @Override
    public void append(double value, @NotNull StringBuilder out) {
        decimalAppender.append(value, out);
        out.append("D");
    }
}
