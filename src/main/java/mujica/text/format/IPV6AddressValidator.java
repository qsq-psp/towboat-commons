package mujica.text.format;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

@CodeHistory(date = "2025/9/13")
public class IPV6AddressValidator extends AbstractFormatValidator.IntervalForm {

    public static final IPV6AddressValidator INSTANCE = new IPV6AddressValidator();

    private static final FailureMessage SEGMENT_EMPTY = new FailureMessage(
            "address segment is empty",
            "地址分段为空"
    );

    private static final FailureMessage SEGMENT_LONG = new FailureMessage(
            "address segment {segment} is too long",
            "地址分段 {segment} 过长"
    );

    private static final FailureMessage CHARACTER = new FailureMessage(
            "address contains illegal character {char}",
            "地址中包含非法字符 {char}"
    );

    private static final FailureMessage SEGMENT_COUNT = new FailureMessage(
            "wrong address segment count {count}",
            "地址分段数量 {count} 错误"
    );

    private static final FailureMessage COMPRESSED_SEGMENT_COUNT = new FailureMessage(
            "wrong compressed address segment count {count}",
            "压缩地址分段数量 {count} 错误"
    );

    private static final int STATE_START = 0;
    private static final int STATE_START_SINGLE = 1;
    private static final int STATE_START_DOUBLE = 2;
    private static final int STATE_FREE = 3;
    private static final int STATE_SINGLE = 4;
    private static final int STATE_DOUBLE = 5;

    @NotNull
    @Override
    LocalizedFailureMessage get(@Nullable Locale locale, @NotNull CharSequence string, int startIndex, int endIndex) {
        int segmentCount = 0;
        int compressedSegmentCount = 0;
        {
            int state = STATE_START;
            int length = 0;
            for (int index = startIndex; index < endIndex; index++) {
                int ch = string.charAt(index);
                if (ch == ':') {
                    if (state == STATE_START_DOUBLE || state == STATE_DOUBLE) {
                        return localize(locale, SEGMENT_EMPTY);
                    }
                    state++;
                    if (length > 4) {
                        return localize(locale, SEGMENT_LONG).replaceQuoted("{segment}", string.subSequence(index - length, index).toString());
                    }
                    length = 0;
                    continue;
                }
                if ('0' <= ch && ch <= '9' || 'A' <= ch && ch <= 'F' || 'a' <= ch && ch <= 'f') {
                    if (state == STATE_START_SINGLE) {
                        return localize(locale, SEGMENT_EMPTY);
                    }
                    if (state == STATE_START_DOUBLE || state == STATE_DOUBLE) {
                        compressedSegmentCount++;
                    }
                    if (state == STATE_START || state == STATE_SINGLE) {
                        segmentCount++;
                    }
                    state = STATE_FREE;
                    length++;
                    continue;
                }
                return localize(locale, CHARACTER).replaceQuoted("{char}", (char) ch);
            }
            if (state == STATE_START_SINGLE || state == STATE_SINGLE) {
                return localize(locale, SEGMENT_EMPTY);
            }
            if (state == STATE_START_DOUBLE || state == STATE_DOUBLE) {
                compressedSegmentCount++;
            }
            if (length > 4) {
                return localize(locale, SEGMENT_LONG).replaceQuoted("{segment}", string.subSequence(endIndex - length, endIndex).toString());
            }
        }
        if (compressedSegmentCount == 0) {
            if (segmentCount != 8) {
                return localize(locale, SEGMENT_COUNT).replace("{count}", segmentCount);
            }
        } else if (compressedSegmentCount == 1) {
            if (segmentCount >= 8) {
                return localize(locale, SEGMENT_COUNT).replace("{count}", segmentCount);
            }
        } else {
            return localize(locale, COMPRESSED_SEGMENT_COUNT).replace("{count}", compressedSegmentCount);
        }
        return BooleanFailureMessage.SUCCESS;
    }
}
