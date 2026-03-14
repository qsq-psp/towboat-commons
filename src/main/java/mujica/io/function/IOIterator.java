package mujica.io.function;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Stable;
import org.jetbrains.annotations.NotNull;

import java.io.IOError;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.function.Consumer;

@CodeHistory(date = "2025/4/7")
@Stable(date = "2025/8/18")
public interface IOIterator<E> {

    boolean hasNext() throws IOException;

    E next() throws IOException;

    default void remove() throws IOException {
        throw new UnsupportedOperationException();
    }

    default void forEachRemaining(@NotNull IOConsumer<? super E> action) throws IOException {
        while (hasNext()) {
            action.accept(next());
        }
    }

    @NotNull
    default Iterator<E> uncheck() {
        return new Iterator<>() {

            @Override
            public boolean hasNext() {
                try {
                    return IOIterator.this.hasNext();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }

            @Override
            public E next() {
                try {
                    return IOIterator.this.next();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }

            @Override
            public void remove() {
                try {
                    IOIterator.this.remove();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }

            @Override
            public void forEachRemaining(Consumer<? super E> action) {
                try {
                    IOIterator.this.forEachRemaining(IOConsumer.check(action));
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        };
    }

    @NotNull
    static <E> IOIterator<E> check(@NotNull Iterator<E> iterator) {
        return new IOIterator<>() {

            @Override
            public boolean hasNext() throws IOException {
                try {
                    return iterator.hasNext();
                } catch (UncheckedIOException e) {
                    throw e.getCause();
                }
            }

            @Override
            public E next() throws IOException {
                try {
                    return iterator.next();
                } catch (UncheckedIOException e) {
                    throw e.getCause();
                }
            }

            @Override
            public void remove() throws IOException {
                try {
                    iterator.remove();
                } catch (UncheckedIOException e) {
                    throw e.getCause();
                }
            }

            @Override
            public void forEachRemaining(@NotNull IOConsumer<? super E> action) throws IOException {
                try {
                    iterator.forEachRemaining(action.uncheck());
                } catch (UncheckedIOException e) {
                    throw e.getCause();
                }
            }
        };
    }

    @NotNull
    default Iterator<E> upgrade() {
        return new Iterator<>() {

            @Override
            public boolean hasNext() {
                try {
                    return IOIterator.this.hasNext();
                } catch (IOException e) {
                    throw new IOError(e);
                }
            }

            @Override
            public E next() {
                try {
                    return IOIterator.this.next();
                } catch (IOException e) {
                    throw new IOError(e);
                }
            }

            @Override
            public void remove() {
                try {
                    IOIterator.this.remove();
                } catch (IOException e) {
                    throw new IOError(e);
                }
            }

            @Override
            public void forEachRemaining(Consumer<? super E> action) {
                try {
                    IOIterator.this.forEachRemaining(IOConsumer.downgrade(action));
                } catch (IOException e) {
                    throw new IOError(e);
                }
            }
        };
    }

    @NotNull
    static <E> IOIterator<E> downgrade(@NotNull Iterator<E> iterator) {
        return new IOIterator<>() {

            @Override
            public boolean hasNext() throws IOException {
                try {
                    return iterator.hasNext();
                } catch (IOError e) {
                    Throwable cause = e.getCause();
                    if (cause instanceof IOException) {
                        throw (IOException) cause;
                    }
                    throw e;
                }
            }

            @Override
            public E next() throws IOException {
                try {
                    return iterator.next();
                } catch (IOError e) {
                    Throwable cause = e.getCause();
                    if (cause instanceof IOException) {
                        throw (IOException) cause;
                    }
                    throw e;
                }
            }

            @Override
            public void remove() throws IOException {
                try {
                    iterator.remove();
                } catch (IOError e) {
                    Throwable cause = e.getCause();
                    if (cause instanceof IOException) {
                        throw (IOException) cause;
                    }
                    throw e;
                }
            }

            @Override
            public void forEachRemaining(@NotNull IOConsumer<? super E> action) throws IOException {
                try {
                    iterator.forEachRemaining(action.upgrade());
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
