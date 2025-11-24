package mujica.reflect.bytecode;

import mujica.io.buffer.ByteBufferUtil;
import mujica.io.nest.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import mujica.text.escape.Quote;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

@CodeHistory(date = "2019", project = "bone", name = "JavaConstantUtf8")
@CodeHistory(date = "2025/9/6")
@ReferencePage(title = "JVMS12 The CONSTANT_Utf8_info Structure", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.4.7")
class Utf8ConstantInfo extends ConstantInfo {

    private static final long serialVersionUID = 0xCE9EA2B9C2469887L;

    String string;

    Utf8ConstantInfo() {
        super();
    }

    Utf8ConstantInfo(@NotNull String string) {
        super();
        this.string = string;
    }

    @Override
    protected int tag() {
        return ConstantPool.CONSTANT_UTF8;
    }

    protected int section() {
        return 6;
    }

    protected int sinceVersion() {
        return (45 << Short.SIZE) | 3;
    }

    @Nullable
    protected Class<?> loadedClass() {
        return null;
    }

    @Override
    public void read(@NotNull LimitedDataInput in) throws IOException {
        string = in.readUTF();
    }

    @Override
    public void read(@NotNull ByteBuffer buffer) {
        final int length = 0xffff & buffer.getShort(); // read unsigned short as byte length
        final int oldLimit = buffer.limit();
        final int newLimit = buffer.position() + length;
        if (newLimit > oldLimit) {
            throw new BufferUnderflowException();
        }
        buffer.limit(newLimit);
        try {
            char[] chars = new char[length]; // estimated maximum size; the actual string may be smaller
            int charIndex = 0;
            while (buffer.hasRemaining()) {
                int b0 = buffer.get();
                if ((b0 & 0x80) == 0) {
                    if (b0 == 0) {
                        throw new RuntimeException();
                    }
                    chars[charIndex++] = (char) b0;
                } else if ((b0 & 0xe0) == 0xc0) {
                    int b1 = buffer.get();
                    chars[charIndex++] = (char) (((b0 & 0x1f) << 6) | (b1 & 0x3f));
                } else {
                    int b1 = buffer.get();
                    int b2 = buffer.get();
                    chars[charIndex++] = (char) (((b0 & 0x0f) << 12) | ((b1 & 0x3f) << 6) | (b2 & 0x3f));
                }
            }
            string = new String(chars, 0, charIndex);
        } finally {
            buffer.limit(oldLimit);
        }
    }

    @Override
    public void write(@NotNull DataOutput out) throws IOException {
        out.writeUTF(string);
    }

    @Override
    public void write(@NotNull ByteBuffer buffer) {
        final int position = buffer.position();
        try {
            buffer.putShort((short) 0); // reserve a slot for an unsigned short, fill it later
            int length = string.length();
            for (int index = 0; index < length; index++) {
                int ch = string.charAt(index);
                if (0 < ch && ch < 0x80) {
                    buffer.put((byte) ch);
                } else if (ch < 0x800) {
                    buffer.put((byte) (0xc0 | (ch >>> 6)));
                    buffer.put((byte) (0x80 | (ch & 0x3f)));
                } else {
                    buffer.put((byte) (0xe0 | (ch >>> 12)));
                    buffer.put((byte) (0x80 | ((ch >>> 6) & 0x3f)));
                    buffer.put((byte) (0x80 | (ch & 0x3f)));
                }
            }
            buffer.putShort(position, (short) (buffer.position() - position - 2));
        } catch (BufferOverflowException e) {
            buffer.position(position);
            throw e;
        }
    }

    @NotNull
    @Override
    public String toString(@NotNull ClassFile context) {
        return "#" + newIndex + " utf8 " + Quote.JSON.apply(string);
    }

    @Override
    public void retain(@NotNull ClassFile context) {
        referenceCount++;
    }

    @Override
    public int hashCode() {
        return string.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Utf8ConstantInfo && string.equals(((Utf8ConstantInfo) obj).string);
    }
}
