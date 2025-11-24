package mujica.io.compress;

import mujica.reflect.function.IOFunction;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

@CodeHistory(date = "2025/10/19", name = "TowboatInflaterInputStream")
@CodeHistory(date = "2025/11/7")
@ReferencePage(title = "DEFLATE Compressed Data Format Specification version 1.3", href = "https://www.rfc-editor.org/rfc/rfc1951.html")
public abstract class AbstractInflateInputStream extends FilterInputStream {

    protected static final int MAX_RUN_BUFFER_DISTANCE = 1 << 15;

    protected AbstractInflateInputStream(@NotNull InputStream in) {
        super(in);
    }

    public abstract void trailingBytesMode();

    @CodeHistory(date = "2025/11/13")
    @FunctionalInterface
    public interface Constructor1 extends IOFunction<InputStream, AbstractInflateInputStream> {

        @NotNull
        @Override
        AbstractInflateInputStream apply(@NotNull InputStream in) throws IOException;
    }

    @CodeHistory(date = "2025/11/12")
    @FunctionalInterface
    public interface Constructor2 {

        @NotNull
        AbstractInflateInputStream apply(@NotNull InputStream in, int maxRunBufferDistance) throws IOException;
    }
}
