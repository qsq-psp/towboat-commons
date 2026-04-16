package mujica.text.number;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

/**
 * Created on 2026/4/3.
 */
@CodeHistory(date = "2026/4/3")
public class JoinedIntegralAppender extends IntegralAppender {

    @NotNull
    protected final IntegralAppender[] parts;

    @NotNull
    protected final String separator;

    public JoinedIntegralAppender(@NotNull IntegralAppender[] parts, @NotNull String separator) {
        super();
        this.parts = parts;
        this.separator = separator;
    }

    @Override
    public void acceptByte(byte value, @NotNull StringBuilder out) {
        final int length = parts.length;
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.append(separator);
            }
            parts[index].acceptByte(value, out);
        }
    }

    @Override
    public void acceptShort(short value, @NotNull StringBuilder out) {
        final int length = parts.length;
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.append(separator);
            }
            parts[index].acceptShort(value, out);
        }
    }

    @Override
    public void acceptChar(char value, @NotNull StringBuilder out) {
        final int length = parts.length;
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.append(separator);
            }
            parts[index].acceptChar(value, out);
        }
    }

    @Override
    public void acceptInt(int value, @NotNull StringBuilder out) {
        final int length = parts.length;
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.append(separator);
            }
            parts[index].acceptInt(value, out);
        }
    }

    @Override
    public void acceptLong(long value, @NotNull StringBuilder out) {
        final int length = parts.length;
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.append(separator);
            }
            parts[index].acceptLong(value, out);
        }
    }

    @Override
    public void acceptBig(@NotNull BigInteger value, @NotNull StringBuilder out) {
        final int length = parts.length;
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.append(separator);
            }
            parts[index].acceptBig(value, out);
        }
    }
}
