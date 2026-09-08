package mujica.io.function;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2025/4/9")
public interface AsyncProcessor<R> {

    /**
     * clear all initial state, erasing any sensitive data from memory.
     */
    void clear();

    /**
     * reset the internal state to initial state, ready to accept input. Sensitive data may not be erased.
     */
    void start();

    /**
     * the inputs ended and return the result. Or get result by calling other get method.
     */
    R finish();
}
