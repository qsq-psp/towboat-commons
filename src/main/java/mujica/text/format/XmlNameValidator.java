package mujica.text.format;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

@CodeHistory(date = "2025/9/13")
@ReferencePage(title = "Extensible Markup Language (XML) 1.0 (Fifth Edition)", language = "en", href = "https://www.w3.org/TR/REC-xml/#d0e804")
public class XmlNameValidator extends AbstractFormatValidator.IntervalForm {

    private static final FailureMessage EMPTY = new FailureMessage(
            "xml name is an empty string",
            "XML名称是空字符串"
    );

    private static final FailureMessage START = new FailureMessage(
            "xml name starts with illegal character {char}",
            "XML名称开头是非法字符 {char}"
    );

    /**
     * The name 'part' is from Character.isJavaIdentifierPart()
     */
    private static final FailureMessage PART = new FailureMessage(
            "xml name continues with illegal character {char} at index {index}",
            "XML名称后续在下标 {index} 处包含非法字符 {char}"
    );

    public static boolean isXmlNameStart(int ch) {
        if (ch < 0xc0) {
            return 'A' <= ch && ch <= 'Z' || 'a' <= ch && ch <= 'z' || ch == ':' || ch == '_';
        } else if (ch <= 0x3000) {
            return ch <= 0x2ff && ch != 0xf7 || 0x370 <= ch && ch <= 0x1fff && ch != 0x37e || 0x200c <= ch && ch <= 0x200d || 0x2070 <= ch && ch <= 0x218f || 0x2c00 <= ch && ch <= 0x2fef;
        } else {
            return ch <= 0xd7ff || 0xf900 <= ch && ch <= 0xfdcf || 0xfdf0 <= ch && ch <= 0xfffd;
        }
    }

    public static boolean isXmlNamePart(int ch) {
        if (ch < 0xc0) {
            if (ch <= 'Z') {
                return 'A' <= ch || '0' <= ch && ch <= '9' || ch == ':' || ch == '-' || ch == '.';
            } else {
                return 'a' <= ch && ch <= 'z' || ch == '_' || ch == 0xb7;
            }
        } else if (ch <= 0x3000) {
            return ch <= 0x2ff && ch != 0xf7 || 0x370 <= ch && ch <= 0x1fff && ch != 0x37e || 0x200c <= ch && ch <= 0x200d || 0x2070 <= ch && ch <= 0x218f || 0x2c00 <= ch && ch <= 0x2fef;
        } else {
            return ch <= 0xd7ff || 0xf900 <= ch && ch <= 0xfdcf || 0xfdf0 <= ch && ch <= 0xfffd;
        }
    }

    @NotNull
    @Override
    LocalizedFailureMessage get(@Nullable Locale locale, @NotNull CharSequence string, int start, int end) {
        if (start >= end) {
            return localize(locale, EMPTY);
        } else if (!isXmlNameStart(string.charAt(start))) {
            return localize(locale, START).replaceQuoted("{char}", string.charAt(start));
        } else {
            for (int index = start + 1; index < end; index++) {
                char ch = string.charAt(index);
                if (!isXmlNamePart(ch)) {
                    return localize(locale, PART).replace("{index}", index - start).replaceQuoted("{char}", ch);
                }
            }
        }
        return BooleanFailureMessage.SUCCESS;
    }
}
