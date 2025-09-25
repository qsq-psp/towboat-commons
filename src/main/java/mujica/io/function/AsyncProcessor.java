package mujica.io.function;

import mujica.reflect.modifier.CodeHistory;

/**
 * Created on 2025/4/9.
 */
@CodeHistory(date = "2025/4/9")
public interface AsyncProcessor<R> {

    /**
     * Clear all initial state, erasing any sensitive data from memory.
     */
    void clear();

    /**
     * Reset the internal state to initial state, ready to accept input. Sensitive data may not be erased.
     */
    void start();

    /**
     * The inputs ended and return the result. Or get result by calling other get method.
     */
    R finish();
}
