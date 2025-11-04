package mujica.io.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

@CodeHistory(date = "2020/12/24", project = "va", name = "HexDumpConfig")
@CodeHistory(date = "2022/6/15", project = "Ultramarine", name = "HexDumpConfig")
@CodeHistory(date = "2025/10/5")
public class HexDumpByteTemplate extends HexDumpTemplate {

    private static final long serialVersionUID = 0xAC69A7A448C85BD8L;

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

    @CodeHistory(date = "2025/10/22")
    private class DumpOutputStream extends FilterOutputStream {

        DumpOutputStream(@NotNull OutputStream out) {
            super(out);
        }
    }

    @CodeHistory(date = "2025/10/22")
    private class DumpInputStream extends FilterInputStream {

        DumpInputStream(@NotNull InputStream in) {
            super(in);
        }
    }
}
