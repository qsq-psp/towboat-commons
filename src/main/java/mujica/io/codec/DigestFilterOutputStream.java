package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferenceCode;
import mujica.reflect.modifier.Stable;
import org.jetbrains.annotations.NotNull;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;

@CodeHistory(date = "2023/10/10", project = "Ultramarine")
@CodeHistory(date = "2025/3/11")
@Stable(date = "2026/2/15")
@ReferenceCode(groupId = "JDK", artifactId = "java.base", version = "12", fullyQualifiedName = "java.security.DigestOutputStream")
public class DigestFilterOutputStream extends FilterOutputStream implements DigestStream {

    @NotNull
    final MessageDigest digest;

    public DigestFilterOutputStream(@NotNull OutputStream out, @NotNull MessageDigest digest) {
        super(out);
        this.digest = digest;
    }

    @Override
    @NotNull
    public MessageDigest getDigest() {
        return digest;
    }

    @Override
    public void write(int b) throws IOException {
        digest.update((byte) b);
        out.write(b); // in java.security.DigestOutputStream, write to inner stream first
    }

    /**
     * this.read(byte[]) is delegated to this.read(byte[], int, int), not in.read(byte[]) or in.read(byte[], int, int)
     */
    @Override
    public void write(@NotNull byte[] b, int off, int len) throws IOException {
        digest.update(b, off, len);
        out.write(b, off, len);
    }

    @NotNull
    public String toString() {
        return "DigestFilterOutputStream[" + digest + "]";
    }
}
