package mujica.io.hash;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/5/14.
 * Not an enum, same as {@code ByteOrder}
 */
@CodeHistory(date = "2025/5/14")
public final class ByteAlign {

    private final String side;

    private ByteAlign(@NotNull String side) {
        super();
        this.side = side;
    }

    public static final ByteAlign LEFT = new ByteAlign("LEFT");

    public static final ByteAlign RIGHT = new ByteAlign("RIGHT");

    public static final ByteAlign MSB = LEFT;

    public static final ByteAlign LSB = RIGHT;

    @Override
    public String toString() {
        return side;
    }
}
