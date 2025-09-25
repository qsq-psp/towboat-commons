package mujica.text.format;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

@CodeHistory(date = "2025/4/16")
@ReferencePage(title = "MySQL 8.0 Schema Object Names", language = "en", href = "https://dev.mysql.com/doc/refman/8.0/en/identifiers.html")
public class SchemaObjectNameValidator extends AbstractFormatValidator.IntervalForm {

    public static final SchemaObjectNameValidator INSTANCE = new SchemaObjectNameValidator();

    private static final FailureMessage EMPTY = new FailureMessage(
            "name is an empty string",
            "名称是空字符串"
    );

    private static final FailureMessage LONG = new FailureMessage(
            "name is too long",
            "名称太长"
    );

    private static final FailureMessage END = new FailureMessage(
            "name can not end with space",
            "名称不能以半角空格结束"
    );

    private static final FailureMessage ALL_NUMBER = new FailureMessage(
            "name can not consist solely of digits",
            "名称不能是纯数字"
    );

    private static final FailureMessage RESERVED_CHARACTER = new FailureMessage(
            "name contains forbidden reserved character",
            "名称包含禁止使用的保留字符"
    );

    @NotNull
    @Override
    LocalizedFailureMessage get(@Nullable Locale locale, @NotNull CharSequence string, int start, int end) {
        final int length = end - start;
        FailureMessage message;
        if (length <= 0) {
            message = EMPTY;
        } else if (length > 64) {
            message = LONG;
        } else if (string.charAt(end - 1) == ' ') {
            message = END;
        } else {
            message = null;
            boolean allNumber = true;
            for (int index = start; index < end; index++) {
                int ch = string.charAt(index);
                if ('0' <= ch && ch <= '9') {
                    continue;
                }
                allNumber = false;
                if (ch >= 0x80) {
                    continue;
                }
                if ('a' <= ch && ch <= 'z' || 'A' <= ch && ch <= 'Z') {
                    continue;
                }
                if (ch == '$' || ch == '_') {
                    continue;
                }
                message = RESERVED_CHARACTER;
                break;
            }
            if (allNumber) {
                message = ALL_NUMBER;
            }
        }
        return localize(locale, message);
    }
}
