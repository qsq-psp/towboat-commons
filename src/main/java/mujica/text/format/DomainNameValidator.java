package mujica.text.format;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

@CodeHistory(date = "2021/4/23", project = "webbiton", name = "DomainNames")
@CodeHistory(date = "2025/4/30")
@ReferencePage(title = "The Domain Naming Convention for Internet User Applications", href = "https://www.rfc-editor.org/rfc/rfc819.html")
public class DomainNameValidator extends AbstractFormatValidator.IntervalForm {

    public static final DomainNameValidator INSTANCE = new DomainNameValidator();

    private static final FailureMessage EMPTY = new FailureMessage(
            "domain name is an empty string",
            "域名是空字符串"
    );

    private static final FailureMessage LONG = new FailureMessage(
            "domain name is too long",
            "域名太长"
    );

    private static final FailureMessage NESTING = new FailureMessage(
            "brackets nest too much",
            "括号嵌套过多"
    );

    private static final FailureMessage PAIRING = new FailureMessage(
            "brackets not paired",
            "括号不配对"
    );

    private static final FailureMessage SEGMENT_SHORT = new FailureMessage(
            "name segment {segment} is too short",
            "名称片段 {segment} 太短"
    );

    private static final FailureMessage SEGMENT_LONG = new FailureMessage(
            "name segment {segment} is too long",
            "名称片段 {segment} 太长"
    );

    private static final FailureMessage ADDRESS = new FailureMessage(
            "wrong net address format {segment}",
            "错误的网络地址格式 {segment}"
    );

    private static final FailureMessage LETTER = new FailureMessage(
            "name segment {segment} only starts with letter",
            "名称片段 {segment} 只能以字母开头"
    );

    private static final FailureMessage ALPHANUMERIC = new FailureMessage(
            "name segment {segment} only ends with letter or digit",
            "名称片段 {segment} 只能以字母或数字结尾"
    );

    private static final FailureMessage ALPHANUMERIC_HYPHEN = new FailureMessage(
            "name segment {segment} only contains letter, digit or hyphen in the middle",
            "名称片段 {segment} 中间只能包含字母、数字或连词符"
    );

    @NotNull
    LocalizedFailureMessage getForSegment(@Nullable Locale locale, @NotNull CharSequence string, int start, int end) {
        {
            int length = end - start;
            if (length < 2) {
                return localize(locale, SEGMENT_SHORT);
            }
            if (length >= 64) {
                return localize(locale, SEGMENT_LONG);
            }
        }
        int ch = string.charAt(start++);
        if (ch == '[') {
            ch = string.charAt(--end);
            if (ch != ']') {
                return localize(locale, ADDRESS);
            }
            for (int index = start; index < end; index++) {
                ch = string.charAt(index);
                if (ch == '.') {
                    return IPV4AddressValidator.INSTANCE.get(locale, string, start, end);
                } else if (ch == ':') {
                    return IPV6AddressValidator.INSTANCE.get(locale, string, start, end);
                }
            }
            return localize(locale, ADDRESS);
        } else if (ch == '#') {
            while (start < end) {
                ch = string.charAt(start++);
                if (!('0' <= ch && ch <= '9')) {
                    return localize(locale, ADDRESS);
                }
            }
        } else {
            if (!('A' <= ch && ch <= 'Z' || 'a' <= ch && ch <= 'z')) {
                return localize(locale, LETTER);
            }
            ch = string.charAt(--end);
            if (!('A' <= ch && ch <= 'Z' || 'a' <= ch && ch <= 'z' || '0' <= ch && ch <= '9')) {
                return localize(locale, ALPHANUMERIC);
            }
            while (start < end) {
                ch = string.charAt(start++);
                if (!('A' <= ch && ch <= 'Z' || 'a' <= ch && ch <= 'z' || '0' <= ch && ch <= '9' || ch == '-')) {
                    return localize(locale, ALPHANUMERIC_HYPHEN);
                }
            }
        }
        return BooleanFailureMessage.SUCCESS;
    }

    @NotNull
    @Override
    LocalizedFailureMessage get(@Nullable Locale locale, @NotNull CharSequence string, int start, int end) {
        {
            int length = end - start;
            if (length <= 0) {
                return localize(locale, EMPTY);
            }
            if (length >= 256) {
                return localize(locale, LONG);
            }
        }
        boolean inside = false;
        for (int index = start; index <= end; index++) {
            int ch;
            boolean divide;
            if (index == end) {
                ch = '.';
                divide = true;
            } else {
                ch = string.charAt(index);
                divide = !inside && (ch == '.');
            }
            if (divide) {
                LocalizedFailureMessage message = getForSegment(locale, string, start, index);
                if (!(message instanceof BooleanFailureMessage)) {
                    return message.replace("{segment}", string.subSequence(start, index).toString());
                } else if (message != BooleanFailureMessage.SUCCESS) {
                    return message;
                }
                start = index + 1;
            } else if (ch == '[') {
                if (inside) {
                    return localize(locale, NESTING);
                } else {
                    inside = true;
                }
            } else if (ch == ']') {
                if (inside) {
                    inside = false;
                } else {
                    return localize(locale, PAIRING);
                }
            }
        }
        if (inside) {
            return localize(locale, PAIRING);
        }
        return BooleanFailureMessage.SUCCESS;
    }

    @Deprecated
    private static FailureMessage failureMessage(String name, int startIndex, int endIndex) {
        {
            int length = endIndex - startIndex;
            if (length < 2) {
                return SEGMENT_SHORT;
            }
            if (length >= 64) {
                return SEGMENT_LONG;
            }
        }
        int ch = name.charAt(startIndex++);
        if (ch == '[') {
            ch = name.charAt(--endIndex);
            if (ch != ']') {
                return ADDRESS;
            }
            // use regex to validate IPv4 or IPv6
        } else if (ch == '#') {
            while (startIndex < endIndex) {
                ch = name.charAt(startIndex++);
                if (!('0' <= ch && ch <= '9')) {
                    return ADDRESS;
                }
            }
        } else {
            if (!('A' <= ch && ch <= 'Z' || 'a' <= ch && ch <= 'z')) {
                return LETTER;
            }
            ch = name.charAt(--endIndex);
            if (!('A' <= ch && ch <= 'Z' || 'a' <= ch && ch <= 'z' || '0' <= ch && ch <= '9')) {
                return ALPHANUMERIC;
            }
            while (startIndex < endIndex) {
                ch = name.charAt(startIndex++);
                if (!('A' <= ch && ch <= 'Z' || 'a' <= ch && ch <= 'z' || '0' <= ch && ch <= '9' || ch == '-')) {
                    return ALPHANUMERIC_HYPHEN;
                }
            }
        }
        return null;
    }

    @Deprecated
    private static FailureMessage failureMessage(String name) {
        if (name == null) {
            return NULL;
        }
        final int length = name.length();
        if (length == 0) {
            return EMPTY;
        }
        if (length >= 256) {
            return LONG;
        }
        boolean inside = false;
        int index0 = 0;
        for (int index1 = 0; index1 < length; index1++) {
            int ch = name.charAt(index1);
            if (ch == '[') {
                if (inside) {
                    return ADDRESS;
                } else {
                    inside = true;
                }
            } else if (ch == ']') {
                if (inside) {
                    inside = false;
                } else {
                    return ADDRESS;
                }
            } else if (ch == '.') {
                FailureMessage fm = failureMessage(name, index0, index1);
                if (fm != null) {
                    return fm;
                }
                index0 = index1 + 1;
            }
        }
        return failureMessage(name, index0, length);
    }
}
