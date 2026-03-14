package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

@CodeHistory(date = "2025/4/20")
class Base16CharsetEncoder extends CharsetEncoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(Base16CharsetEncoder.class);

    Base16CharsetEncoder(@NotNull Base16Charset cs) {
        super(cs, 0.5f, 1.0f);
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
    @NotNull
    protected CoderResult encodeLoop(@NotNull CharBuffer in, @NotNull ByteBuffer out) {
        while (true) {
            if (in.remaining() < 2) {
                return CoderResult.UNDERFLOW; // underflow first
            }
            if (out.remaining() < 1) {
                return CoderResult.OVERFLOW;
            }
            int octet;
            int ch = in.get();
            if ('0' <= ch && ch <= '9') {
                octet = ch - '0';
            } else if ('a' <= ch && ch <= 'z') {
                octet = ch - Base16Case.LOWER_CONSTANT;
            } else if ('A' <= ch && ch <= 'Z') {
                octet = ch - Base16Case.UPPER_CONSTANT;
            } else {
                LOGGER.warn("digit0 {}", ch);
                return CoderResult.malformedForLength(1);
            }
            octet <<= 4;
            ch = in.get();
            if ('0' <= ch && ch <= '9') {
                octet |= ch - '0';
            } else if ('a' <= ch && ch <= 'z') {
                octet |= ch - Base16Case.LOWER_CONSTANT;
            } else if ('A' <= ch && ch <= 'Z') {
                octet |= ch - Base16Case.UPPER_CONSTANT;
            } else {
                LOGGER.warn("digit1 {}", ch);
                return CoderResult.malformedForLength(2);
            }
            out.put((byte) octet);
        }
    }
}
