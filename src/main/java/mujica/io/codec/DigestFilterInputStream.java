package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DataType;
import mujica.reflect.modifier.Stable;
import org.jetbrains.annotations.NotNull;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * same with java.security.DigestInputStream
 */
@CodeHistory(date = "2023/10/9", project = "Ultramarine")
@CodeHistory(date = "2025/3/11")
@Stable(date = "2025/11/28")
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
    @DataType("u8+{-1}")
    public int read() throws IOException {
        final int data = in.read();
        if (data != -1) {
            digest.update((byte) data);
        }
        return data;
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
