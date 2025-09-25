package mujica.io.stream;

import org.jetbrains.annotations.NotNull;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * Created in Ultramarine on 2023/10/9.
 * Moved here on 2025/3/11.
 * Same with java.security.DigestInputStream
 */
public class DigestFilterInputStream extends FilterInputStream implements DigestStream {

    @NotNull
    final MessageDigest digest;

    public DigestFilterInputStream(@NotNull InputStream in, @NotNull MessageDigest digest) {
        super(in);
        this.digest = digest;
    }

    @Override
    @NotNull
    public MessageDigest getDigest() {
        return digest;
    }

    @Override
    public int read() throws IOException {
        final int b = in.read();
        if (b != -1) {
            digest.update((byte) b);
        }
        return b;
    }

    /**
     * this.read(byte[]) is delegated to this.read(byte[], int, int), not in.read(byte[]) or in.read(byte[], int, int)
     */
    @Override
    public int read(@NotNull byte[] b, int off, int len) throws IOException {
        final int cnt = in.read(b, off, len);
        if (cnt > 0) {
            digest.update(b, off, cnt);
        }
        return cnt;
    }

    @NotNull
    public String toString() {
        return "DigestFilterInputStream[" + digest + "]";
    }
}
