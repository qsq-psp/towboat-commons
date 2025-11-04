package mujica.io.hash;

import mujica.io.view.ByteFillPolicy;
import mujica.io.view.DataView;
import mujica.io.view.IntDataView;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.function.IntSupplier;

@CodeHistory(date = "2025/5/17")
@ReferencePage(title = "xxHash", href = "https://cyan4973.github.io/xxHash/")
public class XxHash32 extends ByteBlockByteHashCore {

    private static final long serialVersionUID = 0x31b9a8190e170264L;

    private static final int PRIME1 = 0x9e3779b1;
    private static final int PRIME2 = 0x85ebca77;
    private static final int PRIME3 = 0xc2b2ae3d;
    private static final int PRIME4 = 0x27d4eb2f;
    private static final int PRIME5 = 0x165667b1;

    private static final int[] ROTATE_SHIFT = {1, 7, 12, 18};

    @NotNull
    private final int[] state = new int[4];

    private final int seed;

    private int length;

    private int result;

    public XxHash32(int seed) {
        super();
        this.seed = seed;
    }

    public XxHash32() {
        this(0);
    }

    @Override
    public int blockBytes() {
        return 16;
    }

    @Override
    public int resultBytes() {
        return 4;
    }

    @NotNull
    @Override
    public XxHash32 clone() {
        final XxHash32 that = new XxHash32(seed);
        cloneArray(this.state, that.state);
        that.length = this.length;
        that.result = this.result;
        return that;
    }

    @Override
    public void clear() {
        Arrays.fill(state, 0);
        length = 0;
        result = 0;
    }

    @Override
    public void start() {
        state[0] = seed + PRIME1 + PRIME2;
        state[1] = seed + PRIME2;
        state[2] = seed;
        state[3] = seed - PRIME1;
        length = 0;
    }

    @Override
    public void step(@NotNull ByteBuffer buffer) {
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i < 4; i++) {
            state[i] = Integer.rotateLeft(state[i] + buffer.getInt() * PRIME2, 13) * PRIME1;
        }
        length += 16;
    }

    @Override
    public void finish(@NotNull ByteBuffer buffer) {
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        if (length != 0) {
            result = 0;
            for (int i = 0; i < 4; i++) {
                result += Integer.rotateLeft(state[i], ROTATE_SHIFT[i]);
            }
        } else {
            result = seed + PRIME5;
        }
        length += buffer.remaining();
        result += length;
        while (buffer.remaining() >= 4) {
            result = Integer.rotateLeft(result + buffer.getInt() * PRIME3, 17) * PRIME4;
        }
        while (buffer.hasRemaining()) {
            result = Integer.rotateLeft(result + (0xff & buffer.get()) * PRIME5, 11) * PRIME1;
        }
        result ^= result >>> 15;
        result *= PRIME2;
        result ^= result >>> 13;
        result *= PRIME3;
        result ^= result >>> 16;
    }

    @NotNull
    @Override
    public DataView getDataView(@NotNull Runnable guard) {
        return new IntDataView(() -> {
            guard.run();
            return result;
        }, ByteFillPolicy.MIDDLE_TO_RIGHT, Integer.SIZE);
    }
}
