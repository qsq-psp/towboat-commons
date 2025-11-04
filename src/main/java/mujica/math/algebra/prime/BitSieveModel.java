package mujica.math.algebra.prime;

import mujica.math.algebra.discrete.IntegralMath;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;

/**
 * Created on 2025/10/1.
 */
public class BitSieveModel implements Serializable {

    private static final long serialVersionUID = 0x7BE1AA3CFC711235L;

    final int period;

    transient int[] offsets; // its length is the bit size

    transient int byteSize;

    public BitSieveModel(int period) {
        if (period <= 1) {
            throw new IllegalArgumentException();
        }
        this.period = period;
        establish();
    }

    private void readObject(@NotNull ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        establish();
    }

    private void establish() {
        final IntegralMath math = IntegralMath.INSTANCE;
        int[] offsets = new int[period - 1];
        int bitSize = 0;
        for (int number = 1; number < period; number++) {
            if (math.gcd(number, period) == 1) {
                offsets[bitSize++] = number;
            }
        }
        if (bitSize < period - 1) {
            offsets = Arrays.copyOf(offsets, bitSize);
        }
        this.offsets = offsets;
        this.byteSize = (bitSize + 0x7) >> 3; // ceil divide 8
    }

    public float efficiency() {
        return (period - offsets.length) / (float) period;
    }

    @Override
    public int hashCode() {
        return period;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BitSieveModel && this.period == ((BitSieveModel) obj).period;
    }

    @Override
    public String toString() {
        return "BitSieveModel[period = " + period + ", offsets(" + offsets.length + ") = " + Arrays.toString(offsets)
                + ", byteSize = " + byteSize + ", efficiency = " + efficiency() + "]";
    }
}
