package mujica.io.hash;

import mujica.io.view.ByteFillPolicy;
import mujica.io.view.DataView;
import mujica.io.view.LongDataView;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.nio.ByteOrder;
import java.util.function.LongSupplier;

/**
 * Created on 2025/4/27.
 */
@CodeHistory(date = "2025/4/27")
public class GeneralLongSizedCRC extends EachBitStreamHash implements LongSupplier, Serializable {

    @NotNull
    public final CrcSpec spec;

    private long state;

    public GeneralLongSizedCRC(@NotNull CrcSpec spec) {
        super();
        this.spec = spec;
    }

    @NotNull
    @Override
    public ByteOrder bitOrder() {
        if (spec.reflectIn) {
            return ByteOrder.LITTLE_ENDIAN;
        } else {
            return ByteOrder.BIG_ENDIAN;
        }
    }

    @NotNull
    @Override
    public GeneralLongSizedCRC clone() {
        final GeneralLongSizedCRC that = new GeneralLongSizedCRC(spec);
        that.state = this.state;
        return that;
    }

    @Override
    public void clear() {
        state = 0L;
    }

    @Override
    public void start() {
        state = spec.initialState;
    }

    @Override
    public void update(boolean input) {
        if ((state & (1L << (spec.bitLength - 1))) != 0) {
            input = !input;
        }
        state <<= 1;
        if (input) {
            state ^= spec.polynomial;
        }
    }

    @NotNull
    @Override
    public DataView finish() {
        return new LongDataView(getAsLong(), ByteFillPolicy.RIGHT_TO_MIDDLE, spec.bitLength);
    }

    @Override
    public long getAsLong() {
        long result = state;
        if (spec.reflectOut) {
            result = Long.reverse(result) >> (Long.SIZE - spec.bitLength);
        }
        result ^= spec.finalFlip;
        result &= (1L << spec.bitLength) - 1L;
        return result;
    }
}
