package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

@CodeHistory(date = "2026/2/14")
class PercentDecoder extends CharsetDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(PercentDecoder.class);

    PercentDecoder(@NotNull PercentCharset cs) {
        super(cs, 0.8f, 1.0f);
    }

    @Override
    protected void implOnMalformedInput(CodingErrorAction newAction) {
        LOGGER.info("set malformed action {}", newAction);
    }

    @Override
    protected void implOnUnmappableCharacter(CodingErrorAction newAction) {
        LOGGER.info("set unmappable action {}", newAction);
    }

    @Override
    protected CoderResult decodeLoop(@NotNull ByteBuffer in, @NotNull CharBuffer out) {
        while (true) {
            int inRemaining = in.remaining();
            if (inRemaining < 1) {
                return CoderResult.UNDERFLOW; // underflow first
            }
            if (!out.hasRemaining()) {
                return CoderResult.OVERFLOW;
            }
            int ch = in.get();
            if (ch != '%') {
                if (ch < 0) {
                    LOGGER.warn("non-ASCII {}", ch);
                    return CoderResult.malformedForLength(1);
                }
                out.put((char) ch);
                continue;
            }
            int utf8Length = 0;
            int utf8Index = 0;
            int cp = 0;
            while (true) {
                if (utf8Length == 0) { // first byte
                    if (inRemaining < 3) {
                        in.position(in.position() - 1);
                        return CoderResult.UNDERFLOW;
                    }
                } else {
                    if (in.get() != '%') {
                        in.position(in.position() - 1);
                        return CoderResult.malformedForLength(3 * utf8Index + 1);
                    }
                }
                int octet;
                ch = in.get();
                if ('0' <= ch && ch <= '9') {
                    octet = ch - '0';
                } else if ('a' <= ch && ch <= 'z') {
                    octet = ch - Base16Case.LOWER;
                } else if ('A' <= ch && ch <= 'Z') {
                    octet = ch - Base16Case.UPPER;
                } else {
                    return CoderResult.malformedForLength(3 * utf8Index + 2);
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
                    return CoderResult.malformedForLength(3 * utf8Index + 3);
                }
                utf8Index++;
                if (utf8Length == 0) {
                    if ((octet & 0x80) == 0x00) {
                        utf8Length = 1;
                        cp = octet;
                    } else if ((octet & 0xe0) == 0xc0) {
                        utf8Length = 2;
                        cp = (octet & 0x1f) << 6;
                    } else if ((octet & 0xf0) == 0xe0) {
                        utf8Length = 3;
                        cp = (octet & 0x0f) << 12;
                    } else if ((octet & 0xf8) == 0xf0) {
                        utf8Length = 4;
                        cp = (octet & 0x07) << 18;
                    } else {
                        return CoderResult.malformedForLength(3);
                    }
                } else {
                    if ((octet & 0xc0) == 0x80) {
                        cp |= (octet & 0x3f) << (6 * (utf8Length - utf8Index));
                    } else {
                        return CoderResult.malformedForLength(3 * utf8Index);
                    }
                }
                if (utf8Index == utf8Length) {
                    if (cp < 0x10000) {
                        out.put((char) cp);
                    } else {
                        out.put(Character.highSurrogate(cp));
                        out.put(Character.lowSurrogate(cp));
                    }
                    break;
                }
            }
        }
    }
}
