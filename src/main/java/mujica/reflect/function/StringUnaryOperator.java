package mujica.reflect.function;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

import java.util.function.UnaryOperator;

@CodeHistory(date = "2022/5/25")
@FunctionalInterface
public interface StringUnaryOperator extends UnaryOperator<String> {

    @Override
    String apply(String string);

    default String apply(@NotNull String string, @Index(of = "string") int start, @Index(of = "string", inclusive = false) int end) {
        return apply(string.substring(start, end));
    }

    default void append(String in, @NotNull StringBuilder out) {
        out.append(apply(in));
    }

    default void append(@NotNull String in, @Index(of = "string") int start, @Index(of = "string", inclusive = false) int end, @NotNull StringBuilder out) {
        append(in.substring(start, end), out);
    }

    default void selfApply(@NotNull String[] io) {
        final int length = io.length;
        for (int index = 0; index < length; index++) {
            io[index] = apply(io[index]);
        }
    }

    @NotNull
    default String[] batchApply(@NotNull String[] in) {
        final int length = in.length;
        final String[] dst = new String[length];
        for (int index = 0; index < length; index++) {
            dst[index] = apply(in[index]);
        }
        return dst;
    }
}
