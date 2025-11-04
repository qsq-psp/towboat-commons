package mujica.io.codec;

import mujica.math.algebra.discrete.IntegralMath;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/5/3.
 */
@CodeHistory(date = "2025/5/3")
public class BaseAnyLongCodec extends BaseAnyCodec {

    private static final long serialVersionUID = 0xf8a08599fc6b54c4L;

    protected long buffer;

    private int octetIndex, codeIndex; // count down

    public BaseAnyLongCodec(@NotNull BaseAnyCodecSpec spec) {
        super(spec);
        spec.validateForLong();
    }

    @Override
    public boolean moreOctet() {
        return octetIndex > 0;
    }

    @Override
    public boolean moreCode() {
        return codeIndex > 0;
    }

    @Override
    public void encodeStart() {
        buffer = 0L;
        octetIndex = spec.octetCount();
        codeIndex = 0;
    }

    @Override
    public void encodeStop() {
        if (octetIndex < spec.octetCount()) {
            octetIndex = 0;
            codeIndex = spec.codeCount();
        }
    }

    @Override
    public void encodeIn(byte octet) {
        if (octetIndex > 0) {
            octetIndex--;
            if (spec.reflectOctet()) {
                buffer = (buffer << Byte.SIZE) | (0xffL & octet);
            } else {
                buffer |= (0xffL & octet) << (octetIndex << 3);
            }
            if (octetIndex == 0) {
                codeIndex = spec.codeCount();
            }
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public byte encodeOut() {
        if (codeIndex > 0) {
            codeIndex--;
            long modulus = spec.codeTable.length;
            int value;
            if (spec.reflectCode()) {
                modulus = IntegralMath.INSTANCE.power(modulus, codeIndex);
                value = (int) (buffer / modulus);
                buffer %= modulus;
            } else {
                value = (int) (buffer % modulus);
                buffer /= modulus;
            }
            byte code = spec.codeTable[value];
            if (codeIndex == 0) {
                octetIndex = spec.octetCount();
                buffer = 0L;
            }
            return code;
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public void decodeStart() {
        buffer = 0L;
        octetIndex = 0;
        codeIndex = spec.codeCount();
    }

    @Override
    public void decodeStop() {
        if (codeIndex < spec.codeCount()) {
            octetIndex = spec.octetCount();
            codeIndex = 0;
        }
    }

    @Override
    public void decodeIn(byte code) {
        final int value = spec.octetTable[code];
        if (value < 0) {
            throw new RuntimeException();
        }
        if (codeIndex > 0) {
            codeIndex--;
            long modulus = spec.codeTable.length;
            if (spec.reflectCode()) {
                buffer = buffer * modulus + value;
            } else if (value != 0) {
                buffer += IntegralMath.INSTANCE.power(modulus, codeIndex) * value;
            }
            if (codeIndex == 0) {
                octetIndex = spec.octetCount();
            }
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public byte decodeOut() {
        if (octetIndex > 0) {
            octetIndex--;
            byte octet;
            if (spec.reflectOctet()) {
                octet = (byte) (buffer >>> (octetIndex << 3));
            } else {
                octet = (byte) buffer;
                buffer >>>= 3;
            }
            if (octetIndex == 0) {
                codeIndex = spec.codeCount();
                buffer = 0L;
            }
            return octet;
        } else {
            throw new IllegalStateException();
        }
    }
}
