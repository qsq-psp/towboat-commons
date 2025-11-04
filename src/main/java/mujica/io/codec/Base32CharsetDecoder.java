package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

@CodeHistory(date = "2025/4/21")
class Base32CharsetDecoder extends CharsetDecoder {

    private int buffer32;

    private int srcShift;

    Base32CharsetDecoder(@NotNull Base32Charset cs) {
        super(cs, 1.6f, 8.0f);
    }

    @Override
    protected void implReset() {
        srcShift = 0;
    }

    @Override
    protected CoderResult decodeLoop(ByteBuffer in, CharBuffer out) {
        final int alphabetOffset = ((Base32Charset) charset()).alphabetOffset();
        while (true) {
            if (out.remaining() < 8) {
                return CoderResult.OVERFLOW;
            }
            if (in.remaining() < 5) {
                break;
            }
            long longData = 0L;
            for (int shift = 32; shift >= 0; shift -= 8) {
                longData |= (0xffL & in.get()) << shift;
            }
            for (int shift = 35; shift >= 0; shift -= 5) {
                out.put(decode(0x1f & (int) (longData >> shift), alphabetOffset));
            }
        }
        return CoderResult.UNDERFLOW;
    }

    @Override
    protected CoderResult implFlush(CharBuffer out) {
        return super.implFlush(out);
    }

    private char decode(int digit, int alphabetOffset) {
        if (digit < 26) {
            return (char) (alphabetOffset + digit);
        } else {
            return (char) ('2' - 26 + alphabetOffset);
        }
    }
}
