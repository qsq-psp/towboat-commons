package mujica.io.nest;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/10/9.
 */
@CodeHistory(date = "2025/10/9")
public interface LimitedInput {

    @CodeHistory(date = "2025/10/13")
    enum Behavior {

        END, PAD, THROW;

        private static final long serialVersionUID = 0x419074C2BE92FEC4L;
    }

    @NotNull
    Behavior getBehavior();

    void setBehavior(@NotNull Behavior newBehavior);

    long getRemaining();

    long setRemaining(long newRemaining);
}
