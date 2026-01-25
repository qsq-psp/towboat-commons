package mujica.netty.concurrent;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

@CodeHistory(date = "2019/2/9", project = "SimpleStack", name = "SimpleStack")
@CodeHistory(date = "2024/6/12", project = "Ultramarine")
@CodeHistory(date = "2025/12/27")
public class JavaMethodStack<E> implements Runnable {

    static final int STATE_INTERRUPTED = -1;
    static final int STATE_NEW = 0;
    static final int STATE_IDLE = 1;
    static final int STATE_PUSH_REQUEST = 2;
    static final int STATE_PUSH_RESPONSE_OK = 3;
    static final int STATE_PUSH_RESPONSE_FULL = 4;
    static final int STATE_POP_REQUEST = 5;
    static final int STATE_POP_RESPONSE_OK = 6;
    static final int STATE_POP_RESPONSE_EMPTY = 7;
    static final int STATE_PEEK_REQUEST = 8;
    static final int STATE_PEEK_RESPONSE_OK = 9;
    static final int STATE_PEEK_RESPONSE_EMPTY = 10;

    final AtomicInteger state = new AtomicInteger(STATE_NEW);

    volatile E transfer;

    private JavaMethodStack() {
        super();
    }

    @NotNull
    public static<E> JavaMethodStack<E> onNewThread() {
        final JavaMethodStack<E> stack = new JavaMethodStack<>();
        final Thread thread = new Thread(stack, JavaMethodStack.class.getSimpleName());
        thread.setDaemon(true);
        thread.start();
        while (stack.state.get() == STATE_NEW) {
            Thread.yield();
        }
        return stack;
    }

    @Override
    public void run() {
        if (!state.compareAndSet(STATE_NEW, STATE_IDLE)) {
            throw new RuntimeException("can only run once");
        }
        try {
            synchronized (this) {
                LOOP:
                while (true) {
                    wait();
                    switch (state.get()) {
                        case STATE_PUSH_REQUEST:
                            frame(transfer);
                            break;
                        case STATE_POP_REQUEST:
                            state.set(STATE_POP_RESPONSE_EMPTY);
                            break;
                        case STATE_PEEK_REQUEST:
                            state.set(STATE_PEEK_RESPONSE_EMPTY);
                            break;
                        case STATE_INTERRUPTED: // never
                            break LOOP;
                    }
                }
            }
        } catch (InterruptedException ignore) {} finally {
            state.set(STATE_INTERRUPTED);
        }
    }

    private void frame(E value) throws InterruptedException {
        state.set(STATE_PUSH_RESPONSE_OK);
        LOOP:
        while (true) {
            wait();
            switch (state.get()) {
                case STATE_PUSH_REQUEST:
                    try {
                        frame(transfer);
                    } catch (StackOverflowError e) {
                        state.set(STATE_PUSH_RESPONSE_FULL);
                    }
                    break;
                case STATE_POP_REQUEST:
                    transfer = value;
                    state.set(STATE_POP_RESPONSE_OK);
                    break LOOP;
                case STATE_PEEK_REQUEST:
                    transfer = value;
                    state.set(STATE_PEEK_RESPONSE_OK);
                    break;
                case STATE_INTERRUPTED: // never
                    break LOOP;
            }
        }
    }

    private void checkAlive() throws RuntimeException {
        final int result = state.get();
        if (result > 0) {
            return;
        }
        String message;
        if (result == STATE_INTERRUPTED) {
            message = "interrupted";
        } else if (result == STATE_NEW) {
            message = "not started";
        } else {
            message = "unknown";
        }
        throw new RuntimeException(message);
    }

    public void push(E value) throws RuntimeException {
        while (!state.compareAndSet(STATE_IDLE, STATE_PUSH_REQUEST)) {
            checkAlive();
            Thread.yield();
        }
        transfer = value;
        while (true) {
            synchronized (this) {
                notify();
            }
            int result = state.get();
            if (result == STATE_PUSH_REQUEST) {
                Thread.yield();
                continue;
            } else if (result == STATE_PUSH_RESPONSE_OK) {
                if (state.compareAndSet(result, STATE_IDLE)) {
                    break;
                }
            } else if (result == STATE_PUSH_RESPONSE_FULL) {
                if (state.compareAndSet(result, STATE_IDLE)) {
                    throw new NoSuchElementException(); // replace with StackOverflowException later
                }
            }
            throw new IllegalStateException("unexpected state change");
        }
    }

    private E popOrPeek(int request) throws RuntimeException {
        while (!state.compareAndSet(STATE_IDLE, request)) {
            checkAlive();
            Thread.yield();
        }
        while (true) {
            synchronized (this) {
                notify();
            }
            int result = state.get();
            if (result == request) {
                Thread.yield();
                continue;
            } else if (result == request + 1) { // OK
                break;
            } else if (result == request + 2) {
                if (state.compareAndSet(result, STATE_IDLE)) {
                    throw new NoSuchElementException();
                }
            }
            throw new IllegalStateException("unexpected state change");
        }
        final E value = transfer;
        if (!state.compareAndSet(request + 1, STATE_IDLE)) {
            throw new IllegalStateException("unexpected state change");
        }
        return value;
    }

    public E pop() throws RuntimeException {
        return popOrPeek(STATE_POP_REQUEST);
    }

    public E peek() throws RuntimeException {
        return popOrPeek(STATE_PEEK_REQUEST);
    }
}
