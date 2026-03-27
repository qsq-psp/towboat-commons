package mujica.io.stream;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

@CodeHistory(date = "2025/9/22")
public class IndentWriter extends FilterWriter {

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
