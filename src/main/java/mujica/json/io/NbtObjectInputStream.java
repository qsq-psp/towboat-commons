package mujica.json.io;

import mujica.io.stream.ZeroBufferDataInputStream;
import mujica.json.handler.JsonHandler;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

@CodeHistory(date = "2023/5/13", project = "omnidirectional", name = "NbtInputStream")
@CodeHistory(date = "2023/5/16", project = "omnidirectional", name = "NbtReader")
@CodeHistory(date = "2026/6/8")
public class NbtObjectInputStream extends ZeroBufferDataInputStream implements NbtSyncReader {

    public NbtObjectInputStream(@NotNull InputStream in) {
        super(in);
    }

    @Override
    public void read(@NotNull JsonHandler jh) {
        try {
            jh.openObject();
            {
                int id = readUnsignedByte();
                jh.key(readUTF());
                readObject(id, jh);
            }
            jh.closeObject();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void readObject(int id, @NotNull JsonHandler jh) throws IOException {
        switch (id) {
            case ID_BYTE:
                jh.numberValue(readUnsignedByte());
                break;
            case ID_SHORT:
                jh.numberValue(readUnsignedShort());
                break;
            case ID_INT:
                jh.numberValue(readInt());
                break;
            case ID_LONG:
                jh.numberValue(readLong());
                break;
            case ID_FLOAT:
                jh.numberValue(readFloat());
                break;
            case ID_DOUBLE:
                jh.numberValue(readDouble());
                break;
            case ID_BYTE_ARRAY:
                readByteArray(jh);
                break;
            case ID_STRING:
                jh.stringValue(readUTF());
                break;
            case ID_LIST:
                readList(jh);
                break;
            case ID_COMPOUND:
                readCompound(jh);
                break;
            case ID_INT_ARRAY:
                readIntArray(jh);
                break;
            case ID_LONG_ARRAY:
                readLongArray(jh);
                break;
            default:
                throw new IOException("unrecognized id " + id);
        }
    }

    private void readByteArray(@NotNull JsonHandler jh) throws IOException {
        final int length = readInt();
        if (length < 0) {
            throw new IOException("negative byte array length");
        }
        jh.openArray();
        for (int index = 0; index < length; index++) {
            jh.numberValue(readUnsignedByte());
        }
        jh.closeArray();
    }

    private void readIntArray(@NotNull JsonHandler jh) throws IOException {
        final int length = readInt();
        if (length < 0) {
            throw new IOException("negative int array length");
        }
        jh.openArray();
        for (int index = 0; index < length; index++) {
            jh.numberValue(readInt());
        }
        jh.closeArray();
    }

    private void readLongArray(@NotNull JsonHandler jh) throws IOException {
        final int length = readInt();
        if (length < 0) {
            throw new IOException("negative long array length");
        }
        jh.openArray();
        for (int index = 0; index < length; index++) {
            jh.numberValue(readLong());
        }
        jh.closeArray();
    }

    private void readList(@NotNull JsonHandler jh) throws IOException {
        final int id = readUnsignedByte();
        final int length = readInt();
        if (length < 0) {
            throw new IOException("negative list array length");
        }
        jh.openArray();
        for (int index = 0; index < length; index++) {
            readObject(id, jh);
        }
        jh.closeArray();
    }

    private void readCompound(@NotNull JsonHandler jh) throws IOException {
        jh.openObject();
        while (true) {
            int id = readUnsignedByte();
            if (id == ID_END) {
                break;
            }
            jh.key(readUTF());
            readObject(id, jh);
        }
        jh.closeObject();
    }
}
