package mujica.io.buffer;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

@CodeHistory(date = "2025/10/5")
public class HexDumpCharTemplate extends HexDumpTemplate {

    private static final long serialVersionUID = 0xEFE5148444D55B29L;

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

    private void offsetNumber(long offsetValue) {
        int templateOffset = offsetOffset;
        for (int shift = byteOffsetShiftStart; shift >= 0; shift -= 4) {
            lineTemplate[templateOffset++] = hex4((int) (offsetValue >> shift));
        }
    }

    private void charVisible(int octet, int templateOffset) {
        if (octet < 0x20 || octet >= 0x7f) {
            octet = placeholderChar;
        }
        lineTemplate[templateOffset] = (char) octet;
    }

    private void middleData(@NotNull byte[] array, int srcOffset) {
        int templateHexOffset = hexOffset;
        int templateCharOffset = charOffset;
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            for (int byteIndex = 0; byteIndex < columnSize; byteIndex++) {
                int octet = array[srcOffset++];
                hex8(octet, templateHexOffset);
                charVisible(octet, templateCharOffset);
                templateHexOffset += hexOffsetByteDelta;
                templateCharOffset++;
            }
            templateHexOffset += hexOffsetColumnDelta;
            templateCharOffset += charOffsetColumnDelta;
        }
    }

    public void setMiddleData(@NotNull byte[] array, int srcOffset, long dstOffset) {
        offsetNumber(dstOffset);
        middleData(array, srcOffset);
    }

    public void setData(@NotNull byte[] array, int srcOffset, int length, int lineOffset, long dstOffset) {
        //
    }

    @CodeHistory(date = "2025/10/22")
    private class DumpReader extends Reader {

        @NotNull
        final InputStream in;

        private DumpReader(@NotNull InputStream in) {
            super();
            this.in = in;
        }

        @Override
        public int read(@NotNull char[] array, int offset, int length) throws IOException {
            return 0;
        }

        @Override
        public void close() throws IOException {
            in.close();
        }
    }
}
