package mujica.text.sequence;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.ToLongFunction;

@CodeHistory(date = "2026/9/14")
public enum StringHash64 implements ToLongFunction<CharSequence> {

    HF {
        @Override
        public long applyAsLong(@NotNull CharSequence string) {
            final int length = string.length();
            int result = 0;
            for (int index = 0; index < length; index++) {
                result += string.charAt(index) * 3L * index;
            }
            return result;
        }
    },
    @SuppressWarnings("SpellCheckingInspection")
    HFIP {
        @Override
        public long applyAsLong(@NotNull CharSequence string) {
            final int length = string.length();
            int result = 0;
            for (int index = 0; index < length; index++) {
                result += string.charAt(index & 0x3) ^ string.charAt(index);
            }
            return result;
        }
    }
    ;

    @Override
    public abstract long applyAsLong(@NotNull CharSequence string);
}
