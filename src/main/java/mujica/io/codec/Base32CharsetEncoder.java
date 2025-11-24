package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

/**
 * Created on 2025/4/21.
 */
@CodeHistory(date = "2025/4/21")
public class Base32CharsetEncoder extends CharsetEncoder {

    Base32CharsetEncoder(@NotNull Base32Charset cs) {
        super(cs, 0.625f, 0.625f);
    }

    @Override
    protected CoderResult encodeLoop(CharBuffer in, ByteBuffer out) {
        return null;
    }
}
