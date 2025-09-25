package mujica.io.charset;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

@CodeHistory(date = "2025/4/20")
class Base16CharsetDecoder extends CharsetDecoder {

    Base16CharsetDecoder(@NotNull Base16Charset cs) {
        super(cs, 2.0f, 2.0f);
    }

    @Override
    protected CoderResult decodeLoop(@NotNull ByteBuffer in, @NotNull CharBuffer out) {
        final int alphabetOffset = ((Base16Charset) charset()).alphabetOffset();
        while (true) {
            if (in.remaining() < 1) {
                return CoderResult.UNDERFLOW;
            }
            if (out.remaining() < 2) {
                return CoderResult.OVERFLOW;
            }
            int octet = in.get();
            int digit = 0xf & (octet >> 4);
            if (digit < 0xa) {
                digit += '0';
            } else {
                digit += alphabetOffset;
            }
            out.put((char) digit);
            digit = 0xf & octet;
            if (digit < 0xa) {
                digit += '0';
            } else {
                digit += alphabetOffset;
            }
            out.put((char) digit);
        }
    }
}
