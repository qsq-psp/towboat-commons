package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

@CodeHistory(date = "2025/4/20")
class Base16CharsetEncoder extends CharsetEncoder {

    Base16CharsetEncoder(@NotNull Base16Charset cs) {
        super(cs, 0.5f, 1.0f);
    }

    @Override
    protected CoderResult encodeLoop(@NotNull CharBuffer in, @NotNull ByteBuffer out) {
        while (true) {
            if (in.remaining() < 2) {
                return CoderResult.UNDERFLOW;
            }
            if (out.remaining() < 1) {
                return CoderResult.OVERFLOW;
            }
            int octet;
            char ch = in.get();
            if ('0' <= ch && ch <= '9') {
                octet = ch - '0';
            } else if ('a' <= ch && ch <= 'z') {
                octet = ch - Base16Case.LOWER;
            } else if ('A' <= ch && ch <= 'Z') {
                octet = ch - Base16Case.UPPER;
            } else {
                return CoderResult.malformedForLength(1);
            }
            octet <<= 4;
            ch = in.get();
            if ('0' <= ch && ch <= '9') {
                octet |= ch - '0';
            } else if ('a' <= ch && ch <= 'z') {
                octet |= ch - Base16Case.LOWER;
            } else if ('A' <= ch && ch <= 'Z') {
                octet |= ch - Base16Case.UPPER;
            } else {
                return CoderResult.malformedForLength(1);
            }
            out.put((byte) octet);
        }
    }
}
