package indi.qsq.io.string;

import org.jetbrains.annotations.NotNull;

/**
 * Created on 2024/6/29.
 */
public class UTF32String extends IOString {

    @NotNull
    int[] codePoints;

    public UTF32String(@NotNull String string) {
        super(string);
        codePoints = string.codePoints().toArray();
    }

    @Override
    public int byteLength() {
        int length = 0;
        for (int codePoint : codePoints) {
            if (codePoint < 0x0080) {
                length++;
            } else if (codePoint < 0x0800) {
                length += 2;
            } else if (codePoint < 0x10000) {
                length += 3;
            } else {
                length += 4;
            }
        }
        return length;
    }

    @Override
    public int codePointLength() {
        return codePoints.length;
    }
}
