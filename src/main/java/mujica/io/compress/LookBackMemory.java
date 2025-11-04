package mujica.io.compress;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/10/31", name = "LookBackMemory")
public interface LookBackMemory { // maybe named RunBuffer

    void write(int data);

    int write(@NotNull byte[] array, @Index(of = "array") int offset, int length);

    int copyAndWrite(int distance);

    int copyAndWrite(int distance, @NotNull byte[] array, @Index(of = "array") int offset, int length);

    void release();
}
