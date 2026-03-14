package mujica.text.number;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

@CodeHistory(date = "2026/3/9")
public class MarkedIntegralAppender extends IntegralAppender {

    private static final long serialVersionUID = 0x1F2AC87A4C3FC5C4L;

    @NotNull
    protected final IntegralAppender appender;

    public MarkedIntegralAppender(@NotNull IntegralAppender appender) {
        super();
        this.appender = appender;
    }

    @Override
    public void acceptByte(byte value, @NotNull StringBuilder out) {
        appender.acceptByte(value, out.append("(byte) "));
    }

    @Override
    public void acceptShort(short value, @NotNull StringBuilder out) {
        appender.acceptShort(value, out.append("(short) "));
    }

    @Override
    public void acceptCharacter(char value, @NotNull StringBuilder out) {
        appender.acceptCharacter(value, out.append("(char) "));
    }

    @Override
    public void acceptLong(long value, @NotNull StringBuilder out) {
        appender.acceptLong(value, out);
        out.append("L");
    }

    @Override
    public void acceptBig(@NotNull BigInteger value, @NotNull StringBuilder out) {
        appender.acceptBig(value, out.append("(big) "));
    }
}
