package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;
import java.security.MessageDigest;

@CodeHistory(date = "2020/12/24", project = "webbiton")
@CodeHistory(date = "2022/6/15", project = "Ultramarine")
@CodeHistory(date = "2025/3/11")
public class DigestOutputStream extends OutputStream implements DigestStream {

    @NotNull
    final MessageDigest digest;

    public DigestOutputStream(@NotNull MessageDigest digest) {
        super();
        this.digest = digest;
    }

    @Override
    @NotNull
    public MessageDigest getDigest() {
        return digest;
    }

    @Override
    public void write(int b) {
        digest.update((byte) b);
    }

    @Override
    public void write(@NotNull byte[] b) {
        digest.update(b);
    }

    @Override
    public void write(@NotNull byte[] b, int off, int len) {
        digest.update(b, off, len);
    }

    @NotNull
    public String toString() {
        return "DigestOutputStream[" + digest + "]";
    }
}
