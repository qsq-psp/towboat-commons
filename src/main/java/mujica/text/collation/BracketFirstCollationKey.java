package mujica.text.collation;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.text.CollationKey;

@CodeHistory(date = "2026/2/22")
public class BracketFirstCollationKey extends CollationKey {

    BracketFirstCollationKey(@NotNull String source) {
        super(source);
    }

    @Override
    public int compareTo(@NotNull CollationKey that) {
        return 0;
    }

    @Override
    public byte[] toByteArray() {
        return new byte[0];
    }
}
