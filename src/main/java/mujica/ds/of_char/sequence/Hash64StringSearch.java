package mujica.ds.of_char.sequence;

import mujica.algebra.discrete.ModuloI64;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/5/11.
 */
public class Hash64StringSearch implements StringSearch {

    @NotNull
    final String pattern;

    final long multiplier;

    final long premultiplied;

    final long patternHash;

    public Hash64StringSearch(@NotNull String pattern, long multiplier) {
        super();
        this.pattern = pattern;
        this.multiplier = multiplier;
        final int patternLength = pattern.length();
        this.premultiplied = ModuloI64.INSTANCE.power(multiplier, patternLength);
        long hash = 0L;
        for (int index = 0; index < patternLength; index++) {
            hash = hash * multiplier + pattern.charAt(index);
        }
        this.patternHash = hash;
    }

    @Override
    public int firstIndexOf(@NotNull CharSequence string) {
        final int patternLength = pattern.length();
        final int fullLength = string.length();
        long segmentHash = 0L;
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
