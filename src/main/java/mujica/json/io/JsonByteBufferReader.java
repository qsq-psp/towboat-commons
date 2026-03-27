package mujica.json.io;

import mujica.io.codec.Base16Case;
import mujica.json.entity.JsonHandler;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DataType;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

@CodeHistory(date = "2026/3/25")
public class JsonByteBufferReader implements JsonSyncReader {

    @NotNull
    private final ByteBuffer buffer;

    private int flags;

    public JsonByteBufferReader(@NotNull ByteBuffer buffer) {
        super();
        this.buffer = buffer;
    }

    @Override
    public void setFlags(int flags) {
        this.flags = flags;
    }

    @Override
    public int getFlags() {
        return flags;
    }

    @Override
    public void read(@NotNull JsonHandler jh) {
        //
    }

    @NotNull
    protected String readJsonString(@DataType("u8") int quoteChar) {
        int startIndex = buffer.position();
        int slowWriterIndex = startIndex;
        LABEL:
        while (true) {
            int x = buffer.get();
            if (x == '\\') {
                x = buffer.get();
                switch (x) {
                    case '\r':
                        x = buffer.get();
                        if (x != '\n') {
                            buffer.position(buffer.position() - 1);
                        }
                        continue LABEL;
                    case '\n':
                        continue LABEL;
                    case '"':
                    case '\\':
                    case '/':
                        break;
                    case '\'':
                        if (x != quoteChar) {
                            throw new RuntimeException("escape apostrophe");
                        }
                        break;
                    case '`':
                        if (x != quoteChar) {
                            throw new RuntimeException("escape grave accent");
                        }
                        break;
                    case 'b':
                        x = '\b';
                        break;
                    case 'f':
                        x = '\f';
                        break;
                    case 'n':
                        x = '\n';
                        break;
                    case 'r':
                        x = '\r';
                        break;
                    case 't':
                        x = '\t';
                        break;
                    case 'u':
                        x = readHex16();
                        break;
                }
            } else if (x == quoteChar) {
                break;
            }
            buffer.put(slowWriterIndex++, (byte) x);
        }
        return "";
    }

    private int readHex16() {
        int value = 0;
        for (int shift = 12; shift >= 0; shift -= 4) {
            int digit = buffer.get();
            if ('0' <= digit && digit <= '9') {
                digit -= '0';
            } else if ('A' <= digit && digit <= 'Z') {
                digit -= Base16Case.UPPER_CONSTANT;
            } else if ('a' <= digit && digit <= 'z') {
                digit -= Base16Case.LOWER_CONSTANT;
            } else {
                throw new RuntimeException("hex digit " + digit);
            }
            value |= digit << shift;
        }
        return value;
    }
}
