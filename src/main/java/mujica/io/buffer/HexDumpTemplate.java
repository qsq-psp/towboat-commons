package mujica.io.buffer;

import mujica.io.codec.Base16Case;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@CodeHistory(date = "2025/5/2")
public class HexDumpTemplate implements Base16Case, Serializable {

    private static final long serialVersionUID = 0xEBD07B75FB319109L;

    protected final long byteOffsetStart;
    protected final int byteOffsetShiftStart;
    protected final int columnSize;
    protected final int columnCount;
    protected final int bytesPerLine;
    protected final int alphabetOffset;

    protected HexDumpTemplate(@NotNull HexDumpBuilder builder) {
        super();
        byteOffsetStart = builder.getByteOffsetStart();
        byteOffsetShiftStart = (builder.getByteOffsetDigits() - 1) << 2;
        columnSize = builder.getColumnSize();
        columnCount = builder.getColumnCount();
        bytesPerLine = columnSize * columnCount;
        alphabetOffset = builder.isUpperCase() ? UPPER : LOWER;
    }
}
