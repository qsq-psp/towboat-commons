package mujica.io.hash;

import mujica.io.view.*;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.nio.ByteOrder;
import java.util.function.IntSupplier;

@CodeHistory(date = "2024/12/11", name = "BaselineCRC32")
@CodeHistory(date = "2025/4/12")
public class SimpleIntSizedCRC extends EachBitStreamHash implements IntSupplier, Serializable {

    private static final long serialVersionUID = 0xf49818281cf481eeL;

    @NotNull
    public static SimpleIntSizedCRC crc32() {
        return new SimpleIntSizedCRC(0xedb88320);
    }

    @NotNull
    public static SimpleIntSizedCRC crc32C() {
        return new SimpleIntSizedCRC(0x82f63b78);
    }

    private int remainder;

    private final int polynomial;

    public SimpleIntSizedCRC(int polynomial) {
        super();
        this.polynomial = polynomial;
    }

    @NotNull
    @Override
    public ByteOrder bitOrder() {
        return ByteOrder.LITTLE_ENDIAN;
    }

    @NotNull
    @Override
    public SimpleIntSizedCRC clone() {
        final SimpleIntSizedCRC that = new SimpleIntSizedCRC(polynomial);
        that.remainder = this.remainder;
        return that;
    }

    @Override
    public void clear() {
        remainder = 0;
    }

    @Override
    public void start() {
        remainder = -1;
    }

    @Override
    public void update(boolean input) {
        if ((remainder & 0x1) != 0) {
            input = !input;
        }
        remainder >>>= 1;
        if (input) {
            remainder ^= polynomial;
        }
    }

    @NotNull
    @Override
    public DataView finish() {
        return new IntDataView(getAsInt(), ByteFillPolicy.RIGHT_TO_MIDDLE, Integer.SIZE);
    }

    @Override
    public int getAsInt() {
        return ~remainder;
    }
}
