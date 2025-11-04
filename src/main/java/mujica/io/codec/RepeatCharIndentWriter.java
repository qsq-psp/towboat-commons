package mujica.io.codec;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

/**
 * Created on 2025/9/22.
 */
public class RepeatCharIndentWriter extends IndentWriter {

    static final int TEMPLATE_SIZE = 64;

    final char[] template = new char[TEMPLATE_SIZE];

    final int multiplier;

    int indent;

    /**
     * Create a new filtered writer.
     *
     * @param out a Writer object to provide the underlying stream.
     * @throws NullPointerException if <code>out</code> is <code>null</code>
     */
    public RepeatCharIndentWriter(@NotNull Writer out, char ch, int count) {
        super(out);
        Arrays.fill(template, ch);
        multiplier = count;
    }

    public RepeatCharIndentWriter(@NotNull Writer out) {
        this(out, ' ', 4);
    }

    @Override
    public void newLine() throws IOException {
        super.newLine();
        int remaining = indent;
        while (remaining >= TEMPLATE_SIZE) {
            write(template, 0, TEMPLATE_SIZE);
            remaining -= TEMPLATE_SIZE;
        }
        if (remaining != 0) {
            write(template, 0, remaining);
        }
    }

    @Override
    public void indentIn() {
        indent += multiplier;
    }

    @Override
    public void indentOut() {
        indent -= multiplier;
    }
}
