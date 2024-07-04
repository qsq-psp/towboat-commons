package indi.qsq.io.string;

import indi.qsq.util.reflect.ActuallyChar;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntPredicate;
import java.util.function.Predicate;

/**
 * Created on 2024/7/4.
 */
public final class PredictCharacteristic {

    public static class UTF16 {

        @NotNull
        public static Predicate<CharSequence> charSequencePredicate(@NotNull IntPredicate charPredicate) {
            return string -> {
                final int length = string.length();
                for (int index = 0; index < length; index++) {
                    if (charPredicate.test(string.charAt(index))) {
                        return true;
                    }
                }
                return false;
            };
        }

        @NotNull
        public static Predicate<char[]> charArrayPredicate(@NotNull IntPredicate charPredicate) {
            return ca -> {
                for (int ch : ca) {
                    if (charPredicate.test(ch)) {
                        return true;
                    }
                }
                return false;
            };
        }

        public static boolean isNull(@ActuallyChar int ch) {
            return ch == 0;
        }

        public static boolean isControl(@ActuallyChar int ch) {
            return ch < 0x20 || 0x7f <= ch && ch < 0xa0;
        }
    }
}
