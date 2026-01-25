package mujica.io.compress;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.io.FilterOutputStream;
import java.io.OutputStream;

@CodeHistory(date = "2025/12/26")
@ReferencePage(title = "DEFLATE Compressed Data Format Specification version 1.3", href = "https://www.rfc-editor.org/rfc/rfc1951.html")
public abstract class AbstractInflateOutputStream extends FilterOutputStream {

    protected AbstractInflateOutputStream(@NotNull OutputStream out) {
        super(out);
    }
}
