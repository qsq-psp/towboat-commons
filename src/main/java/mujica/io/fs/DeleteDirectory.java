package mujica.io.fs;

import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Promise;
import mujica.netty.concurrent.HeapRunnable;
import mujica.netty.concurrent.StackRunnable;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.BooleanSupplier;

@CodeHistory(date = "2026/9/21")
public abstract class DeleteDirectory {

    @NotNull
    final Promise<Void> promise;

    @NotNull
    final Path start;

    protected DeleteDirectory(@NotNull Promise<Void> promise, @NotNull Path start) {
        super();
        this.promise = promise;
        this.start = start;
    }

    @CodeHistory(date = "2026/9/21")
    public static class StackTask extends DeleteDirectory implements StackRunnable {

        public StackTask(@NotNull EventExecutor executor, @NotNull Path start) {
            super(executor.newPromise(), start);
        }

        @Override
        public void run(@NotNull BooleanSupplier yield) {
            try {
                Files.walkFileTree(start, new Visitor(yield));
                promise.setSuccess(null);
            } catch (Throwable t) {
                promise.setFailure(t);
                if (t instanceof ThreadDeath) {
                    throw (ThreadDeath) t;
                }
            }
        }

        @CodeHistory(date = "2026/9/22")
        private static class Visitor implements FileVisitor<Path> {

            @NotNull
            final BooleanSupplier yield;

            Visitor(@NotNull BooleanSupplier yield) {
                super();
                this.yield = yield;
            }

            @NotNull
            private FileVisitResult result() {
                if (yield.getAsBoolean()) {
                    return FileVisitResult.CONTINUE;
                } else {
                    return FileVisitResult.TERMINATE;
                }
            }

            @NotNull
            @Override
            public FileVisitResult preVisitDirectory(@NotNull Path dir, BasicFileAttributes attrs) {
                return result();
            }

            @NotNull
            @Override
            public FileVisitResult visitFile(@NotNull Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return result();
            }

            @Override
            public FileVisitResult visitFileFailed(@NotNull Path file, IOException e) throws IOException {
                throw e;
            }

            @Override
            @NotNull
            public FileVisitResult postVisitDirectory(@NotNull Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return result();
            }
        }
    }

    @CodeHistory(date = "2026/9/21")
    public static class HeapTask extends DeleteDirectory implements HeapRunnable {

        public HeapTask(@NotNull EventExecutor executor, @NotNull Path start) {
            super(executor.newPromise(), start);
        }

        @Override
        public boolean run(boolean canContinue) {
            return false;
        }
    }
}
