package mujica.io.fs;

import io.netty.util.concurrent.Promise;
import mujica.netty.concurrent.HeapRunnable;
import mujica.netty.concurrent.StackRunnable;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.function.BooleanSupplier;

@CodeHistory(date = "2026/9/29")
public abstract class CopyDirectory {

    @NotNull
    final Promise<Void> promise;

    @NotNull
    final Path srcStart, dstStart;


    protected CopyDirectory(@NotNull Promise<Void> promise, @NotNull Path srcStart, @NotNull Path dstStart) {
        super();
        this.promise = promise;
        this.srcStart = srcStart;
        this.dstStart = dstStart;
    }

    @CodeHistory(date = "2026/9/29")
    public static class StackTask extends CopyDirectory implements StackRunnable {

        public StackTask(@NotNull Promise<Void> promise, @NotNull Path srcStart, @NotNull Path dstStart) {
            super(promise, srcStart, dstStart);
        }

        @Override
        public void run(@NotNull BooleanSupplier yield) {

        }
    }

    @CodeHistory(date = "2026/9/29")
    public static class HeapTask extends CopyDirectory implements HeapRunnable {

        public HeapTask(@NotNull Promise<Void> promise, @NotNull Path srcStart, @NotNull Path dstStart) {
            super(promise, srcStart, dstStart);
        }

        @Override
        public boolean run(boolean canContinue) {
            return false;
        }
    }
}
