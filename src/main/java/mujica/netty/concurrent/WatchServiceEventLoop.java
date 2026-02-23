package mujica.netty.concurrent;

import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SingleThreadEventLoop;
import io.netty.util.concurrent.DefaultThreadFactory;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@CodeHistory(date = "2026/2/12")
public class WatchServiceEventLoop extends SingleThreadEventLoop {

    private static final Logger LOGGER = LoggerFactory.getLogger(WatchServiceEventLoop.class);

    @NotNull
    private final WatchService watchService;

    @NotNull
    private final Consumer<WatchKey> consumer;

    public WatchServiceEventLoop() throws IOException {
        this(null, new DefaultThreadFactory(DefaultEventLoop.class), FileSystems.getDefault(), ignore -> {});
    }

    public WatchServiceEventLoop(@NotNull Consumer<WatchKey> consumer) throws IOException {
        this(null, new DefaultThreadFactory(DefaultEventLoop.class), FileSystems.getDefault(), consumer);
    }

    public WatchServiceEventLoop(@NotNull ThreadFactory threadFactory) throws IOException {
        this(null, threadFactory, FileSystems.getDefault(), ignore -> {});
    }

    public WatchServiceEventLoop(@Nullable EventLoopGroup parent) throws IOException {
        this(parent, new DefaultThreadFactory(DefaultEventLoop.class), FileSystems.getDefault(), ignore -> {});
    }

    public WatchServiceEventLoop(@NotNull Executor executor) throws IOException {
        this(null, executor, FileSystems.getDefault(), ignore -> {});
    }

    public WatchServiceEventLoop(@Nullable EventLoopGroup parent, @NotNull ThreadFactory threadFactory,
                                 @NotNull FileSystem fileSystem, @NotNull Consumer<WatchKey> consumer) throws IOException {
        super(parent, threadFactory, true);
        this.watchService = fileSystem.newWatchService();
        this.consumer = consumer;
    }

    public WatchServiceEventLoop(@Nullable EventLoopGroup parent, @NotNull Executor executor,
                                 @NotNull FileSystem fileSystem, @NotNull Consumer<WatchKey> consumer) throws IOException {
        super(parent, executor, true);
        this.watchService = fileSystem.newWatchService();
        this.consumer = consumer;
    }

    @NotNull
    public WatchKey register(@NotNull Path path, WatchEvent.Kind<?>... events) throws IOException {
        return path.register(watchService, events);
    }

    @NotNull
    public WatchKey register(@NotNull Path path, WatchEvent.Kind<?>[] events, WatchEvent.Modifier... modifiers) throws IOException {
        return path.register(watchService, events, modifiers);
    }

    @Override
    protected void wakeup(boolean inEventLoop) {
        if (!inEventLoop) {
            interruptThread();
        }
    }

    @Override
    protected void run() {
        while (!confirmShutdown()) {
            WatchKey watchKey = null;
            try {
                long deadline = nextScheduledTaskDeadlineNanos();
                if (deadline == -1L) { // no task is scheduled
                    watchKey = watchService.take(); // block
                } else {
                    deadline = deadlineToDelayNanos(deadline);
                    watchKey = watchService.poll(deadline, TimeUnit.NANOSECONDS);
                }
            } catch (InterruptedException e) {
                LOGGER.debug("interrupted", e);
            }
            if (watchKey != null) {
                consumer.accept(watchKey);
                continue;
            }
            runAllTasks(0);
        }
    }
}
