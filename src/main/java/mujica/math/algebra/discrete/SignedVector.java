package mujica.math.algebra.discrete;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

@CodeHistory(date = "2025/3/1")
public class SignedVector extends DimensionCodecModifier {

    public SignedVector(DimensionCodec codec) {
        super(codec);
        if (codec.vectorSigned()) {
            throw new IllegalArgumentException("Inner codec has signed vector");
        }
    }

    @Override
    public boolean vectorSigned() {
        return true;
    }

    @Override
    public long encode2(@NotNull int[] in) {
        for (int i = 0; i < 2; i++) {
            in[i] = BitInterleave.signedToUnsigned(in[i]);
        }
        return codec.encode2(in);
    }

    @Override
    public void decode2(long code, @NotNull int[] out) {
        codec.decode2(code, out);
        for (int i = 0; i < 2; i++) {
            out[i] = BitInterleave.unsignedToSigned(out[i]);
        }
    }

    @Override
    public int encode4(@NotNull byte[] in) {
        for (int i = 0; i < 4; i++) {
            in[i] = BitInterleave.signedToUnsigned(in[i]);
        }
        return codec.encode4(in);
    }

    @Override
    public void decode4(int code, @NotNull byte[] out) {
        codec.decode4(code, out);
        for (int i = 0; i < 4; i++) {
            out[i] = BitInterleave.unsignedToSigned(out[i]);
        }
    }

    @Override
    public long encode8(@NotNull byte[] in) {
        for (int i = 0; i < 8; i++) {
            in[i] = BitInterleave.signedToUnsigned(in[i]);
        }
        return codec.encode8(in);
    }

    @Override
    public void decode8(long code, @NotNull byte[] out) {
        codec.decode8(code, out);
        for (int i = 0; i < 8; i++) {
            out[i] = BitInterleave.unsignedToSigned(out[i]);
        }
    }

    @NotNull
    @Override
    public BigInteger encodeN(@NotNull BigInteger[] in) {
        final int size = in.length;
        for (int i = 0; i < size; i++) {
            in[i] = BitInterleave.signedToUnsigned(in[i]);
        }
        return codec.encodeN(in);
    }

    @Override
    public void decodeN(@NotNull BigInteger code, @NotNull BigInteger[] out) {
        codec.decodeN(code, out);
        final int size = out.length;
        for (int i = 0; i < size; i++) {
            out[i] = BitInterleave.unsignedToSigned(out[i]);
        }
    }
}
