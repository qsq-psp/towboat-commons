package mujica.text.format;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

@CodeHistory(date = "2025/5/8")
public class CharsetNameValidator extends AbstractFormatValidator.IntervalForm {

    public static final CharsetNameValidator INSTANCE = new CharsetNameValidator();

    private static final FailureMessage EMPTY = new FailureMessage(
            "charset name is an empty string",
            "字符集名称是空字符串"
    );

    private static final FailureMessage START = new FailureMessage(
            "charset name must start with letter or digit",
            "字符集名称必须以字母或数字开头"
    );

    /**
     * The name 'part' is from Character.isJavaIdentifierPart()
     */
    private static final FailureMessage PART = new FailureMessage(
            "charset name should only contain letter, digit, or '-+:_.'",
            "字符集后续名称只应该包含字母、数字和五种符号'-+:_.'"
    );

    private static boolean isCharsetNameStart(int ch) {
        return 'A' <= ch && ch <= 'Z' || 'a' <= ch && ch <= 'z' || '0' <= ch && ch <= '9';
    }

    private static boolean isCharsetNamePart(int ch) {
        return isCharsetNameStart(ch) || "-+:_.".indexOf(ch) != -1;
    }

    @NotNull
    @Override
    LocalizedFailureMessage get(@Nullable Locale locale, @NotNull CharSequence string, int startIndex, int endIndex) {
        FailureMessage message;
        if (startIndex >= endIndex) {
            message = EMPTY;
        } else if (!isCharsetNameStart(string.charAt(startIndex))) {
            message = START;
        } else {
            message = null;
            for (int index = startIndex + 1; index < endIndex; index++) {
                if (!isCharsetNamePart(string.charAt(index))) {
                    message = PART;
                    break;
                }
            }
        }
        return localize(locale, message);
    }

    @Nullable
    @Override
    public String regularExpression() {
        return "[A-Za-z0-9][A-Za-z0-9\\-+:_.]*";
    }
}
