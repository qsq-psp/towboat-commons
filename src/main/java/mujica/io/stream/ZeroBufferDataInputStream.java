package mujica.io.stream;

import mujica.io.codec.TowboatCharsetProvider;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.*;

@CodeHistory(date = "2026/2/24")
public class ZeroBufferDataInputStream extends FilterInputStream implements DataInput {

    public ZeroBufferDataInputStream(@NotNull InputStream in) {
        super(in);
    }

    @Override
    public void readFully(@NotNull byte[] array) throws IOException {
        readFully(array, 0, array.length);
    }

    @Override
    public void readFully(@NotNull byte[] array, int offset, int length) throws IOException {
        assert length >= 0;
        while (length > 0) {
            assert offset >= 0;
            int count = read(array, offset, length);
            assert count <= length;
            if (count <= 0) {
                throw new EOFException();
            }
            offset += count;
            length -= count;
        }
    }

    @Override
    public int skipBytes(int n) throws IOException {
        return (int) skip(n); // just skip; no buffer
    }

    @Override
    public boolean readBoolean() throws IOException {
        return readByte() != 0;
    }

    @Override
    public byte readByte() throws IOException {
        return (byte) readUnsignedByte();
    }

    @Override
    public int readUnsignedByte() throws IOException {
        final int value = read();
        if (value == -1) {
            throw new EOFException();
        }
        assert value == (value & 0xff);
        return value;
    }

    @Override
    public short readShort() throws IOException {
        return (short) readUnsignedShort();
    }

    @Override
    public int readUnsignedShort() throws IOException {
        return (readUnsignedByte() << Byte.SIZE) | readUnsignedByte();
    }

    @Override
    public char readChar() throws IOException {
        return (char) readUnsignedShort();
    }

    @Override
    public int readInt() throws IOException {
        return (readUnsignedShort() << Short.SIZE) | readUnsignedShort();
    }

    public long readUnsignedInt() throws IOException {
        return readInt() & 0xffffffffL;
    }

    @Override
    public long readLong() throws IOException {
        return (readUnsignedInt() << Integer.SIZE) | readUnsignedInt();
    }

    @Override
    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    @Override
    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    @Override
    public String readLine() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public String readUTF() throws IOException {
        final int length = readUnsignedShort();
        final byte[] data = new byte[length];
        readFully(data);
        return new String(data, TowboatCharsetProvider.MUTF8);
    }
}
