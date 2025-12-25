package mujica.math.algebra.discrete;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

@CodeHistory(date = "2025/3/1", name = "SignedCode")
public class SignedCode extends DimensionCodecModifier { // SignedCodeModifier

    public SignedCode(@NotNull DimensionCodec codec) {
        super(codec);
        if (codec.codeSigned()) {
            throw new IllegalArgumentException("Inner codec has signed code");
        }
    }

    @Override
    public boolean codeSigned() {
        return true;
    }

    @Override
    public long encode2(@NotNull int[] in) {
        return BitInterleave.unsignedToSigned(codec.encode2(in));
    }

    @Override
    public void decode2(long in, @NotNull int[] out) {
        codec.decode2(BitInterleave.signedToUnsigned(in), out);
    }

    @Override
    public int encode4(@NotNull byte[] in) {
        return BitInterleave.unsignedToSigned(codec.encode4(in));
    }

    @Override
    public void decode4(int in, @NotNull byte[] out) {
        codec.decode4(BitInterleave.signedToUnsigned(in), out);
    }

    @Override
    public long encode8(@NotNull byte[] in) {
        return BitInterleave.unsignedToSigned(codec.encode8(in));
    }

    @Override
    public void decode8(long in, @NotNull byte[] out) {
        codec.decode8(BitInterleave.signedToUnsigned(in), out);
    }

    @NotNull
    @Override
    public BigInteger encodeN(@NotNull BigInteger[] in) {
        return BitInterleave.unsignedToSigned(codec.encodeN(in));
    }

    @Override
    public void decodeN(@NotNull BigInteger in, @NotNull BigInteger[] out) {
        codec.decodeN(BitInterleave.signedToUnsigned(in), out);
    }
}
