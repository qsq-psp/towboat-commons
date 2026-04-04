package mujica.json.io;

import io.netty.buffer.ByteBuf;
import mujica.ds.of_int.PublicIntSlot;
import mujica.io.codec.Base16Case;
import mujica.io.codec.UTF8PushPullEncoder;
import mujica.reflect.function.ByteConsumer;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DataType;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

/**
 * Created on 2026/3/30.
 */
@CodeHistory(date = "2026/3/30")
public class JsonRewriteByteBufReader extends JsonByteBufReader {

    private final UTF8PushPullEncoder encoder = new UTF8PushPullEncoder();

    private final PublicIntSlot slowWriterIndex = new PublicIntSlot();

    private final ByteConsumer slowWrite = value -> content().setByte(slowWriterIndex.value++, value);

    public JsonRewriteByteBufReader(@NotNull ByteBuf data) {
        super(data);
    }

    @NotNull
    protected String readJsonString(@DataType("u8") int quoteChar) {
        final ByteBuf data = content();
        final int startIndex = data.readerIndex();
        encoder.reset();
        slowWriterIndex.setInt(startIndex);
        LABEL:
        while (true) {
            int value = data.readByte();
            if (value == '\\') {
                value = data.readByte();
                switch (value) {
                    case '\r':
                        value = data.readByte();
                        if (value != '\n') {
                            data.readerIndex(data.readerIndex() - 1);
                        }
                        // no break here
                    case '\n':
                        continue LABEL;
                    case '"':
                    case '\\':
                    case '/':
                        break;
                    case '\'':
                        if (value != quoteChar) {
                            throw new RuntimeException("escape apostrophe");
                        }
                        break;
                    case '`':
                        if (value != quoteChar) {
                            throw new RuntimeException("escape grave accent");
                        }
                        break;
                    case 'b':
                        value = '\b';
                        break;
                    case 'f':
                        value = '\f';
                        break;
                    case 'n':
                        value = '\n';
                        break;
                    case 'r':
                        value = '\r';
                        break;
                    case 't':
                        value = '\t';
                        break;
                    case 'u':
                        readHex16();
                        continue LABEL;
                }
            } else if (value == quoteChar) {
                break;
            }
            encoder.finishPush(slowWrite);
            slowWrite.accept((byte) value);
        }
        encoder.finishPush(slowWrite);
        return data.toString(startIndex, slowWriterIndex.value - startIndex, StandardCharsets.UTF_8);
    }

    private void readHex16() {
        final ByteBuf data = content();
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
        encoder.push((char) value, slowWrite);
    }
}
