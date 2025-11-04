package mujica.text.format;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

@CodeHistory(date = "2025/9/11")
public abstract class AbstractFormatValidator implements FormatValidator {

    static final FailureMessage NULL = new FailureMessage(
            "string",
            "字符串是空值"
    );

    @NotNull
    LocalizedFailureMessage localize(@Nullable Locale locale, @Nullable FailureMessage message) {
        if (message == null) {
            return BooleanFailureMessage.SUCCESS;
        }
        if (locale == null) {
            return BooleanFailureMessage.FAIL;
        }
        if (locale.getLanguage().equals(Locale.CHINESE.getLanguage())) {
            return new StringFailureMessage(message.zh);
        } else {
            return new StringFailureMessage(message.en);
        }
    }

    @Nullable
    @Override
    public String regularExpression() {
        return null;
    }

    @CodeHistory(date = "2025/9/12")
    public static abstract class StringForm extends AbstractFormatValidator {

        @NotNull
        abstract LocalizedFailureMessage get(@Nullable Locale locale, @Nullable String string);

        @Override
        public boolean test(@Nullable CharSequence string) {
            return get(null, string != null ? string.toString() : null).get() == null;
        }

        @Override
        public boolean test(@NotNull CharSequence string, int start, int end) {
            return get(null, string.subSequence(start, end).toString()).get() == null;
        }

        @Nullable
        @Override
        public String message(@Nullable Locale locale, @Nullable CharSequence string) {
            if (locale == null) {
                locale = Locale.ROOT;
            }
            return get(locale, string != null ? string.toString() : null).get();
        }

        @Nullable
        @Override
        public String message(@Nullable Locale locale, @NotNull CharSequence string, int start, int end) {
            if (locale == null) {
                locale = Locale.ROOT;
            }
            return get(locale, string.subSequence(start, end).toString()).get();
        }
    }

    @CodeHistory(date = "2025/9/12")
    public static abstract class IntervalForm extends AbstractFormatValidator {

        @NotNull
        abstract LocalizedFailureMessage get(@Nullable Locale locale, @NotNull CharSequence string, int start, int end);

        @Override
        public boolean test(@Nullable CharSequence string) {
            if (string == null) {
                return false;
            }
            return get(null, string, 0, string.length()).get() == null;
        }

        @Override
        public boolean test(@NotNull CharSequence string, int start, int end) {
            return get(null, string, start, end).get() == null;
        }

        @Nullable
        @Override
        public String message(@Nullable Locale locale, @Nullable CharSequence string) {
            if (locale == null) {
                locale = Locale.ROOT;
            }
            if (string == null) {
                return localize(locale, NULL).get();
            }
            return get(locale, string, 0, string.length()).get();
        }

        @Nullable
        @Override
        public String message(@Nullable Locale locale, @NotNull CharSequence string, int start, int end) {
            if (locale == null) {
                locale = Locale.ROOT;
            }
            return get(locale, string, start, end).get();
        }
    }
}
