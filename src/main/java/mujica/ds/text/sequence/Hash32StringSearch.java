package mujica.ds.text.sequence;

import mujica.algebra.discrete.ModuloI32;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/4/28.
 */
public class Hash32StringSearch implements StringSearch {

    @NotNull
    final String pattern;

    final int multiplier;

    final int premultiplied;

    final int patternHash;

    public Hash32StringSearch(@NotNull String pattern, int multiplier) {
        super();
        this.pattern = pattern;
        this.multiplier = multiplier;
        final int patternLength = pattern.length();
        this.premultiplied = ModuloI32.INSTANCE.power(multiplier, patternLength);
        int hash = 0;
        for (int index = 0; index < patternLength; index++) {
            hash = hash * multiplier + pattern.charAt(index);
        }
        this.patternHash = hash;
    }

    @Override
    public int firstIndexOf(@NotNull CharSequence string) {
        final int patternLength = pattern.length();
        final int fullLength = string.length();
        int segmentHash = 0;
        int index;
        for (index = 0; index < patternLength; index++) {
            segmentHash = segmentHash * multiplier + string.charAt(index);
        }
        while (true) {
            if (segmentHash == patternHash && CharSequence.compare(string.subSequence(index - patternLength, index), pattern) == 0) {
                return index;
            }
            if (index < fullLength) {
                segmentHash = segmentHash * multiplier + string.charAt(index) - premultiplied * string.charAt(index - patternLength);
                index++;
            } else {
                break;
            }
        }
        return -1;
    }
}
