package mujica.io.buffer;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.*;

@CodeHistory(date = "2025/10/5")
public class HexDumpCharTemplate extends HexDumpTemplate {

    private static final long serialVersionUID = 0xefe5148444d55b29L;

    protected final char placeholderChar;
    protected final int offsetOffset;
    protected final int hexOffset;
    protected final int hexOffsetByteDelta;
    protected final int hexOffsetColumnDelta;
    protected final int charOffset;
    protected final int charOffsetColumnDelta;
    @NotNull
    protected final char[] lineTemplate;

    public HexDumpCharTemplate(@NotNull HexDumpBuilder builder) {
        super(builder);
        final String placeholder = builder.getPlaceholder();
        if (placeholder.length() != 1) {
            throw new IllegalArgumentException();
        }
        placeholderChar = placeholder.charAt(0);
        final StringBuilder sb = new StringBuilder();
        sb.append(builder.getPrefix());
        offsetOffset = sb.length();
        sb.append("S".repeat(builder.getByteOffsetDigits())).append(builder.getRegionSeparator());
        hexOffset = sb.length();
        hexOffsetByteDelta = builder.getSmallSeparator().length() + 2;
        hexOffsetColumnDelta = builder.getHexColumnSeparator().length() - builder.getSmallSeparator().length();
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            if (columnIndex != 0) {
                sb.append(builder.getHexColumnSeparator());
            }
            for (int byteIndex = 0; byteIndex < columnSize; byteIndex++) {
                if (byteIndex != 0) {
                    sb.append(builder.getSmallSeparator());
                }
                sb.append("HH");
            }
        }
        charOffset = sb.append(builder.getRegionSeparator()).length();
        charOffsetColumnDelta = builder.getCharColumnSeparator().length();
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            if (columnIndex != 0) {
                sb.append(builder.getCharColumnSeparator());
            }
            sb.append("*");
        }
        lineTemplate = sb.append(builder.getSuffix()).toString().toCharArray();
    }

    private char hex4(int nibble) {
        nibble &= 0xf;
        if (nibble < 0xa) {
            return (char) ('0' + nibble);
        } else {
            return (char) (alphabetOffset + nibble);
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
            octet = placeholderChar;
        }
        lineTemplate[templateOffset] = (char) octet;
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
    private class DumpReader extends Reader {

        @NotNull
        final InputStream in;

        final byte[] buffer = new byte[bytesPerLine];

        long hexOffset;

        int outPosition = -1;

        private DumpReader(@NotNull InputStream in) {
            super();
            this.in = in;
        }

        @Override
        public int read(@NotNull char[] array, int offset, int length) throws IOException {
            if (outPosition == -1) {
                int inPosition = 0;
                while (inPosition < bytesPerLine) {
                    int count = in.read(buffer, 0, bytesPerLine);
                    if (count <= 0) {
                        return count;
                    }
                    count = setData(buffer, 0, count, inPosition, hexOffset);
                    if (count <= 0) {
                        return count; // never
                    }
                    inPosition += count;
                }
                outPosition = 0;
            }
            length = Math.min(length, lineTemplate.length - outPosition);
            System.arraycopy(lineTemplate, outPosition, array, offset, length);
            outPosition += length;
            if (outPosition < lineTemplate.length) {
                return length;
            }
            hexOffset += bytesPerLine;
            outPosition = -1;
            return length;
        }

        @Override
        public void close() throws IOException {
            in.close();
        }
    }

    @CodeHistory(date = "2025/12/18")
    private class DumpOutputStream extends OutputStream {

        @NotNull
        final Writer out;

        long hexOffset;

        int inPosition;

        private DumpOutputStream(@NotNull Writer out) {
            super();
            this.out = out;
        }

        @Override
        public void write(int b) throws IOException {
            write(new byte[] {(byte) b}, 0, 1);
        }

        @Override
        public void write(@NotNull byte[] array, int offset, int length) throws IOException {
            while (true) {
                if (inPosition < bytesPerLine) {
                    if (length > 0) {
                        int count = setData(array, offset, length, inPosition, hexOffset);
                        inPosition += count;
                        offset += count;
                        length -= count;
                    } else {
                        break;
                    }
                } else {
                    out.write(lineTemplate);
                    hexOffset += bytesPerLine;
                    inPosition = 0;
                }
            }
        }
    }
}
