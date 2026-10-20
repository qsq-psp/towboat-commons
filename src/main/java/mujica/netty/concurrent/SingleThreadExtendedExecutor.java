package mujica.netty.concurrent;

import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.RejectedExecutionHandler;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

@CodeHistory(date = "2026/9/28")
public class SingleThreadExtendedExecutor extends SingleThreadEventExecutor implements ExtendedExecutor {

    final Queue<StackRunnable> stackTaskQueue = new ArrayDeque<>();

    protected SingleThreadExtendedExecutor(EventExecutorGroup parent, ThreadFactory threadFactory, boolean addTaskWakesUp) {
        super(parent, threadFactory, addTaskWakesUp);
    }

    protected SingleThreadExtendedExecutor(EventExecutorGroup parent, ThreadFactory threadFactory, boolean addTaskWakesUp, int maxPendingTasks, RejectedExecutionHandler rejectedHandler) {
        super(parent, threadFactory, addTaskWakesUp, maxPendingTasks, rejectedHandler);
    }

    protected SingleThreadExtendedExecutor(EventExecutorGroup parent, Executor executor, boolean addTaskWakesUp) {
        super(parent, executor, addTaskWakesUp);
    }

    protected SingleThreadExtendedExecutor(EventExecutorGroup parent, Executor executor, boolean addTaskWakesUp, int maxPendingTasks, RejectedExecutionHandler rejectedHandler) {
        super(parent, executor, addTaskWakesUp, maxPendingTasks, rejectedHandler);
    }

    protected SingleThreadExtendedExecutor(EventExecutorGroup parent, Executor executor, boolean addTaskWakesUp, Queue<Runnable> taskQueue, RejectedExecutionHandler rejectedHandler) {
        super(parent, executor, addTaskWakesUp, taskQueue, rejectedHandler);
    }

    @Override
    public void execute(@NotNull Runnable task) {
        super.execute(task);
    }

    @CodeHistory(date = "2026/10/4")
    class BoundHeapRunnable implements Runnable {

        @NotNull
        final HeapRunnable heapRunnable;

        BoundHeapRunnable(@NotNull HeapRunnable heapRunnable) {
            super();
            this.heapRunnable = heapRunnable;
        }

        @Override
        public void run() {
            if (heapRunnable.run(true)) {
                execute(this);
            }
        }
    }

    @Override
    public void execute(@NotNull HeapRunnable task) {
        execute(new BoundHeapRunnable(task));
    }

    @Override
    public void execute(@NotNull StackRunnable task) {
        stackTaskQueue.offer(task);
    }

    @Override
    protected void run() {
        //
    }
}
