package mujica.algebra.discrete;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

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

    @NotNull
    @Override
    public BigInteger encodeN(@NotNull BigInteger[] in) {
        if (in.length != 2) {
            throw new UnsupportedOperationException();
        }
        return null;
    }

    @Override
    public void decodeN(@NotNull BigInteger in, @NotNull BigInteger[] out) {

    }
}
