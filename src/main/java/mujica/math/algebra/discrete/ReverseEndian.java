package mujica.math.algebra.discrete;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

@CodeHistory(date = "2025/3/1")
public class ReverseEndian extends VectorExchangeModifier {

    public ReverseEndian(@NotNull DimensionCodec codec) {
        super(codec);
    }

    private void reverse2(@NotNull int[] vector) {
        final int value = vector[0];
        vector[0] = vector[1];
        vector[1] = value;
    }

    @Override
    public long encode2(@NotNull int[] in) {
        reverse2(in);
        return codec.encode2(in);
    }

    @Override
    public void decode2(long code, @NotNull int[] out) {
        codec.decode2(code, out);
        reverse2(out);
    }

    private void reverse(@NotNull byte[] vector, int size) {
        int low = 0;
        int high = size - 1;
        while (low < high) {
            byte value = vector[low];
            vector[low] = vector[high];
            vector[high] = value;
            low++;
            high--;
        }
    }

    @Override
    public int encode4(@NotNull byte[] in) {
        reverse(in, 4);
        return codec.encode4(in);
    }

    @Override
    public void decode4(int code, @NotNull byte[] out) {
        codec.decode4(code, out);
        reverse(out, 4);
    }

    @Override
    public long encode8(@NotNull byte[] in) {
        reverse(in, 8);
        return codec.encode8(in);
    }

    @Override
    public void decode8(long code, @NotNull byte[] out) {
        codec.decode8(code, out);
        reverse(out, 8);
    }

    private void reverse(@NotNull BigInteger[] vector) {
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

    @NotNull
    @Override
    public BigInteger encodeN(@NotNull BigInteger[] in) {
        reverse(in);
        return codec.encodeN(in);
    }

    @Override
    public void decodeN(@NotNull BigInteger code, @NotNull BigInteger[] out) {
        codec.decodeN(code, out);
        reverse(out);
    }
}
