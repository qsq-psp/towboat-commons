package mujica.io.codec;

import org.jetbrains.annotations.NotNull;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Created on 2025/9/22.
 */
public class IndentWriter extends FilterWriter {

    /**
     * Create a new filtered writer.
     *
     * @param out a Writer object to provide the underlying stream.
     * @throws NullPointerException if <code>out</code> is <code>null</code>
     */
    public IndentWriter(@NotNull Writer out) {
        super(out);
    }

    public void newLine() throws IOException {
        write(System.lineSeparator());
    }

    public void indentIn() {
        // pass
    }

    public void indentOut() {
        // pass
    }
}
