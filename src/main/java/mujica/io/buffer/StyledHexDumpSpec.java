package mujica.io.buffer;

import mujica.io.function.Base16Case;
import org.intellij.lang.annotations.MagicConstant;

/**
 * Created on 2025/4/28.
 */
public class StyledHexDumpSpec extends HexDumpSpec {

    protected StyledHexDumpSpec(@MagicConstant(valuesFromClass = Base16Case.class) int alphabetOffset) {
        super(alphabetOffset);
    }
}
