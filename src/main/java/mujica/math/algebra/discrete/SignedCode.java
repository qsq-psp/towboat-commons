package mujica.math.algebra.discrete;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

@CodeHistory(date = "2025/3/1")
public class SignedCode extends DimensionCodecModifier {

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
    public void decode2(long code, @NotNull int[] out) {
        codec.decode2(BitInterleave.signedToUnsigned(code), out);
    }

    @Override
    public int encode4(@NotNull byte[] in) {
        return BitInterleave.unsignedToSigned(codec.encode4(in));
    }

    @Override
    public void decode4(int code, @NotNull byte[] out) {
        codec.decode4(BitInterleave.signedToUnsigned(code), out);
    }

    @Override
    public long encode8(@NotNull byte[] in) {
        return BitInterleave.unsignedToSigned(codec.encode8(in));
    }

    @Override
    public void decode8(long code, @NotNull byte[] out) {
        codec.decode8(BitInterleave.signedToUnsigned(code), out);
    }

    @NotNull
    @Override
    public BigInteger encodeN(@NotNull BigInteger[] in) {
        return BitInterleave.unsignedToSigned(codec.encodeN(in));
    }

    @Override
    public void decodeN(@NotNull BigInteger code, @NotNull BigInteger[] out) {
        codec.decodeN(BitInterleave.signedToUnsigned(code), out);
    }
}
