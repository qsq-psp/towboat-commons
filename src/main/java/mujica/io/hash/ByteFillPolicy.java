package mujica.io.hash;

import java.nio.ByteOrder;

/**
 * Created on 2025/5/14.
 */
public enum ByteFillPolicy {

    LEFT_TO_MIDDLE(ByteAlign.LEFT, ByteOrder.BIG_ENDIAN),
    MIDDLE_TO_LEFT(ByteAlign.LEFT, ByteOrder.LITTLE_ENDIAN),
    RIGHT_TO_MIDDLE(ByteAlign.RIGHT, ByteOrder.LITTLE_ENDIAN),
    MIDDLE_TO_RIGHT(ByteAlign.RIGHT, ByteOrder.BIG_ENDIAN);

    private static final long serialVersionUID = 0xb1aff47b7b03bf34L;

    public final ByteAlign align;

    public final ByteOrder order; // {@code ByteOrder} is not serializable

    ByteFillPolicy(ByteAlign align, ByteOrder order) {
        this.align = align;
        this.order = order;
    }
}
