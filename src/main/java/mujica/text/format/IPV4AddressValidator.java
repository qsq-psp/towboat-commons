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
            "地址分段的值 {segment} 超出范围"
    );

    private static final FailureMessage CHARACTER = new FailureMessage(
            "address contains illegal character {char}",
            "地址中包含非法字符 {char}"
    );

    private static final FailureMessage SEGMENT_COUNT = new FailureMessage(
            "wrong address segment count {count}",
            "地址分段数量 {count} 错误"
    );

    @NotNull
    @Override
    LocalizedFailureMessage get(@Nullable Locale locale, @NotNull CharSequence string, int startIndex, int endIndex) {
        int segmentCount = 0;
        {
            int value = -1;
            for (int index = startIndex; index <= endIndex; index++) {
                int ch;
                if (index == endIndex) {
                    ch = '.';
                } else {
                    ch = string.charAt(index);
                }
                if (ch == '.') {
                    if (value == -1) {
                        return localize(locale, SEGMENT_EMPTY);
                    }
                    startIndex = index + 1;
                    segmentCount++;
                    value = -1;
                } else if ('0' <= ch && ch <= '9') {
                    if (value == -1) {
                        value = ch - '0';
                    } else if (value == 0) {
                        LocalizedFailureMessage message = localize(locale, LEADING_ZERO);
                        if (!(message instanceof BooleanFailureMessage)) {
                            for (index++; index < endIndex; index++) {
                                if (string.charAt(index) == '.') {
                                    break;
                                }
                            }
                            message = message.replaceQuoted("{segment}", string.subSequence(startIndex, index).toString());
                        }
                        return message;
                    } else {
                        value = value * 10 + (ch - '0');
                        if (value > 255) {
                            LocalizedFailureMessage message = localize(locale, SEGMENT_VALUE);
                            if (!(message instanceof BooleanFailureMessage)) {
                                for (index++; index < endIndex; index++) {
                                    if (string.charAt(index) == '.') {
                                        break;
                                    }
                                }
                                message = message.replaceQuoted("{segment}", string.subSequence(startIndex, index).toString());
                            }
                            return message;
                        }
                    }
                } else {
                    return localize(locale, CHARACTER).replaceQuoted("{char}", (char) ch);
                }
            }
        }
        if (segmentCount != 4) {
            return localize(locale, SEGMENT_COUNT).replace("{count}", segmentCount);
        }
        return BooleanFailureMessage.SUCCESS;
    }
}
