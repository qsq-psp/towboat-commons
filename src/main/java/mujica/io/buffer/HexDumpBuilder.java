package mujica.io.buffer;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

@CodeHistory(date = "2025/2/9", project = "Ultramarine", name = "TerminalHexDump")
@CodeHistory(date = "2025/3/3", name = "HexDumpSpec")
@CodeHistory(date = "2025/10/20")
public class HexDumpBuilder implements Serializable {

    private static final long serialVersionUID = 0xF0851F1C4EDBD4C0L;

    private boolean upperCase;

    public boolean isUpperCase() {
        return upperCase;
    }

    @NotNull
    public HexDumpBuilder setUpperCase(boolean upperCase) {
        this.upperCase = upperCase;
        return this;
    }

    private int columnSize = 8;

    public int getColumnSize() {
        return columnSize;
    }

    @NotNull
    public HexDumpBuilder setColumnSize(int columnSize) {
        if (columnSize <= 0 || columnSize >= 0x8000) {
            throw new IllegalArgumentException();
        }
        this.columnSize = columnSize;
        return this;
    }

    private int columnCount = 2;

    public int getColumnCount() {
        return columnCount;
    }

    @NotNull
    public HexDumpBuilder setColumnCount(int columnCount) {
        if (columnCount <= 0 || columnCount >= 0x8000) {
            throw new IllegalArgumentException();
        }
        this.columnCount = columnCount;
        return this;
    }

    private int byteOffsetDigits = 6;

    public int getByteOffsetDigits() {
        return byteOffsetDigits;
    }

    @NotNull
    public HexDumpBuilder setByteOffsetDigits(int byteOffsetDigits) {
        if (byteOffsetDigits < 0 || byteOffsetDigits > 16) {
            throw new IllegalArgumentException();
        }
        this.byteOffsetDigits = byteOffsetDigits;
        return this;
    }

    private long byteOffsetStart = 0L;

    public long getByteOffsetStart() {
        return byteOffsetStart;
    }

    @NotNull
    public HexDumpBuilder setByteOffsetStart(long byteOffsetStart) {
        if (byteOffsetStart < 0L) {
            throw new IllegalArgumentException();
        }
        this.byteOffsetStart = byteOffsetStart;
        return this;
    }

    @NotNull
    private String placeholder = ".";

    @NotNull
    public String getPlaceholder() {
        return placeholder;
    }

    @NotNull
    public HexDumpBuilder setPlaceholder(@NotNull String placeholder) {
        this.placeholder = Objects.requireNonNull(placeholder);
        return this;
    }

    @NotNull
    private String prefix = "0x";

    @NotNull
    public String getPrefix() {
        return prefix;
    }

    @NotNull
    public HexDumpBuilder setPrefix(@NotNull String prefix) {
        this.prefix = Objects.requireNonNull(prefix);
        return this;
    }

    @NotNull
    private String suffix = System.lineSeparator();

    @NotNull
    public String getSuffix() {
        return suffix;
    }

    @NotNull
    public HexDumpBuilder setSuffix(@NotNull String suffix) {
        this.suffix = Objects.requireNonNull(suffix);
        return this;
    }

    @NotNull
    private String smallSeparator = " ";

    @NotNull
    public String getSmallSeparator() {
        return smallSeparator;
    }

    @NotNull
    public HexDumpBuilder setSmallSeparator(@NotNull String smallSeparator) {
        this.smallSeparator = Objects.requireNonNull(smallSeparator);
        return this;
    }

    @NotNull
    private String hexColumnSeparator = "  ";

    @NotNull
    public String getHexColumnSeparator() {
        return hexColumnSeparator;
    }

    @NotNull
    public HexDumpBuilder setHexColumnSeparator(@NotNull String hexColumnSeparator) {
        this.hexColumnSeparator = Objects.requireNonNull(hexColumnSeparator);
        return this;
    }

    @NotNull
    private String charColumnSeparator = " ";

    @NotNull
    public String getCharColumnSeparator() {
        return charColumnSeparator;
    }

    @NotNull
    public HexDumpBuilder setCharColumnSeparator(@NotNull String charColumnSeparator) {
        this.charColumnSeparator = Objects.requireNonNull(charColumnSeparator);
        return this;
    }

    @NotNull
    private String regionSeparator = "  ";

    @NotNull
    public String getRegionSeparator() {
        return regionSeparator;
    }

    @NotNull
    public HexDumpBuilder setRegionSeparator(@NotNull String regionSeparator) {
        this.regionSeparator = Objects.requireNonNull(regionSeparator);
        return this;
    }

    public boolean isStyleCleared() {
        return negativeStyle.isReset() && invisibleStyle.isReset() && alphanumericStyle.isReset() && otherStyle.isReset();
    }

    @NotNull
    public HexDumpBuilder clearStyles() {
        negativeStyle = TerminalStyle.RESET_STYLE;
        invisibleStyle = TerminalStyle.RESET_STYLE;
        alphanumericStyle = TerminalStyle.RESET_STYLE;
        otherStyle = TerminalStyle.RESET_STYLE;
        return this;
    }

    @NotNull
    private TerminalStyle negativeStyle = TerminalStyle.RESET_STYLE; // negative byte value

    @NotNull
    public TerminalStyle getNegativeStyle() {
        return negativeStyle;
    }

    @NotNull
    private TerminalStyle invisibleStyle = TerminalStyle.RESET_STYLE; // control C0 and DEL, space not included

    @NotNull
    public TerminalStyle getInvisibleStyle() {
        return invisibleStyle;
    }

    @NotNull
    private TerminalStyle alphanumericStyle = TerminalStyle.RESET_STYLE; // digits and letters

    @NotNull
    public TerminalStyle getAlphanumericStyle() {
        return alphanumericStyle;
    }

    @NotNull
    private TerminalStyle otherStyle = TerminalStyle.RESET_STYLE; // space and visible symbols

    @NotNull
    public TerminalStyle getOtherStyle() {
        return otherStyle;
    }
}
