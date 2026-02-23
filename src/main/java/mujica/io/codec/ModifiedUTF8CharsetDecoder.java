package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

@CodeHistory(date = "2026/2/19")
@ReferencePage(title = "JVMS12 The CONSTANT_Utf8_info Structure", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.4.7")
class ModifiedUTF8CharsetDecoder extends CharsetDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModifiedUTF8CharsetDecoder.class);

    ModifiedUTF8CharsetDecoder(@NotNull ModifiedUTF8Charset cs) {
        super(cs, 1.0f, 1.0f);
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
            int octet = in.get();
            if ((octet & 0x80) == 0x00) {
                if (octet == 0) {
                    LOGGER.warn("octet 0");
                }
                out.put((char) octet);
            } else if ((octet & 0xe0) == 0xc0) {
                int codePoint = (octet & 0x1f) << 6;
                if (inRemaining < 2) {
                    undoGet(in);
                    return CoderResult.UNDERFLOW; // additional input is required
                }
                octet = in.get();
                if ((octet & 0xc0) != 0x80) {
                    LOGGER.error("prefix (2.1) {}", octet);
                    return CoderResult.malformedForLength(2);
                }
                codePoint |= octet & 0x3f;
                if (codePoint < 0x80 && codePoint != 0) {
                    LOGGER.warn("too small (2) {}", codePoint);
                }
                out.put((char) codePoint);
            } else if ((octet & 0xf0) == 0xe0) {
                int codePoint = (octet & 0x0f) << 12;
                if (inRemaining < 3) {
                    undoGet(in);
                    return CoderResult.UNDERFLOW; // additional input is required
                }
                octet = in.get();
                if ((octet & 0xc0) != 0x80) {
                    LOGGER.error("prefix (3.1) {}", octet);
                    return CoderResult.malformedForLength(2);
                }
                codePoint |= (octet & 0x3f) << 6;
                octet = in.get();
                if ((octet & 0xc0) != 0x80) {
                    LOGGER.error("prefix (3.2) {}", octet);
                    return CoderResult.malformedForLength(3);
                }
                codePoint |= octet & 0x3f;
                if (codePoint < 0x800) {
                    LOGGER.warn("too small (3) {}", codePoint);
                }
                out.put((char) codePoint);
            } else {
                LOGGER.error("prefix {}", octet);
                return CoderResult.malformedForLength(1);
            }
        }
    }

    private void undoGet(@NotNull ByteBuffer in) {
        in.position(in.position() - 1);
    }
}
