package mujica.netty.concurrent;

import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SingleThreadEventLoop;
import io.netty.util.concurrent.DefaultThreadFactory;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;

@CodeHistory(date = "2022/7/28", project = "infrastructure")
@CodeHistory(date = "2025/12/26")
public class ReferenceQueueEventLoop<T> extends SingleThreadEventLoop {

    private final ReferenceQueue<T> queue = new ReferenceQueue<>();

    @NotNull
    private final Consumer<Reference<? extends T>> consumer;

    public ReferenceQueueEventLoop() {
        this(null, new DefaultThreadFactory(DefaultEventLoop.class), ignore -> {});
    }

    public ReferenceQueueEventLoop(@NotNull Consumer<Reference<? extends T>> consumer) {
        this(null, new DefaultThreadFactory(DefaultEventLoop.class), consumer);
    }

    public ReferenceQueueEventLoop(@NotNull ThreadFactory threadFactory) {
        this(null, threadFactory, ignore -> {});
    }

    public ReferenceQueueEventLoop(@Nullable EventLoopGroup parent) {
        this(parent, new DefaultThreadFactory(DefaultEventLoop.class), ignore -> {});
    }

    public ReferenceQueueEventLoop(@NotNull Executor executor) {
        this(null, executor, ignore -> {});
    }

    public ReferenceQueueEventLoop(@Nullable EventLoopGroup parent, @NotNull ThreadFactory threadFactory, @NotNull Consumer<Reference<? extends T>> consumer) {
        super(parent, threadFactory, true);
        this.consumer = consumer;
    }

    public ReferenceQueueEventLoop(@Nullable EventLoopGroup parent, @NotNull Executor executor, @NotNull Consumer<Reference<? extends T>> consumer) {
        super(parent, executor, true);
        this.consumer = consumer;
    }

    @Override
    protected void wakeup(boolean inEventLoop) {
        if (!inEventLoop) {
            (new WakeupSignal<>(null, queue)).enqueue();
        }
    }

    @Override
    protected void run() {
        for (;;) {
            // ...
        }
    }
}
