package mujica.netty.concurrent;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.channels.Selector;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@CodeHistory(date = "2026/9/22", project = "nettyon", name = "ColorfulSleep")
@CodeHistory(date = "2026/9/24", project = "nettyon", name = "ColorfulSleep")
@CodeHistory(date = "2026/10/5")
public enum Sleep implements SleepProvider {

    THREAD_SLEEP("Thread.sleep()", false,
            Thread.State.TIMED_WAITING, VisualVmState.SLEEPING, OshiState.SLEEPING,
            TimeUnit.MILLISECONDS) {
        @NotNull
        @Override
        public SleepRunnable create() {
            return (executor, duration, unit) -> Thread.sleep(unit.toMillis(duration));
        }
    },
    @Deprecated
    THREAD_SUSPEND("Thread.suspend()", true,
            Thread.State.WAITING, VisualVmState.WAIT, OshiState.SUSPENDED,
            TimeUnit.MILLISECONDS) {
        @NotNull
        @Override
        public SleepRunnable create() {
            final Semaphore semaphore = new Semaphore(0);
            return (executor, duration, unit) -> {
                final Thread thread = Thread.currentThread();
                executor.execute(() -> {
                    thread.suspend();
                    semaphore.release();
                    executor.schedule(thread::resume, duration, unit);
                });
                semaphore.acquire(); // the SleepRunnable do not return until resume
            };
        }
    },
    OBJECT_WAIT_TIME("Object.wait(time)", false,
            Thread.State.TIMED_WAITING, VisualVmState.WAIT, OshiState.WAITING,
            TimeUnit.MILLISECONDS) {
        @NotNull
        @Override
        public SleepRunnable create() {
            return (executor, duration, unit) -> {
                synchronized (this) {
                    wait(unit.toMillis(duration));
                }
            };
        }
    },
    OBJECT_WAIT("Object.wait()", false,
            Thread.State.WAITING, VisualVmState.WAIT, OshiState.WAITING,
            TimeUnit.NANOSECONDS) {
        @NotNull
        @Override
        public SleepRunnable create() {
            final Semaphore semaphore = new Semaphore(0);
            return (executor, duration, unit) -> {
                final Object lock = this;
                executor.schedule(() -> {
                    try {
                        semaphore.acquire();
                        synchronized (lock) {
                            lock.notify();
                        }
                    } catch (InterruptedException ignore) {
                    }
                }, duration, unit);
                synchronized (lock) {
                    semaphore.release();
                    lock.wait();
                }
            };
        }
    },
    UNSAFE_PARK_TIME("Unsafe.park(time)", false,
             Thread.State.TIMED_WAITING, VisualVmState.PARK, OshiState.WAITING,
             TimeUnit.NANOSECONDS) {
        @NotNull
        @Override
        public SleepRunnable create() {
            final Semaphore semaphore = new Semaphore(0);
            return (executor, duration, unit) -> semaphore.tryAcquire(duration, unit);
        }
    },
    UNSAFE_PARK("Unsafe.park()", false,
             Thread.State.WAITING, VisualVmState.PARK, OshiState.WAITING,
             TimeUnit.NANOSECONDS) {
        @NotNull
        @Override
        public SleepRunnable create() {
            final Semaphore semaphore = new Semaphore(0);
            return (executor, duration, unit) -> {
                executor.schedule((Runnable) semaphore::release, duration, unit);
                semaphore.acquire();
            };
        }
    },
    SYNCHRONIZED("synchronized()", false,
             Thread.State.BLOCKED, VisualVmState.MONITOR, OshiState.WAITING,
             TimeUnit.MILLISECONDS) {
        @NotNull
        @Override
        public SleepRunnable create() {
            final Semaphore semaphore = new Semaphore(0);
            return (executor, duration, unit) -> {
                final Object lock = this;
                executor.execute(() -> {
                    try {
                        synchronized (lock) {
                            semaphore.release();
                            Thread.sleep(unit.toMillis(duration)); // sleep while holding the lock, can not replace with executor.schedule(time)
                        }
                    } catch (InterruptedException ignore) {}
                });
                semaphore.acquire();
                synchronized (lock) { // blocked here
                    lock.notify(); // nothing happened, just do not leave the synchronized code block empty
                }
            };
        }
    },
    SELECTOR_SELECT_TIME("Selector.select(time)", false,
            Thread.State.RUNNABLE, VisualVmState.RUNNING, OshiState.RUNNING,
            TimeUnit.MILLISECONDS) {
        @NotNull
        @Override
        public SleepRunnable create() {
            try {
                return new CloseableSleepRunnable() {
                    final Selector selector = Selector.open();
                    @Override
                    public void run(ScheduledExecutorService executor, long duration, @NotNull TimeUnit unit) {
                        try {
                            selector.select(unit.toMillis(duration));
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    }
                    @Override
                    public void close() throws IOException {
                        selector.close();
                    }
                };
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    },
    SELECTOR_SELECT("Selector.select()", false,
            Thread.State.RUNNABLE, VisualVmState.RUNNING, OshiState.RUNNING,
            TimeUnit.NANOSECONDS) {
        @NotNull
        @Override
        public SleepRunnable create() {
            try {
                return new CloseableSleepRunnable() {
                    final Selector selector = Selector.open();
                    @Override
                    public void run(ScheduledExecutorService executor, long duration, @NotNull TimeUnit unit) {
                        executor.schedule((Runnable) selector::wakeup, duration, unit);
                        try {
                            selector.select();
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    }
                    @Override
                    public void close() throws IOException {
                        selector.close();
                    }
                };
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    };

    private static final long serialVersionUID = 0xE7213E1AF4E650B0L;

    @NotNull
    final String sleepName;

    final boolean deprecated;

    @NotNull
    final Thread.State threadState;

    @NotNull
    final VisualVmState visualVmState;

    @NotNull
    final OshiState oshiState;

    @NotNull
    final TimeUnit timeAccuracy;

    Sleep(@NotNull String sleepName, boolean deprecated,
          @NotNull Thread.State threadState, @NotNull VisualVmState visualVmState, @NotNull OshiState oshiState,
          @NotNull TimeUnit timeAccuracy) {
        this.sleepName = sleepName;
        this.deprecated = deprecated;
        this.threadState = threadState;
        this.visualVmState = visualVmState;
        this.oshiState = oshiState;
        this.timeAccuracy = timeAccuracy;
    }

    @NotNull
    @Override
    public String sleepName() {
        return sleepName;
    }

    @Override
    public boolean isDeprecated() {
        return deprecated;
    }

    @Nullable
    @Override
    public Thread.State threadState() {
        return threadState;
    }

    @Nullable
    @Override
    public VisualVmState visualVmState() {
        return visualVmState;
    }

    @Nullable
    @Override
    public OshiState oshiState() {
        return oshiState;
    }

    @Nullable
    @Override
    public TimeUnit timeAccuracy() {
        return timeAccuracy;
    }

    @NotNull
    @Override
    public abstract SleepRunnable create();
}
