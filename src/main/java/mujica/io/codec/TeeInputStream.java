package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferenceCode;
import org.jetbrains.annotations.NotNull;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@ReferenceCode(groupId = "org.bouncycastle", artifactId = "bcprov-lts8on", fullyQualifiedName = "org.bouncycastle.util.io.TeeInputStream")
@CodeHistory(date = "2025/12/28")
public class TeeInputStream extends FilterInputStream {

    @NotNull
    protected final OutputStream out;

    public TeeInputStream(@NotNull InputStream in, @NotNull OutputStream out) {
        super(in);
        this.out = out;
    }

    @NotNull
    public InputStream getInputStream() {
        return in;
    }

    @NotNull
    public OutputStream getOutputStream() {
        return out;
    }

    @Override
    public int read() throws IOException {
        final int x = in.read();
        if (x >= 0) {
            out.write(x);
        }
        return x;
    }

    @Override
    public int read(@NotNull byte[] array, int offset, int length) throws IOException {
        length = in.read(array, offset, length);
        if (length > 0) {
            out.write(array, offset, length);
        }
        return length;
    }

    @Override
    public void close() throws IOException {
        in.close();
        out.close();
    }
}
