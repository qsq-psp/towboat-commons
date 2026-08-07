package mujica.text.collation;

import java.text.CollationKey;
import java.text.Collator;

/**
 * Created on 2026/2/22.
 */
public class BracketFirstCollator extends Collator {

    @Override
    public int compare(String source, String target) {
        return 0;
    }

    @Override
    public CollationKey getCollationKey(String source) {
        return null;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
