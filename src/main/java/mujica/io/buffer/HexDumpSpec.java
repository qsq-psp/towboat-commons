package mujica.io.buffer;

import mujica.io.function.Base16Case;
import mujica.text.number.HexEncoder;
import org.intellij.lang.annotations.MagicConstant;

/**
 * Created in va on 2020/12/24, named HexDumpConfig.
 * Recreated in Ultramarine on 2022/6/15.
 * Recreated on 2025/3/3.
 */
public class HexDumpSpec extends HexEncoder {

    protected HexDumpSpec(@MagicConstant(valuesFromClass = Base16Case.class) int alphabetOffset) {
        super(alphabetOffset);
    }
}
