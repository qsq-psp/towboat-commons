package mujica.json.io;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.DefaultByteBufHolder;
import mujica.io.codec.Base16Case;
import mujica.json.entity.JsonHandler;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DataType;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@CodeHistory(date = "2021/9/19", project = "va", name = "ByteBufReader")
@CodeHistory(date = "2021/12/30", project = "infrastructure", name = "ByteBufReader")
@CodeHistory(date = "2022/8/12", project = "Ultramarine")
@CodeHistory(date = "2026/3/24")
public class JsonByteBufReader extends DefaultByteBufHolder implements JsonSyncReader { // child: IntactJsonByteBufReader

    private int flags;

    public JsonByteBufReader(@NotNull ByteBuf data) {
        super(data);
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
        final ByteBuf data = content();
        List<Object> segments = null;
        int startIndex = data.readerIndex();
        int slowWriterIndex = startIndex;
        boolean isLatin1 = true;
        LABEL:
        while (true) {
            int x = data.readByte();
            if (x == '\\') {
                x = data.readByte();
                switch (x) {
                    case '\r':
                        x = data.readByte();
                        if (x != '\n') {
                            data.readerIndex(data.readerIndex() - 1);
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
                        x = readHex16(data);
                        if (x >= 0x80) {
                            if (segments == null) {
                                segments = new ArrayList<>();
                            }
                            segments.add(new int[] {startIndex, slowWriterIndex, isLatin1 ? 0 : 1});
                            segments.add((char) x);
                            startIndex = slowWriterIndex;
                            isLatin1 = true;
                            continue LABEL;
                        }
                        break;
                }
            } else if (x == quoteChar) {
                break;
            } else if (x < 0) {
                isLatin1 = false;
            }
            data.setByte(slowWriterIndex++, x);
        }
        if (startIndex < slowWriterIndex) {
            if (segments == null) {
                return data.toString(startIndex, slowWriterIndex - startIndex, isLatin1 ? StandardCharsets.US_ASCII : StandardCharsets.UTF_8);
            } else {
                segments.add(new int[] {startIndex, slowWriterIndex, isLatin1 ? 0 : 1});
            }
        }
        if (segments == null) {
            return "";
        } else {
            return concat(data, segments);
        }
    }

    protected int readHex16(@NotNull ByteBuf data) {
        int value = 0;
        for (int shift = 12; shift >= 0; shift -= 4) {
            int digit = data.readByte();
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

    @NotNull
    protected String concat(@NotNull ByteBuf data, @NotNull List<Object> segments) {
        int charLength = 0;
        final char[] chars = new char[charLength];
        return new String(chars);
    }
}
