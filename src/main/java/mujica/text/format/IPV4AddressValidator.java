package mujica.text.format;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

@CodeHistory(date = "2025/9/13")
public class IPV4AddressValidator extends AbstractFormatValidator.IntervalForm {

    public static final IPV4AddressValidator INSTANCE = new IPV4AddressValidator();

    private static final FailureMessage SEGMENT_EMPTY = new FailureMessage(
            "address segment is empty",
            "地址分段为空"
    );

    private static final FailureMessage LEADING_ZERO = new FailureMessage(
            "address segment {segment} contains leading zero",
            "地址分段 {segment} 包含前导零"
    );

    private static final FailureMessage SEGMENT_VALUE = new FailureMessage(
            "address segment value {segment} out of range",
            "地址分段的值 {segment} 为超出范围"
    );

    private static final FailureMessage CHARACTER = new FailureMessage(
            "address contains illegal character {char}",
            "地址中包含非法字符 {char}"
    );

    private static final FailureMessage SEGMENT_COUNT = new FailureMessage(
            "wrong address segment count",
            "地址分段数量错误"
    );

    @NotNull
    @Override
    LocalizedFailureMessage get(@Nullable Locale locale, @NotNull CharSequence string, int start, int end) {
        int segmentCount = 0;
        int value = -1;
        for (int index = start; index <= end; index++) {
            int ch;
            if (index == end) {
                ch = '.';
            } else {
                ch = string.charAt(index);
            }
            if (ch == '.') {
                if (value == -1) {
                    return localize(locale, SEGMENT_EMPTY);
                }
                start = index + 1;
                segmentCount++;
                value = -1;
            } else if ('0' <= ch && ch <= '9') {
                if (value == -1) {
                    value = ch - '0';
                } else if (value == 0) {
                    LocalizedFailureMessage message = localize(locale, LEADING_ZERO);
                    if (!(message instanceof BooleanFailureMessage)) {
                        for (index++; index < end; index++) {
                            if (string.charAt(index) == '.') {
                                break;
                            }
                        }
                        message = message.replace("{segment}", string.subSequence(start, index).toString());
                    }
                    return message;
                } else {
                    value = value * 10 + (ch - '0');
                    if (value > 255) {
                        LocalizedFailureMessage message = localize(locale, SEGMENT_VALUE);
                        if (!(message instanceof BooleanFailureMessage)) {
                            for (index++; index < end; index++) {
                                if (string.charAt(index) == '.') {
                                    break;
                                }
                            }
                            message = message.replace("{segment}", string.subSequence(start, index).toString());
                        }
                        return message;
                    }
                }
            } else {
                return localize(locale, CHARACTER).replaceQuoted("{char}", (char) ch);
            }
        }
        if (segmentCount != 4) {
            return localize(locale, SEGMENT_COUNT);
        }
        return BooleanFailureMessage.SUCCESS;
    }
}
