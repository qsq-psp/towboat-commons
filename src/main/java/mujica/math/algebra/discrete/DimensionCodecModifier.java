package mujica.math.algebra.discrete;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

/**
 * the base modifier class remains unchanged (identity transform)
 */
@CodeHistory(date = "2025/3/1")
public class DimensionCodecModifier implements DimensionCodec {

    @NotNull
    public final DimensionCodec codec;

    public DimensionCodecModifier(@NotNull DimensionCodec codec) {
        super();
        this.codec = codec;
    }

    @Override
    public boolean vectorSigned() {
        return codec.vectorSigned();
    }

    @Override
    public boolean codeSigned() {
        return codec.codeSigned();
    }

    @Override
    public long encode2(@NotNull int[] in) {
        return codec.encode2(in);
    }

    @Override
    public void decode2(long in, @NotNull int[] out) {
        codec.decode2(in, out);
    }

    @Override
    public int encode4(@NotNull byte[] in) {
        return codec.encode4(in);
    }

    @Override
    public void decode4(int in, @NotNull byte[] out) {
        codec.decode4(in, out);
    }

    @Override
    public long encode8(@NotNull byte[] in) {
        return codec.encode8(in);
    }

    @Override
    public void decode8(long in, @NotNull byte[] out) {
        codec.decode8(in, out);
    }

    @NotNull
    @Override
    public BigInteger encodeN(@NotNull BigInteger[] in) {
        return codec.encodeN(in);
    }

    @Override
    public void decodeN(@NotNull BigInteger in, @NotNull BigInteger[] out) {
        codec.decodeN(in, out);
    }
}
