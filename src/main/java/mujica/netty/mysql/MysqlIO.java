package mujica.netty.mysql;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;

@CodeHistory(date = "2024/5/19", project = "netty-mysql-connector", name = "MysqlByteBuf")
@CodeHistory(date = "2026/5/2")
final class MysqlIO {

    static final int MAX_SIZE = 0x40000;

    static int readInt3(@NotNull ByteBuf data) {
        final int b0 = data.readByte();
        final int b1 = data.readByte();
        final int b2 = data.readByte();
        return ((b2 & 0xff) << 16) | ((b1 & 0xff) << 8) | (b0 & 0xff); // little endian
    }

    static void writeInt3(int value, @NotNull ByteBuf data) {
        data.writeByte(value);
        data.writeByte(value >> 8);
        data.writeByte(value >> 16); // little endian
    }

    static long readEncoded(@NotNull ByteBuf data) {
        final int first = data.readUnsignedByte();
        switch (first) {
            default:
                return first;
            case 0xfb:
                // no break here
            case 0xfc:
                return data.readUnsignedShortLE();
            case 0xfd:
                return readInt3(data);
            case 0xfe:
                return data.readLongLE();
            case 0xff:
                throw new RuntimeException();
        }
    }

    static void writeEncoded(long value, @NotNull ByteBuf data) {
        if (value >= 0) {
            if (value < 0xfb) {
                data.writeByte((byte) value);
                return;
            } else if (value < 0x1_00_00) {
                data.writeByte((byte) 0xfc);
                data.writeShortLE((int) value);
                return;
            } else if (value < 0x1_00_00_00) {
                data.writeIntLE((((int) value) << 8) | 0xfd);
                return;
            }
        }
        data.writeByte((byte) 0xfe);
        data.writeLongLE(value);
    }

    static void readEncodedToSkip(@NotNull ByteBuf data) {
        final long length = readEncoded(data);
        if (!(0 <= length && length <= data.readableBytes())) {
            throw new IndexOutOfBoundsException();
        }
        data.skipBytes((int) length);
    }

    @NotNull
    static byte[] readBytes(@NotNull ByteBuf srcBuf, int length) {
        final byte[] array = new byte[length];
        srcBuf.readBytes(array);
        return array;
    }

    @NotNull
    static String readString(@NotNull ByteBuf srcBuf, int byteLength) {
        final String string = srcBuf.toString(srcBuf.readerIndex(), byteLength, StandardCharsets.UTF_8);
        srcBuf.skipBytes(byteLength);
        return string;
    }

    @NotNull
    static byte[] readEncodedBytes(@NotNull ByteBuf srcBuf) {
        final long length = readEncoded(srcBuf);
        if (!(0 <= length && length <= Math.min(srcBuf.readableBytes(), MAX_SIZE))) {
            throw new IndexOutOfBoundsException();
        }
        return readBytes(srcBuf, (int) length);
    }

    @NotNull
    static String readEncodedString(@NotNull ByteBuf srcBuf) {
        final long byteLength = readEncoded(srcBuf);
        if (!(0 <= byteLength && byteLength <= Math.min(srcBuf.readableBytes(), MAX_SIZE))) {
            throw new IndexOutOfBoundsException();
        }
        return readString(srcBuf, (int) byteLength);
    }

    @NotNull
    static String readEncodedHexDump(@NotNull ByteBuf srcBuf) {
        final long byteLength = readEncoded(srcBuf);
        if (!(0 <= byteLength && byteLength <= Math.min(srcBuf.readableBytes(), MAX_SIZE))) {
            throw new IndexOutOfBoundsException();
        }
        final String hexDump = ByteBufUtil.hexDump(srcBuf, srcBuf.readerIndex(), (int) byteLength);
        srcBuf.skipBytes((int) byteLength);
        return hexDump;
    }

    static void writeEncodedBytes(@NotNull byte[] srcArray, @NotNull ByteBuf dstBuf) {
        writeEncoded(srcArray.length, dstBuf);
        dstBuf.writeBytes(srcArray);
    }

    static void writeEncodedString(@Nullable String srcString, @NotNull ByteBuf dstBuf) {
        if (srcString != null) {
            writeEncodedBytes(srcString.getBytes(StandardCharsets.UTF_8), dstBuf);
        } else {
            dstBuf.writeByte(0);
        }
    }

    @NotNull
    static byte[] readNullTerminatedBytes(@NotNull ByteBuf srcBuf) {
        final int length = srcBuf.bytesBefore((byte) 0);
        if (length == -1) {
            throw new RuntimeException();
        }
        final byte[] bytes = readBytes(srcBuf, length);
        srcBuf.readByte(); // skip null (0)
        return bytes;
    }

    @NotNull
    static String readNullTerminatedString(@NotNull ByteBuf srcBuf) {
        final int byteLength = srcBuf.bytesBefore((byte) 0);
        if (byteLength == -1) {
            throw new RuntimeException();
        }
        final String string = srcBuf.toString(srcBuf.readerIndex(), byteLength, StandardCharsets.UTF_8);
        srcBuf.skipBytes(byteLength + 1);
        return string;
    }

    static void writeNullTerminatedBytes(@NotNull byte[] srcArray, @NotNull ByteBuf dstBuf) {
        for (int value : srcArray) {
            if (value == 0) {
                throw new RuntimeException();
            }
        }
        dstBuf.writeBytes(srcArray).writeByte(0);
    }

    static void writeNullTerminatedString(@Nullable String srcString, @NotNull ByteBuf dstBuf) {
        final int index = dstBuf.writerIndex();
        dstBuf.writeCharSequence(srcString, StandardCharsets.UTF_8);
        if (dstBuf.bytesBefore(index, dstBuf.writerIndex() - index, (byte) 0) != -1) {
            throw new RuntimeException();
        }
        dstBuf.writeByte(0);
    }

    @NotNull
    static byte[] readRestBytes(@NotNull ByteBuf srcBuf) {
        return readBytes(srcBuf, srcBuf.readableBytes());
    }

    @NotNull
    static String readRestString(@NotNull ByteBuf srcBuf) {
        return readString(srcBuf, srcBuf.readableBytes());
    }
}
