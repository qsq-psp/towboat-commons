package mujica.io.function;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.IOError;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Enumeration;

@CodeHistory(date = "2026/3/4")
public interface IOEnumeration<E> {

    boolean hasMoreElements() throws IOException;

    E nextElement() throws IOException;

    @NotNull
    default Enumeration<E> uncheck() {
        return new Enumeration<>() {

            @Override
            public boolean hasMoreElements() {
                try {
                    return IOEnumeration.this.hasMoreElements();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }

            @Override
            public E nextElement() {
                try {
                    return IOEnumeration.this.nextElement();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        };
    }

    @NotNull
    static <E> IOEnumeration<E> check(@NotNull Enumeration<E> enumeration) {
        return new IOEnumeration<>() {

            @Override
            public boolean hasMoreElements() throws IOException {
                try {
                    return enumeration.hasMoreElements();
                } catch (UncheckedIOException e) {
                    throw e.getCause();
                }
            }

            @Override
            public E nextElement() throws IOException {
                try {
                    return enumeration.nextElement();
                } catch (UncheckedIOException e) {
                    throw e.getCause();
                }
            }
        };
    }

    @NotNull
    default Enumeration<E> upgrade() {
        return new Enumeration<>() {

            @Override
            public boolean hasMoreElements() {
                try {
                    return IOEnumeration.this.hasMoreElements();
                } catch (IOException e) {
                    throw new IOError(e);
                }
            }

            @Override
            public E nextElement() {
                try {
                    return IOEnumeration.this.nextElement();
                } catch (IOException e) {
                    throw new IOError(e);
                }
            }
        };
    }

    @NotNull
    static <E> IOEnumeration<E> downgrade(@NotNull Enumeration<E> enumeration) {
        return new IOEnumeration<>() {

            @Override
            public boolean hasMoreElements() throws IOException {
                try {
                    return enumeration.hasMoreElements();
                } catch (IOError e) {
                    Throwable cause = e.getCause();
                    if (cause instanceof IOException) {
                        throw (IOException) cause;
                    }
                    throw e;
                }
            }

            @Override
            public E nextElement() throws IOException {
                try {
                    return enumeration.nextElement();
                } catch (IOError e) {
                    Throwable cause = e.getCause();
                    if (cause instanceof IOException) {
                        throw (IOException) cause;
                    }
                    throw e;
                }
            }
        };
    }
}
