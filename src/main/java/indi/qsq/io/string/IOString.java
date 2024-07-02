package indi.qsq.io.string;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Created on 2024/6/28.
 */
public class IOString {

    @NotNull
    public final String string;

    public IOString(@NotNull String string) {
        super();
        this.string = string;
    }

    public int byteLength() {
        return string.codePoints().reduce(0, (length, codePoint) -> {
            if (codePoint < 0x0080) {
                return length + 1;
            } else if (codePoint < 0x0800) {
                return length + 2;
            } else if (codePoint < 0x10000) {
                return length + 3;
            } else {
                return length + 4;
            }
        });
    }

    public int charLength() {
        return string.length();
    }

    public int codePointLength() {
        return (int) string.codePoints().count();
    }

    public void writeInto(@NotNull OutputStream os) throws IOException {
        os.write(string.getBytes(StandardCharsets.UTF_8));
    }

    public void addInto(@NotNull ByteBuffer buf) throws BufferOverflowException {
        buf.put(string.getBytes(StandardCharsets.UTF_8));
    }

    public void addInto(@NotNull ByteBuf buf) {
        buf.writeCharSequence(string, StandardCharsets.UTF_8);
    }

    public void appendTo(@NotNull StringBuilder sb) {
        sb.append(string);
    }

    public void writeTo(@NotNull ContentHandler handler) throws SAXException {
        final char[] chars = string.toCharArray();
        handler.characters(chars, 0, chars.length);
    }

    @Override
    public int hashCode() {
        return string.hashCode();
    }

    @Override
    @NotNull
    public String toString() {
        return string;
    }
}
