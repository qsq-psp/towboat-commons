package mujica.io.buffer;

import mujica.text.number.DataSizeStyle;
import mujica.io.codec.Base16Case;
import mujica.text.number.HexEncoder;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/3/3.
 */
public class HexBytes extends HexEncoder {

    public static final int STYLE_LINE_FEED = 0x100;

    public static final int STYLE_NAMED = 0x200;

    @NotNull
    protected final DataSizeStyle segmentStyle;

    /**
     * separator: visible characters
     * delimiter: both visible and invisible characters
     */
    @NotNull
    protected final String byteDelimiter;

    protected HexBytes(@MagicConstant(valuesFromClass = Base16Case.class) int alphabetOffset, @NotNull DataSizeStyle segmentStyle, @NotNull String byteDelimiter) {
        super(alphabetOffset);
        this.segmentStyle = segmentStyle;
        this.byteDelimiter = byteDelimiter;
    }
}
