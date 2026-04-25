package mujica.algebra.discrete;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

@CodeHistory(date = "2025/3/1")
@DirectSubclass({SignedCodeModifier.class, SignedVectorModifier.class, VectorOrderModifier.class})
public class DimensionCodecModifier implements DimensionCodec { // the base modifier class remains unchanged (identity transform)

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
