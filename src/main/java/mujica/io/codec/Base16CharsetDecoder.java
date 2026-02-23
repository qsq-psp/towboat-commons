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

@CodeHistory(date = "2025/4/20")
class Base16CharsetDecoder extends CharsetDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(Base16CharsetDecoder.class);

    Base16CharsetDecoder(@NotNull Base16Charset cs) {
        super(cs, 2.0f, 2.0f);
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
        final int alphabetOffset = ((Base16Charset) charset()).alphabetOffset();
        while (true) {
            if (in.remaining() < 1) {
                return CoderResult.UNDERFLOW; // underflow first
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
