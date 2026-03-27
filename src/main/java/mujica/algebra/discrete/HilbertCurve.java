package mujica.algebra.discrete;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

/**
 * Created on 2026/3/22.
 */
@CodeHistory(date = "2026/3/22")
public class HilbertCurve implements DimensionCodec {

    @Override
    public boolean vectorSigned() {
        return false;
    }

    @Override
    public boolean codeSigned() {
        return false;
    }

    @Override
    public long encode2(@NotNull int[] in) {
        final int x = in[0];
        final int y = in[1];
        return 0L;
    }

    @Override
    public void decode2(long in, @NotNull int[] out) {

    }

    @Override
    public int encode4(@NotNull byte[] in) {
        return 0;
    }

    @Override
    public void decode4(int in, @NotNull byte[] out) {

    }

    @Override
    public long encode8(@NotNull byte[] in) {
        return 0;
    }

    @Override
    public void decode8(long in, @NotNull byte[] out) {

    }

    @NotNull
    @Override
    public BigInteger encodeN(@NotNull BigInteger[] in) {
        return null;
    }

    @Override
    public void decodeN(@NotNull BigInteger in, @NotNull BigInteger[] out) {

    }
}
