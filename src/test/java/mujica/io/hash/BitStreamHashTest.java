package mujica.io.hash;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/2/", project = "OSHI", name = "SimpleBitsDigestTest")
@CodeHistory(date = "2025/5/18")
public class BitStreamHashTest {

    private void caseHash(@NotNull byte[] input, int padBitCount, @NotNull String expectedHexOutput,
                          @NotNull ByteBlockBitHashCore actualCore) {
        // ...
    }
}
