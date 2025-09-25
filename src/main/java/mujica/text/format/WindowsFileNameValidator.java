package mujica.text.format;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Locale;

@CodeHistory(date = "2021/9/9", project = "webbiton", name = "FileNames")
@CodeHistory(date = "2025/4/1")
@ReferencePage(title = "命名文件、路径和命名空间", language = "zh", href = "https://learn.microsoft.com/zh-cn/windows/win32/fileio/naming-a-file")
public class WindowsFileNameValidator extends AbstractFormatValidator.StringForm {

    public static final WindowsFileNameValidator INSTANCE = new WindowsFileNameValidator();

    private static final FailureMessage NULL = new FailureMessage(
            "file name is null",
            "文件名是空值"
    );

    private static final FailureMessage LONG = new FailureMessage(
            "file name is too long ({length} chars)",
            "文件名太长（{length} 个字符）"
    );

    private static final FailureMessage END = new FailureMessage(
            "file name can not end with space or dot",
            "文件名不能以半角空格或英文句点结束"
    );

    /**
     * Character keys and String keys mixed together
     */
    private static final HashMap<Object, FailureMessage> MAP = new HashMap<>();
    
    static {
        final FailureMessage INVISIBLE_CHARACTER = new FailureMessage(
                "file name contains forbidden invisible character {char} at index {index}",
                "文件名在下标 {index} 处包含禁止使用的不可见字符 {char}"
        );
        for (int ch = 0; ch < 32; ch++) {
            MAP.put((char) ch, INVISIBLE_CHARACTER);
        }
        final FailureMessage RESERVED_CHARACTER = new FailureMessage(
                "file name contains forbidden reserved character {char} at index {index}",
                "文件名在下标 {index} 处包含禁止使用的保留字符 {char}"
        );
        MAP.put('<', RESERVED_CHARACTER);
        MAP.put('>', RESERVED_CHARACTER);
        MAP.put(':', RESERVED_CHARACTER);
        MAP.put('"', RESERVED_CHARACTER);
        MAP.put('/', RESERVED_CHARACTER);
        MAP.put('\\', RESERVED_CHARACTER);
        MAP.put('|', RESERVED_CHARACTER);
        MAP.put('?', RESERVED_CHARACTER);
        MAP.put('*', RESERVED_CHARACTER);
        MAP.put("", new FailureMessage(
                "File name is an empty string",
                "文件名是空字符串"
        ));
        final FailureMessage RESERVED_NAME = new FailureMessage(
                "reserved names can not be used as file name",
                "保留名称不能用作文件名"
        );
        MAP.put(".", RESERVED_NAME);
        MAP.put("..", RESERVED_NAME);
        MAP.put("CON", RESERVED_NAME);
        MAP.put("PRN", RESERVED_NAME);
        MAP.put("AUX", RESERVED_NAME);
        MAP.put("NUL", RESERVED_NAME);
        MAP.put("COM0", RESERVED_NAME);
        MAP.put("COM1", RESERVED_NAME);
        MAP.put("COM2", RESERVED_NAME);
        MAP.put("COM3", RESERVED_NAME);
        MAP.put("COM4", RESERVED_NAME);
        MAP.put("COM5", RESERVED_NAME);
        MAP.put("COM6", RESERVED_NAME);
        MAP.put("COM7", RESERVED_NAME);
        MAP.put("COM8", RESERVED_NAME);
        MAP.put("COM9", RESERVED_NAME);
        MAP.put("COM\u00b9", RESERVED_NAME); // superscript 1
        MAP.put("COM\u00b2", RESERVED_NAME); // superscript 2
        MAP.put("COM\u00b3", RESERVED_NAME); // superscript 3
        MAP.put("LPT0", RESERVED_NAME);
        MAP.put("LPT1", RESERVED_NAME);
        MAP.put("LPT2", RESERVED_NAME);
        MAP.put("LPT3", RESERVED_NAME);
        MAP.put("LPT4", RESERVED_NAME);
        MAP.put("LPT5", RESERVED_NAME);
        MAP.put("LPT6", RESERVED_NAME);
        MAP.put("LPT7", RESERVED_NAME);
        MAP.put("LPT8", RESERVED_NAME);
        MAP.put("LPT9", RESERVED_NAME);
        MAP.put("LPT\u00b9", RESERVED_NAME); // superscript 1
        MAP.put("LPT\u00b2", RESERVED_NAME); // superscript 2
        MAP.put("LPT\u00b3", RESERVED_NAME); // superscript 3
    }

    @NotNull
    @Override
    LocalizedFailureMessage get(@Nullable Locale locale, @Nullable String string) {
        if (string == null) {
            return localize(locale, NULL);
        }
        final int length = string.length();
        if (length > 260) {
            return localize(locale, LONG).replace("{length}", length);
        }
        {
            char last = string.charAt(length - 1);
            if (last == ' ' || last == '.') {
                return localize(locale, END);
            }
        }
        FailureMessage message = MAP.get(string.toUpperCase());
        if (message != null) {
            return localize(locale, message);
        }
        for (int index = 0; index < length; index++) {
            char ch = string.charAt(index);
            message = MAP.get(ch);
            if (message != null) {
                return localize(locale, message).replace("{index}", index).replaceQuoted("{char}", ch);
            }
        }
        return BooleanFailureMessage.SUCCESS;
    }
}
