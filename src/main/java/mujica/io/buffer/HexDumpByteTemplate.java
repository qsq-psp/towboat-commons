package mujica.io.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.Charset;

@CodeHistory(date = "2020/12/24", project = "va", name = "HexDumpConfig")
@CodeHistory(date = "2022/6/15", project = "Ultramarine", name = "HexDumpConfig")
@CodeHistory(date = "2025/10/5")
public class HexDumpByteTemplate extends HexDumpTemplate {

    private static final long serialVersionUID = 0xac69a7a448c85bd8L;

    protected final byte placeholderByte;
    protected final int offsetOffset;
    protected final int hexOffset;
    protected final int hexOffsetByteDelta;
    protected final int hexOffsetColumnDelta;
    protected final int charOffset;
    protected final int charOffsetColumnDelta;
    @NotNull
    protected final byte[] lineTemplate;

    public HexDumpByteTemplate(@NotNull HexDumpBuilder builder, @NotNull Charset charset) {
        super(builder);
        final byte[] placeholderBytes = builder.getPlaceholder().getBytes(charset);
        if (placeholderBytes.length != 1) {
            throw new IllegalArgumentException();
        }
        placeholderByte = placeholderBytes[0];
        final ByteBuf buf = Unpooled.buffer();
        buf.writeCharSequence(builder.getPrefix(), charset);
        offsetOffset = buf.writerIndex();
        buf.writeZero(builder.getByteOffsetDigits()).writeCharSequence(builder.getRegionSeparator(), charset);
        hexOffset = buf.writerIndex();
        final byte[] smallSeparator = builder.getSmallSeparator().getBytes(charset);
        final byte[] hexColumnSeparator = builder.getHexColumnSeparator().getBytes(charset);
        hexOffsetByteDelta = smallSeparator.length + 2;
        hexOffsetColumnDelta = hexColumnSeparator.length - smallSeparator.length;
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            if (columnIndex != 0) {
                buf.writeBytes(hexColumnSeparator);
            }
            for (int byteIndex = 0; byteIndex < columnSize; byteIndex++) {
                if (byteIndex != 0) {
                    buf.writeBytes(smallSeparator);
                }
                buf.writeByte('H');
                buf.writeByte('H');
            }
        }
        buf.writeCharSequence(builder.getRegionSeparator(), charset);
        charOffset = buf.writerIndex();
        final byte[] charColumnSeparator = builder.getCharColumnSeparator().getBytes(charset);
        charOffsetColumnDelta = charColumnSeparator.length;
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            if (columnIndex != 0) {
                buf.writeBytes(charColumnSeparator);
            }
            buf.writeByte('*');
        }
        buf.writeCharSequence(builder.getSuffix(), charset);
        lineTemplate = new byte[buf.readableBytes()];
        buf.readBytes(lineTemplate);
    }

    private byte hex4(int nibble) {
        nibble &= 0xf;
        if (nibble < 0xa) {
            return (byte) ('0' + nibble);
        } else {
            return (byte) (alphabetOffset + nibble);
        }
    }

    private void hex8(int octet, int templateOffset) {
        lineTemplate[templateOffset] = hex4(octet >> 4);
        lineTemplate[templateOffset + 1] = hex4(octet);
    }

    private void setOffsetNumber(long offsetValue) {
        int templateOffset = offsetOffset;
        for (int shift = byteOffsetShiftStart; shift >= 0; shift -= 4) {
            lineTemplate[templateOffset++] = hex4((int) (offsetValue >> shift));
        }
    }

    private void setVisibleChar(int octet, int templateOffset) {
        if (octet < 0x20 || octet >= 0x7f) {
            octet = placeholderByte;
        }
        lineTemplate[templateOffset] = (byte) octet;
    }

    public void setData(@NotNull byte[] array, int srcOffset, long dstOffset) {
        setOffsetNumber(dstOffset);
        int templateHexOffset = hexOffset;
        int templateCharOffset = charOffset;
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            for (int byteIndex = 0; byteIndex < columnSize; byteIndex++) {
                int octet = array[srcOffset++];
                hex8(octet, templateHexOffset);
                setVisibleChar(octet, templateCharOffset);
                templateHexOffset += hexOffsetByteDelta;
                templateCharOffset++;
            }
            templateHexOffset += hexOffsetColumnDelta;
            templateCharOffset += charOffsetColumnDelta;
        }
    }

    public int setData(@NotNull byte[] array, int srcOffset, int length, int lineStart, long dstOffset) {
        setOffsetNumber(dstOffset);
        int count = 0;
        int templateHexOffset = hexOffset;
        int templateCharOffset = charOffset;
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            for (int byteIndex = 0; byteIndex < columnSize; byteIndex++) {
                if (lineStart-- <= 0) {
                    if (length-- <= 0) {
                        return count;
                    }
                    int octet = array[srcOffset++];
                    hex8(octet, templateHexOffset);
                    setVisibleChar(octet, templateCharOffset);
                    count++;
                }
                templateHexOffset += hexOffsetByteDelta;
                templateCharOffset++;
            }
            templateHexOffset += hexOffsetColumnDelta;
            templateCharOffset += charOffsetColumnDelta;
        }
        return count;
    }

    @CodeHistory(date = "2025/10/22")
    private class DumpInputStream extends FilterInputStream {

        long offsetNumber;

        int writerIndex = -1;

        DumpInputStream(@NotNull InputStream in) {
            super(in);
        }

        @Override
        public int read(@NotNull byte[] array, int offset, int length) throws IOException {
            if (length <= 0) {
                return length;
            }
            if (writerIndex == -1) {
                int readerIndex = 0;
                int readerRemaining = bytesPerLine - readerIndex;
                while (readerRemaining > 0) {
                    int count = Math.min(length, readerRemaining);
                    count = in.read(array, offset, count);
                    if (count <= 0) {
                        return count;
                    }
                    count = setData(array, offset, count, readerIndex, offsetNumber);
                    if (count <= 0) {
                        return count; // never
                    }
                    readerIndex += count;
                    readerRemaining -= count;
                }
                writerIndex = 0;
            }
            final int writerRemaining = lineTemplate.length - writerIndex;
            assert writerRemaining > 0;
            length = Math.min(length, writerRemaining);
            System.arraycopy(lineTemplate, writerIndex, array, offset, length);
            if (writerRemaining == length) {
                offsetNumber += bytesPerLine;
                writerIndex = -1;
            } else {
                writerIndex += length;
            }
            return length;
        }
    }

    @CodeHistory(date = "2025/10/22")
    private class DumpOutputStream extends FilterOutputStream {

        long offsetNumber;

        int readerIndex;

        DumpOutputStream(@NotNull OutputStream out) {
            super(out);
        }
    }
}
