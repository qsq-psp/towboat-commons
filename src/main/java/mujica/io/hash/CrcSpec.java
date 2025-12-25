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

    final boolean reflectIn, reflectOut;

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
}
