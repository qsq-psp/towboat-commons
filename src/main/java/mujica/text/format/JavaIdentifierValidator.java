package mujica.text.format;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Locale;

@CodeHistory(date = "2025/4/16")
@ReferencePage(title = "JLS12 Chapter 3. Lexical Structure", author = "James Gosling", language = "en", href = "https://docs.oracle.com/javase/specs/jls/se12/html/jls-3.html#jls-3.8")
public class JavaIdentifierValidator extends AbstractFormatValidator.StringForm {

    public static final JavaIdentifierValidator INSTANCE = new JavaIdentifierValidator();

    private static final HashMap<String, FailureMessage> MAP = new HashMap<>();

    static {
        MAP.put(null, new FailureMessage(
                "identifier is null",
                "标识符是空值"
        ));
        MAP.put("", new FailureMessage(
                "identifier is an empty string",
                "标识符是空字符串"
        ));
        final FailureMessage KEYWORD = new FailureMessage(
                "a keyword cannot be used as an identifier",
                "关键词不能用作标识符"
        );
        MAP.put("abstract", KEYWORD);
        MAP.put("assert", KEYWORD);
        MAP.put("boolean", KEYWORD);
        MAP.put("break", KEYWORD);
        MAP.put("byte", KEYWORD);
        MAP.put("case", KEYWORD);
        MAP.put("catch", KEYWORD);
        MAP.put("char", KEYWORD);
        MAP.put("class", KEYWORD);
        MAP.put("const", KEYWORD);
        MAP.put("continue", KEYWORD);
        MAP.put("default", KEYWORD);
        MAP.put("do", KEYWORD);
        MAP.put("double", KEYWORD);
        MAP.put("else", KEYWORD);
        MAP.put("enum", KEYWORD);
        MAP.put("extends", KEYWORD);
        MAP.put("final", KEYWORD);
        MAP.put("finally", KEYWORD);
        MAP.put("float", KEYWORD);
        MAP.put("for", KEYWORD);
        MAP.put("if", KEYWORD);
        MAP.put("goto", KEYWORD);
        MAP.put("implements", KEYWORD);
        MAP.put("import", KEYWORD);
        MAP.put("instanceof", KEYWORD);
        MAP.put("int", KEYWORD);
        MAP.put("interface", KEYWORD);
        MAP.put("long", KEYWORD);
        MAP.put("native", KEYWORD);
        MAP.put("new", KEYWORD);
        MAP.put("package", KEYWORD);
        MAP.put("private", KEYWORD);
        MAP.put("protected", KEYWORD);
        MAP.put("public", KEYWORD);
        MAP.put("return", KEYWORD);
        MAP.put("short", KEYWORD);
        MAP.put("static", KEYWORD);
        MAP.put("strictfp", KEYWORD);
        MAP.put("super", KEYWORD);
        MAP.put("switch", KEYWORD);
        MAP.put("synchronized", KEYWORD);
        MAP.put("this", KEYWORD);
        MAP.put("throw", KEYWORD);
        MAP.put("throws", KEYWORD);
        MAP.put("transient", KEYWORD);
        MAP.put("try", KEYWORD);
        MAP.put("void", KEYWORD);
        MAP.put("volatile", KEYWORD);
        MAP.put("while", KEYWORD);
        final FailureMessage BOOLEAN = new FailureMessage(
                "a boolean literal cannot be used as an identifier",
                "布尔值字面量不能用作标识符"
        );
        MAP.put("false", BOOLEAN);
        MAP.put("true", BOOLEAN);
        MAP.put("null", new FailureMessage(
                "the null literal cannot be used as an identifier",
                "空值字面量不能用作标识符"
        ));
    }
    private static final FailureMessage START = new FailureMessage(
            "identifier starts with illegal character",
            "标识符开头是非法字符"
    );

    /**
     * The name 'part' is from Character.isJavaIdentifierPart()
     */
    private static final FailureMessage PART = new FailureMessage(
            "identifier continues with illegal character",
            "标识符后续包含非法字符"
    );

    @NotNull
    @Override
    LocalizedFailureMessage get(@Nullable Locale locale, @Nullable String string) {
        FailureMessage message = MAP.get(string);
        if (message == null) { // now string is not null and not empty
            assert string != null;
            assert !string.isEmpty();
            if (!Character.isJavaIdentifierStart(string.charAt(0))) {
                message = START;
            } else {
                int length = string.length();
                for (int index = 1; index < length; index++) {
                    if (!Character.isJavaIdentifierPart(string.charAt(index))) {
                        // System.out.println("JavaIdentifierValidator.get " + string);
                        message = PART;
                        break;
                    }
                }
            }
        }
        return localize(locale, message);
    }
}
