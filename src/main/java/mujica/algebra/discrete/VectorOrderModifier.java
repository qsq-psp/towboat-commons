package mujica.algebra.discrete;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Arrays;

@CodeHistory(date = "2025/3/1", name = "VectorExchangeModifier")
@CodeHistory(date = "2026/2/24")
public abstract class VectorOrderModifier extends DimensionCodecModifier {

    public VectorOrderModifier(@NotNull DimensionCodec codec) {
        super(codec);
    }

    protected abstract void modify2(@NotNull int[] vector);

    @Override
    public long encode2(@NotNull int[] in) {
        modify2(in);
        return codec.encode2(in);
    }

    @Override
    public void decode2(long in, @NotNull int[] out) {
        codec.decode2(in, out);
        modify2(out);
    }

    protected abstract void modifyN(@NotNull BigInteger[] vector, boolean isDecode);

    @NotNull
    @Override
    public BigInteger encodeN(@NotNull BigInteger[] in) {
        modifyN(in, false);
        return codec.encodeN(in);
    }

    @Override
    public void decodeN(@NotNull BigInteger in, @NotNull BigInteger[] out) {
        codec.decodeN(in, out);
        modifyN(out, true);
    }

    @CodeHistory(date = "2025/3/1", name = "ReverseEndian")
    @CodeHistory(date = "2026/2/24", name = "ReverseModifier")
    @CodeHistory(date = "2026/4/20")
    public static class ReverseModifier extends VectorOrderModifier {

        public ReverseModifier(@NotNull DimensionCodec codec) {
            super(codec);
        }

        @Override
        protected void modify2(@NotNull int[] vector) {
            final int value = vector[0];
            vector[0] = vector[1];
            vector[1] = value;
        }

        @Override
        protected void modifyN(@NotNull BigInteger[] vector, boolean isDecode) {
            int low = 0;
            int high = vector.length - 1;
            while (low < high) {
                BigInteger value = vector[low];
                vector[low] = vector[high];
                vector[high] = value;
                low++;
                high--;
            }
        }
    }

    @CodeHistory(date = "2025/12/20", name = "RotateModifier")
    @CodeHistory(date = "2026/4/20")
    public static class RotateModifier extends VectorOrderModifier {

        final int move;

        public RotateModifier(@NotNull DimensionCodec codec, int move) {
            super(codec);
            this.move = move;
        }

        @Override
        protected void modify2(@NotNull int[] vector) {
            if ((move & 1) == 0) {
                return;
            }
            final int value = vector[0];
            vector[0] = vector[1];
            vector[1] = value;
        }

        @Override
        protected void modifyN(@NotNull BigInteger[] vector, boolean isDecode) {
            final int length = vector.length;
            if (length < 2) {
                return;
            }
            int positiveMove = (length + move) % length;
            if (positiveMove < 0) {
                positiveMove += length;
            }
            if (isDecode) {
                positiveMove = length - positiveMove;
            }
            final BigInteger[] temp = Arrays.copyOf(vector, length);
            for (int index = 0; index < length; index++) {
                vector[(index + positiveMove) % length] = temp[index];
            }
        }
    }
}
