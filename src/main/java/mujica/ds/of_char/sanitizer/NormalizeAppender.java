package mujica.ds.of_char.sanitizer;

import io.netty.buffer.ByteBuf;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.List;

/**
 * Created on 2026/5/4.
 */
@CodeHistory(date = "2026/5/4")
public class NormalizeAppender extends CharSequenceAppender {

    @NotNull
    protected final Normalizer.Form form;

    public NormalizeAppender(@NotNull Normalizer.Form form) {
        super();
        this.form = form;
    }

    @Override
    public boolean isIdentity(@NotNull CharSequence string) {
        return Normalizer.isNormalized(string, form);
    }

    @Override
    public void write(@NotNull CharSequence string, @NotNull ByteBuffer out) {
        out.put(Normalizer.normalize(string, form).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void write(@NotNull CharSequence string, @NotNull ByteBuf out) {
        out.writeCharSequence(Normalizer.normalize(string, form), StandardCharsets.UTF_8);
    }

    @Override
    public void append(@NotNull CharSequence string, @NotNull StringBuilder out) {
        out.append(Normalizer.normalize(string, form));
    }

    @Override
    public void append(@NotNull CharSequence string, @NotNull StringBuffer out) {
        out.append(Normalizer.normalize(string, form));
    }

    @Override
    public void print(@NotNull CharSequence string, @NotNull PrintStream out) {
        out.print(Normalizer.normalize(string, form)); // append is also OK
    }

    @Override
    public void addTokens(@NotNull CharSequence string, @NotNull List<Object> out) {
        out.add(Normalizer.normalize(string, form));
    }
}
