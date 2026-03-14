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

@CodeHistory(date = "2026/2/14")
abstract class PercentEncoder extends CharsetEncoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(PercentEncoder.class);

    protected PercentEncoder(@NotNull PercentCharset cs, float averageBytesPerChar) {
        super(cs, averageBytesPerChar, 9.0f);
    }

    @Override
    protected void implOnMalformedInput(CodingErrorAction newAction) {
        LOGGER.info("set malformed action {}", newAction);
    }

    @Override
    protected void implOnUnmappableCharacter(CodingErrorAction newAction) {
        LOGGER.info("set unmappable action {}", newAction);
    }

    protected abstract boolean noEscape(int ch);

    @NotNull
    @Override
    protected CoderResult encodeLoop(@NotNull CharBuffer in, @NotNull ByteBuffer out) {
        while (true) {
            int inRemaining = in.remaining();
            if (inRemaining < 1) {
                return CoderResult.UNDERFLOW; // underflow first
            }
            int outRemaining = out.remaining();
            if (outRemaining < 1) {
                return CoderResult.OVERFLOW;
            }
            int ch0 = in.get();
            if (noEscape(ch0)) {
                out.put((byte) ch0);
            } else if (ch0 < 0x0080) {
                if (outRemaining < 3) {
                    undoGet(in);
                    return CoderResult.OVERFLOW;
                }
                percentEncodeOctet(ch0, out);
            } else if (ch0 < 0x0800) {
                if (outRemaining < 6) {
                    undoGet(in);
                    return CoderResult.OVERFLOW;
                }
                percentEncodeOctet(0xc0 | (ch0 >> 6), out);
                percentEncodeOctet(0x80 | (0x3f & ch0), out);
            } else if (ch0 < Character.MIN_SURROGATE || Character.MAX_SURROGATE < ch0) {
                if (outRemaining < 9) {
                    undoGet(in);
                    return CoderResult.OVERFLOW;
                }
                percentEncodeOctet(0xe0 | (ch0 >> 12), out);
                percentEncodeOctet(0x80 | (0x3f & (ch0 >> 6)), out);
                percentEncodeOctet(0x80 | (0x3f & ch0), out);
            } else {
                // now ch0 is surrogate
                if (Character.MIN_LOW_SURROGATE <= ch0) {
                    LOGGER.warn("malformed high {}", ch0);
                    return CoderResult.malformedForLength(1);
                }
                // now ch0 is high (leading) surrogate
                if (inRemaining < 2) {
                    undoGet(in);
                    return CoderResult.UNDERFLOW; // underflow first
                }
                if (outRemaining < 12) {
                    undoGet(in);
                    return CoderResult.OVERFLOW;
                }
                int ch1 = in.get();
                if (ch1 < Character.MIN_LOW_SURROGATE || Character.MAX_LOW_SURROGATE < ch1) {
                    LOGGER.warn("malformed low {} {}", ch0, ch1);
                    return CoderResult.malformedForLength(2);
                }
                int cp = ((ch0 << 10) + ch1) + (Character.MIN_SUPPLEMENTARY_CODE_POINT - (Character.MIN_HIGH_SURROGATE << 10) - Character.MIN_LOW_SURROGATE);
                percentEncodeOctet(0xf0 | (0x07 & (cp >> 18)), out);
                percentEncodeOctet(0x80 | (0x3f & (cp >> 12)), out);
                percentEncodeOctet(0x80 | (0x3f & (cp >> 6)), out);
                percentEncodeOctet(0x80 | (0x3f & cp), out);
            }
        }
    }

    private void undoGet(@NotNull CharBuffer in) {
        in.position(in.position() - 1);
    }

    private void percentEncodeOctet(int octet, @NotNull ByteBuffer out) {
        final int alphabetOffset = ((PercentCharset) charset()).getAlphabetOffset();
        out.put((byte) '%');
        int digit = 0xf & (octet >> 4);
        if (digit < 0xa) {
            digit += '0';
        } else {
            digit += alphabetOffset;
        }
        out.put((byte) digit);
        digit = 0xf & octet;
        if (digit < 0xa) {
            digit += '0';
        } else {
            digit += alphabetOffset;
        }
        out.put((byte) digit);
    }
}
