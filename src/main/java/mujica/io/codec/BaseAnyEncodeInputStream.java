package mujica.io.codec;

import org.jetbrains.annotations.NotNull;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created on 2025/5/7.
 */
public class BaseAnyEncodeInputStream extends FilterInputStream {

    @NotNull
    private final BaseAnyCodec codec;

    protected BaseAnyEncodeInputStream(InputStream in, @NotNull BaseAnyCodec codec) {
        super(in);
        this.codec = codec;
        codec.encodeStart();
    }

    @Override
    public int read() throws IOException {
        while (codec.moreOctet()) {
            int octet = in.read();
            if (octet != -1) {
                codec.encodeIn((byte) octet);
            } else {
                codec.encodeStop();
            }
        }
        if (codec.moreCode()) {
            return 0xff & codec.encodeOut();
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
            while (codec.moreOctet()) {
                int octet = in.read();
                if (octet != -1) {
                    codec.encodeIn((byte) octet);
                } else {
                    codec.encodeStop();
                }
            }
            more = false;
            while (codec.moreCode()) {
                if (offset < limit) {
                    array[offset++] = codec.encodeOut();
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
