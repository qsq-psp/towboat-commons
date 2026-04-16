package mujica.ds.of_char.sequence;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.nio.CharBuffer;
import java.util.List;

@CodeHistory(date = "2026/3/14", project = "webbiton", name = "StringUtil")
@CodeHistory(date = "2021/10/26", project = "va", name = "CommonText")
@CodeHistory(date = "2022/5/19", project = "Ultramarine", name = "StringUtility")
@CodeHistory(date = "2023/4/20", project = "Ultramarine", name = "StringMetrics")
@CodeHistory(date = "2026/3/14")
public abstract class TowboatCharSequence implements CharSequence, Serializable {

    public static final List<Class<? extends CharSequence>> CHAR_SEQUENCE_CLASSES = List.of(
            String.class,
            StringBuilder.class,
            StringBuffer.class,
            CharBuffer.class,
            TowboatCharSequence.class
    );

    public static int commonPrefixLength(@NotNull CharSequence a, @NotNull CharSequence b) {
        final int length = Math.min(a.length(), b.length());
        int index = 0;
        while (index < length) {
            if (a.charAt(index) != b.charAt(index)) {
                break;
            }
            index++;
        }
        return index;
    }

    public static int commonSuffixLength(@NotNull CharSequence a, @NotNull CharSequence b) {
        int indexA = a.length();
        int indexB = b.length();
        int length = 0;
        while (indexA > 0 && indexB > 0) {
            if (a.charAt(--indexA) != b.charAt(--indexB)) {
                break;
            }
            length++;
        }
        return length;
    }

    @ReferencePage(title = "KMP", href = "https://oi-wiki.org/string/kmp/")
    @NotNull
    public static int[] prefixKMP(@NotNull CharSequence string) {
        final int length = string.length();
        final int[] array = new int[length];
        for (int i = 1; i < length; i++) {
            int j = array[i - 1];
            while (j > 0 && string.charAt(i) != string.charAt(j)) {
                j = array[j - 1];
            }
            if (string.charAt(i) == string.charAt(j)) {
                j++;
            }
            array[i] = j;
        }
        return array;
    }

    @NotNull
    public static int[] prefixZ(@NotNull CharSequence string) {
        final int length = string.length();
        final int[] array = new int[length];
        int left = 0;
        int right = 0;
        for (int i = 0; i < length; i++) {
            int z;
            if (i <= right && array[i - left] < right - i + 1) {
                z = array[i - left];
            } else {
                z = Math.max(0, right - i + 1);
                while (i + z < length && string.charAt(z) == string.charAt(i + z)) {
                    z++;
                }
            }
            if (i + z - 1 > right) {
                left = i;
                right = i + z - 1;
            }
            array[i] = z;
        }
        return array;
    }

    public static int commonSubsequenceLength(@NotNull CharSequence a, @NotNull CharSequence b) {
        if (b.length() > a.length()) {
            CharSequence c = b;
            b = a;
            a = c;
        }
        // now b is shorter
        final char[] charsB = toCharArray(b); // for faster access
        final int lengthB = charsB.length;
        int[] src = new int[lengthB + 1];
        int[] dst = new int[lengthB + 1];
        final int lengthA = a.length();
        for (int indexA = 0; indexA < lengthA; indexA++) {
            int ax = a.charAt(indexA);
            for (int indexB = 0; indexB < lengthB; indexB++) {
                if (ax == charsB[indexB]) {
                    dst[indexB + 1] = src[indexB] + 1;
                } else {
                    dst[indexB + 1] = Math.max(src[indexB + 1], dst[indexB]);
                }
            }
            int[] temp = src;
            src = dst;
            dst = temp;
        }
        return src[lengthB];
    }

    public static String commonSubsequence(@NotNull CharSequence a, @NotNull CharSequence b) {
        return ""; // todo
    }

    public static int editDistance(@NotNull CharSequence a, @NotNull CharSequence b) {
        if (b.length() > a.length()) {
            CharSequence c = b;
            b = a;
            a = c;
        }
        // now b is shorter
        final char[] charsB = toCharArray(b); // for faster access
        final int lengthB = charsB.length;
        int[] src = new int[lengthB + 1];
        int[] dst = new int[lengthB + 1];
        final int lengthA = a.length();
        for (int indexA = 0; indexA < lengthA; indexA++) {
            int ax = a.charAt(indexA);
            for (int indexB = 0; indexB < lengthB; indexB++) {
                if (ax == charsB[indexB]) {
                    dst[indexB + 1] = src[indexB];
                } else {
                    dst[indexB + 1] = Math.min(Math.min(src[indexB], src[indexB + 1]), dst[indexB]) + 1;
                }
            }
            int[] temp = src;
            src = dst;
            dst = temp;
        }
        return src[lengthB];
    }

    public static String commonSuperSequence(@NotNull CharSequence a, @NotNull CharSequence b) {
        return ""; // todo
    }

    private static double lengthDistance(int lengthA, int lengthB) {
        return (double) (Math.min(lengthA, lengthB) + 1) / (Math.max(lengthA, lengthB) + 1);
    }

    /**
     * @return 0 <= difference < 1
     */
    public static double difference(@NotNull CharSequence a, @NotNull CharSequence b) {
        final int lengthA = a.length();
        final int lengthB = b.length();
        return (editDistance(a, b) + lengthDistance(lengthA, lengthB)) / (lengthA + lengthB + 1);
    }

    private static final long serialVersionUID = 0x0390169F208201A6L;

    protected TowboatCharSequence() {
        super();
    }

    @Override
    public abstract int length();

    @Override
    public abstract char charAt(int index);

    @Override
    public CharSequence subSequence(int startIndex, int endIndex) {
        return new SlicedCharSequence(this, startIndex, endIndex);
    }

    @NotNull
    public static char[] toCharArray(@NotNull CharSequence string) {
        if (string instanceof String) {
            return ((String) string).toCharArray();
        }
        final int n = string.length();
        final char[] a = new char[n];
        for (int i = 0; i < n; i++) {
            a[i] = string.charAt(i);
        }
        return a;
    }

    @NotNull
    public char[] toCharArray() {
        final int n = length();
        final char[] a = new char[n];
        for (int i = 0; i < n; i++) {
            a[i] = charAt(i);
        }
        return a;
    }

    @NotNull
    @Override
    public String toString() {
        return new String(toCharArray());
    }
}
