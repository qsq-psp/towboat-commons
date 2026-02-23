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

@CodeHistory(date = "2025/4/21")
class Base32CharsetEncoder extends CharsetEncoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(Base32CharsetEncoder.class);

    Base32CharsetEncoder(@NotNull Base32Charset cs) {
        super(cs, 0.625f, 0.625f);
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
    protected CoderResult encodeLoop(@NotNull CharBuffer in, @NotNull ByteBuffer out) {
        return null;
    }
}
