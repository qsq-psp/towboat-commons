package mujica.io.stream;

import org.jetbrains.annotations.NotNull;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created on 2025/5/7.
 */
public class BaseAnyDecodeInputStream extends FilterInputStream {

    @NotNull
    private final BaseAnyCodec codec;

    public BaseAnyDecodeInputStream(@NotNull InputStream in, @NotNull BaseAnyCodec codec) {
        super(in);
        this.codec = codec;
    }

    @Override
    public int read() throws IOException {
        while (codec.moreCode()) {
            int code = in.read();
            if (code != -1) {
                codec.decodeIn((byte) code);
            } else {
                codec.decodeStop();
            }
        }
        if (codec.moreOctet()) {
            return 0xff & codec.decodeOut();
        } else {
            return -1;
        }
    }

    @Override
    public int read(@NotNull byte[] array, int offset, int length) throws IOException {
        final int start = offset;
        final int limit = Math.addExact(offset, length);
        boolean more = true;
        while (more) {
            while (codec.moreCode()) {
                int code = in.read();
                if (code != -1) {
                    codec.decodeIn((byte) code);
                } else {
                    codec.decodeStop();
                }
            }
            more = false;
            while (codec.moreOctet()) {
                if (offset < limit) {
                    array[offset++] = codec.decodeOut();
                    more = true;
                } else {
                    more = false;
                    break;
                }
            }
        }
        return offset - start;
    }
}
