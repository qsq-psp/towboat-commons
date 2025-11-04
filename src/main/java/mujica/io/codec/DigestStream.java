package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.security.MessageDigest;

@CodeHistory(date = "2023/10/9", project = "Ultramarine")
@CodeHistory(date = "2025/3/11")
public interface DigestStream extends Closeable {

    @NotNull
    MessageDigest getDigest();
}
