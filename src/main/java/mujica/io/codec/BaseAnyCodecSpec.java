package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * Created on 2025/5/2.
 */
@CodeHistory(date = "2025/5/2")
public class BaseAnyCodecSpec implements Serializable {

    private static final long serialVersionUID = 0x44fbac1976ec8ef1L;

    protected final short octetVector, codeVector;

    @NotNull
    protected final byte[] codeTable;

    @NotNull
    protected final short[] octetTable;

    public BaseAnyCodecSpec(short octetVector, short codeVector, @NotNull byte[] codeTable) {
        super();
        final int absOctetCount = Math.abs(octetVector);
        final int absCodeCount = Math.abs(codeVector);
        if (absOctetCount == 0 || absCodeCount == 0) {
            throw new IllegalArgumentException();
        }
        this.octetVector = octetVector;
        this.codeVector = codeVector;
        final int modulus = codeTable.length;
        if (modulus <= 1 || modulus > Byte.SIZE) {
            throw new IllegalArgumentException();
        }
        if (BigInteger.valueOf(modulus).pow(absCodeCount).compareTo(BigInteger.ONE.shiftLeft(Byte.SIZE * absOctetCount)) < 0) {
            throw new IllegalArgumentException();
        }
        this.octetTable = new short[0x100];
        Arrays.fill(octetTable, (short) -1);
        for (int index = 0; index < modulus; index++) {
            int code = 0xff & codeTable[index];
            if (octetTable[code] != -1) {
                throw new IllegalArgumentException();
            }
            octetTable[code] = (short) index;
        }
        this.codeTable = codeTable;
    }

    public void validateForLong() {
        final int absOctetCount = Math.abs(octetVector);
        if (absOctetCount >= Long.BYTES) {
            throw new IllegalArgumentException();
        }
    }

    public int octetCount() {
        return Math.abs(octetVector);
    }

    public boolean reflectOctet() {
        return octetVector < 0;
    }

    public int codeCount() {
        return Math.abs(codeVector);
    }

    public boolean reflectCode() {
        return codeVector < 0;
    }

    @Override
    public int hashCode() {
        return (((octetVector << 8) + codeVector) << 8) + Arrays.hashCode(codeTable);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BaseAnyCodecSpec)) {
            return false;
        }
        final BaseAnyCodecSpec that = (BaseAnyCodecSpec) obj;
        return this.octetVector == that.octetVector && this.codeVector == that.codeVector && Arrays.equals(this.codeTable, that.codeTable);
    }

    @Override
    public String toString() {
        return "BaseAnyCodecSpec[octetCount = " + octetVector + ", codeCount = " + codeVector + "]";
    }
}
