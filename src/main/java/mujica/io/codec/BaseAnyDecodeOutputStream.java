package mujica.io.codec;

import org.jetbrains.annotations.NotNull;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created on 2025/5/7.
 */
public class BaseAnyDecodeOutputStream extends FilterOutputStream {

    @NotNull
    private final BaseAnyCodec codec;

    public BaseAnyDecodeOutputStream(@NotNull OutputStream out, @NotNull BaseAnyCodec codec) {
        super(out);
        this.codec = codec;
        codec.decodeStart();
    }

    @Override
    public void write(int code) throws IOException {
        while (codec.moreOctet()) {
            out.write(0xff & codec.decodeOut());
        }
        if (codec.moreCode()) {
            codec.decodeIn((byte) code);
        } else {
            throw new IOException();
        }
    }

    @Override
    public void close() throws IOException {
        codec.decodeStop();
        boolean needFlush = false;
        while (codec.moreOctet()) {
            out.write(0xff & codec.decodeOut());
            needFlush = true;
        }
        if (needFlush) {
            out.flush();
        }
        out.close();
    }
}
