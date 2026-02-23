package mujica.io.hash;

import mujica.reflect.modifier.CodeHistory;

import java.io.Serializable;

@CodeHistory(date = "2025/4/27")
public class CrcSpec implements Serializable {

    private static final long serialVersionUID = 0xdb99a5914d2ba81fL;

    final int bitLength;

    final long polynomial;

    final long initialState;

    final long finalFlip;

    final boolean reflectIn;

    final boolean reflectOut;

    public CrcSpec(int bitLength, long polynomial, long initialState, long finalFlip, boolean reflectIn, boolean reflectOut) {
        super();
        if (!(0 < bitLength && bitLength <= Long.SIZE)) {
            throw new IllegalArgumentException();
        }
        this.bitLength = bitLength;
        this.polynomial = polynomial;
        this.initialState = initialState;
        this.finalFlip = finalFlip;
        this.reflectIn = reflectIn;
        this.reflectOut = reflectOut;
    }

    public static final CrcSpec CRC32 = new CrcSpec(
            32, 0x04c11db7, 0xffffffffL, 0xffffffffL, true, true
    );

    public static final CrcSpec CRC32C = new CrcSpec(
            32, 0x1edc6f41, 0xffffffffL, 0xffffffffL, true, true
    );

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true; // quick
        }
        if (!(obj instanceof CrcSpec)) {
            return false;
        }
        final CrcSpec that = (CrcSpec) obj;
        return this.bitLength == that.bitLength
                && this.polynomial == that.polynomial
                && this.initialState == that.initialState
                && this.finalFlip == that.finalFlip
                && this.reflectIn == that.reflectIn
                && this.reflectOut == that.reflectOut;
    }
}
