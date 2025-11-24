package mujica.io.nest;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.attribute.BasicFileAttributes;

@CodeHistory(date = "2025/10/12")
public interface NestReader<P extends NestResourceKey, A extends BasicFileAttributes> {

    @CodeHistory(date = "2025/10/12")
    interface StreamCallback<P extends NestResourceKey, A extends BasicFileAttributes> {

        void read(@NotNull P path, @NotNull A attributes, @NotNull InputStream is) throws IOException;
    }

    void readAsStream(@NotNull InputStream is, @NotNull StreamCallback<P, A> callback) throws IOException;

    @CodeHistory(date = "2025/10/13")
    interface AttributesCallback<P extends NestResourceKey, A extends BasicFileAttributes> {

        void read(@NotNull P path, @NotNull A attributes) throws IOException;
    }

    void readAttributes(@NotNull InputStream is, @NotNull AttributesCallback<P, A> callback) throws IOException;
}
