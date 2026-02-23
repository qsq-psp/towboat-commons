package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

@CodeHistory(date = "2026/2/20")
@ReferencePage(title = "JVMS12 The CONSTANT_Utf8_info Structure", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.4.7")
class ModifiedUTF8CharsetEncoder extends CharsetEncoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(Base16CharsetEncoder.class);

    ModifiedUTF8CharsetEncoder(@NotNull ModifiedUTF8Charset cs) {
        super(cs, 1.1f, 3.0f);
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
        while (in.hasRemaining()) {
            int outRemaining = out.remaining();
            if (outRemaining < 1) {
                return CoderResult.OVERFLOW;
            }
            int ch = in.get();
            if (ch < 0x80 && ch != 0) {
                out.put((byte) ch);
            } else if (ch < 0x800) {
                if (outRemaining < 2) {
                    undoGet(in);
                    return CoderResult.OVERFLOW;
                }
                out.put((byte) (0b110_00000 | (ch >> 6)));
                out.put((byte) (0b10_000000 | (ch & 0b00_111111)));
            } else {
                if (outRemaining < 3) {
                    undoGet(in);
                    return CoderResult.OVERFLOW;
                }
                out.put((byte) (0b1110_0000 | (ch >> 12)));
                out.put((byte) (0b10_000000 | ((ch >> 6) & 0b00_111111)));
                out.put((byte) (0b10_000000 | (ch & 0b00_111111)));
            }
        }
        return CoderResult.UNDERFLOW;
    }

    private void undoGet(@NotNull CharBuffer in) {
        in.position(in.position() - 1);
    }

    protected CoderResult utf8Loop(@NotNull CharBuffer in, @NotNull ByteBuffer out) {
        while (true) {
            int inRemaining = in.remaining();
            if (inRemaining < 1) {
                return CoderResult.UNDERFLOW; // check underflow first
            }
            int outRemaining = out.remaining();
            if (outRemaining < 1) {
                return CoderResult.OVERFLOW;
            }
            int inPosition = in.position();
            int codePoint;
            {
                char ch0 = in.get();
                if (Character.isSurrogate(ch0)) {
                    if (Character.isHighSurrogate(ch0)) {
                        if (inRemaining < 2) {
                            in.position(inPosition);
                            return CoderResult.UNDERFLOW; // additional input is required
                        }
                        char ch1 = in.get();
                        if (Character.isLowSurrogate(ch1)) {
                            codePoint = Character.toCodePoint(ch0, ch1);
                        } else {
                            LOGGER.error("expect low (trailing) surrogate {} {}", ch0, ch1);
                            return CoderResult.malformedForLength(2);
                        }
                    } else {
                        LOGGER.error("expect high (leading) surrogate {}", ch0);
                        return CoderResult.malformedForLength(1);
                    }
                } else {
                    codePoint = ch0;
                }
            }
            if (codePoint < 0x80) {
                out.put((byte) codePoint);
            } else if (codePoint < 0x800) {
                if (outRemaining < 2) {
                    in.position(inPosition);
                    return CoderResult.OVERFLOW;
                }
                out.put((byte) (0b110_00000 | (codePoint >> 6)));
                out.put((byte) (0b10_000000 | (codePoint & 0b00_111111)));
            } else {
                if (outRemaining < 3) {
                    in.position(inPosition);
                    return CoderResult.OVERFLOW;
                }
                out.put((byte) (0b1110_0000 | (codePoint >> 12)));
                out.put((byte) (0b10_000000 | ((codePoint >> 6) & 0b00_111111)));
                out.put((byte) (0b10_000000 | (codePoint & 0b00_111111)));
            }
        }
    }
}
