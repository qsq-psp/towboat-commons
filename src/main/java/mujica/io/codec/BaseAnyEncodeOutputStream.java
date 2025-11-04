package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created on 2025/5/7.
 */
@CodeHistory(date = "2025/5/7")
public class BaseAnyEncodeOutputStream extends FilterOutputStream {

    @NotNull
    private final BaseAnyCodec codec;

    public BaseAnyEncodeOutputStream(@NotNull OutputStream out, @NotNull BaseAnyCodec codec) {
        super(out);
        this.codec = codec;
        codec.encodeStart();
    }

    @Override
    public void write(int octet) throws IOException {
        while (codec.moreCode()) {
            out.write(0xff & codec.encodeOut());
        }
        if (codec.moreOctet()) {
            codec.encodeIn((byte) octet);
        } else {
            throw new IOException();
        }
    }

    @Override
    public void close() throws IOException {
        codec.encodeStop();
        boolean needFlush = false;
        while (codec.moreCode()) {
            out.write(0xff & codec.encodeOut());
            needFlush = true;
        }
        if (needFlush) {
            out.flush();
        }
        out.close();
    }
}
