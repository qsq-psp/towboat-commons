package mujica.text.escape;

import mujica.reflect.function.StringUnaryOperator;
import mujica.reflect.modifier.CodeHistory;
import mujica.text.number.HexEncoder;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.function.Consumer;

@CodeHistory(date = "2021/9/1", project = "bloom")
@CodeHistory(date = "2021/9/23", project = "va")
@CodeHistory(date = "2022/3/23", project = "infrastructure")
@CodeHistory(date = "2022/5/25", project = "Ultramarine")
@CodeHistory(date = "2025/3/5")
public class Quote extends HexEncoder implements StringUnaryOperator, QuoteBoundary, EscapeValueStyle {

    public static final Quote ONE = new Quote(HEX_VALUE | UNICODE_PREFIX, APOSTROPHE);

    public static final Quote DEFAULT = new Quote(HEX_VALUE | UNICODE_PREFIX, QUOTATION); // usually used in log and debug

    public static final Quote AUTO = new Quote(HEX_VALUE | UNICODE_PREFIX, APOSTROPHE | QUOTATION); // usually used in toString or stringify method

    public static final Quote JSON = new Quote(UNICODE_PREFIX, QUOTATION);

    private final int valueStyle, boundary;

    Quote(@MagicConstant(flagsFromClass = EscapeValueStyle.class) int valueStyle, int boundary) {
        super((valueStyle & UPPER_CASE) != 0 ? UPPER : LOWER);
        this.valueStyle = valueStyle;
        this.boundary = boundary;
    }

    public Quote(boolean useSingle, boolean useDouble, boolean useApostrophe, @MagicConstant(flagsFromClass = EscapeValueStyle.class) int valueStyle) {
        this(valueStyle, boundaryOf(useSingle, useDouble, useApostrophe));
    }
    
    private static int boundaryOf(boolean useSingle, boolean useDouble, boolean useApostrophe) {
        int boundary = 0;
        if (useSingle) {
            boundary |= APOSTROPHE;
        }
        if (useDouble) {
            boundary |= QUOTATION;
        }
        if (useApostrophe) {
            boundary |= GRAVE;
        }
        if (boundary == 0) {
            boundary = QUOTATION;
        }
        return boundary;
    }

    @NotNull
    public String apply(byte b) {
        final StringBuilder sb = new StringBuilder();
        append(b, sb);
        return sb.toString();
    }

    public void append(byte b, @NotNull StringBuilder out) {
        out.append('b'); // data type mark
        appendCodePoint(0xff & b, out);
    }

    public String apply(@NotNull byte[] bytes) {
        final StringBuilder sb = new StringBuilder();
        append(bytes, sb);
        return sb.toString();
    }

    public void append(@NotNull byte[] bytes, @NotNull StringBuilder out) {
        final int len = bytes.length;
        if (len == 0) {
            out.append("b[]");
            return;
        }
        out.append("b[");
        append(bytes[0], out); // no, there is b mark inside
        for (int i = 1; i < len; i++) {
            out.append(", ");
            append(bytes[i], out); // no, there is b mark inside
        }
        out.append("]");
    }

    @NotNull
    public String apply(char ch) {
        final StringBuilder sb = new StringBuilder();
        append(ch, sb);
        return sb.toString();
    }

    public void append(char ch, @NotNull StringBuilder out) {
        out.append('c'); // data type mark
        appendCodePoint((int) ch, out);
    }

    public String apply(@NotNull char[] chars) {
        final StringBuilder sb = new StringBuilder();
        append(chars, sb);
        return sb.toString();
    }

    public void append(@NotNull char[] chars, @NotNull StringBuilder out) {
        final int len = chars.length;
        if (len == 0) {
            out.append("b[]");
            return;
        }
        out.append("c[");
        append(chars[0], out); // no, there is c mark inside
        for (int i = 1; i < len; i++) {
            out.append(", ");
            append(chars[i], out); // no, there is c mark inside
        }
        out.append("]");
    }

    @NotNull
    public String apply(int cp) {
        // code point has no mark
        final StringBuilder sb = new StringBuilder();
        appendCodePoint(cp, sb);
        return sb.toString();
    }

    private void appendCodePoint(int cp, @NotNull StringBuilder out) {
        assert 0 <= cp;
        assert cp <= Character.MAX_CODE_POINT;
        if ((boundary & APOSTROPHE) != 0 && cp != '\'') {
            appendEscaped1(out, cp, '\'');
        } else if ((boundary & QUOTATION) != 0 && cp != '"') {
            appendEscaped1(out, cp, '"');
        } else if ((boundary & GRAVE) != 0 && cp != '`') {
            appendEscaped1(out, cp, '`');
        } else if ((boundary & TRIPLE_APOSTROPHE) != 0) {
            appendEscaped3(out, cp, '\'');
        } else if ((boundary & TRIPLE_QUOTATION) != 0) {
            appendEscaped3(out, cp, '"');
        } else if ((boundary & TRIPLE_GRAVE) != 0) {
            appendEscaped3(out, cp, '`');
        } else if ((boundary & APOSTROPHE) != 0) {
            appendEscaped1(out, cp, '\'');
        } else if ((boundary & QUOTATION) != 0) {
            appendEscaped1(out, cp, '"');
        } else if ((boundary & GRAVE) != 0) {
            appendEscaped1(out, cp, '`');
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    @NotNull
    public String apply(@NotNull String string) {
        final StringBuilder sb = new StringBuilder(string.length() + 16);
        append(string, sb);
        return sb.toString();
    }

    public String apply(@Nullable String string, String fallback) {
        if (string == null) {
            return fallback;
        }
        return apply(string);
    }

    public void append(@NotNull StringBuilder out, @Nullable String in, String fallback) {
        if (in == null) {
            out.append(fallback);
        } else {
            append(in, out);
        }
    }

    @NotNull
    public String surround(@Nullable String prefix, String string, @Nullable String suffix) {
        final StringBuilder sb = new StringBuilder();
        if (prefix != null) {
            sb.append(prefix);
        }
        append(string, sb);
        if (suffix != null) {
            sb.append(suffix);
        }
        return sb.toString();
    }

    @NotNull
    public String surround(@Nullable String prefix, @Nullable String string, @Nullable String suffix, String fallback) {
        final StringBuilder sb = new StringBuilder();
        if (prefix != null) {
            sb.append(prefix);
        }
        if (string != null) {
            append(string, sb);
        } else {
            append(fallback, sb);
        }
        if (suffix != null) {
            sb.append(suffix);
        }
        return sb.toString();
    }

    @Override
    public void append(@NotNull String in, @NotNull StringBuilder out) {
        switch (boundary) {
            case APOSTROPHE:
                appendEscaped1(out, in, '\'');
                break;
            case QUOTATION:
                appendEscaped1(out, in, '"');
                break;
            case GRAVE:
                appendEscaped1(out, in, '`');
                break;
            case TRIPLE_APOSTROPHE:
                if (in.contains("'''")) {
                    throw new IllegalArgumentException();
                }
                appendEscaped3(out, in, '\'');
                break;
            case TRIPLE_QUOTATION:
                if (in.contains("\"\"\"")) {
                    throw new IllegalArgumentException();
                }
                appendEscaped3(out, in, '"');
                break;
            case TRIPLE_GRAVE:
                if (in.contains("```")) {
                    throw new IllegalArgumentException();
                }
                appendEscaped3(out, in, '`');
                break;
            default:
                appendOtherCases(out, in);
                break;
        }
    }

    public void appendOtherCases(@NotNull StringBuilder out, @NotNull String in) {
        if ((boundary & (TRIPLE_APOSTROPHE | TRIPLE_QUOTATION | TRIPLE_GRAVE)) != 0) {
            if ((boundary & TRIPLE_APOSTROPHE) != 0 && !in.contains("'''")) {
                appendEscaped3(out, in, '\'');
                return;
            }
            if ((boundary & TRIPLE_QUOTATION) != 0 && !in.contains("\"\"\"")) {
                appendEscaped3(out, in, '\'');
                return;
            }
            if ((boundary & TRIPLE_GRAVE) != 0 && !in.contains("```")) {
                appendEscaped3(out, in, '\'');
                return;
            }
        }
        Scanner scanner;
        switch (boundary & (APOSTROPHE | QUOTATION | GRAVE)) {
            case APOSTROPHE:
                appendEscaped1(out, in, '\'');
                break;
            case QUOTATION:
                appendEscaped1(out, in, '"');
                break;
            case GRAVE:
                appendEscaped1(out, in, '`');
                break;
            case APOSTROPHE | QUOTATION:
                scanner = new Scanner();
                scanner.accept(in);
                if (scanner.countSingle < scanner.countDouble) {
                    appendEscaped1(out, in, '\'');
                } else {
                    appendEscaped1(out, in, '"');
                }
                break;
            case QUOTATION | GRAVE:
                scanner = new Scanner();
                scanner.accept(in);
                if (scanner.countApostrophe < scanner.countDouble) {
                    appendEscaped1(out, in, '`');
                } else {
                    appendEscaped1(out, in, '"');
                }
                break;
            case APOSTROPHE | GRAVE:
                scanner = new Scanner();
                scanner.accept(in);
                if (scanner.countApostrophe < scanner.countSingle) {
                    appendEscaped1(out, in, '`');
                } else {
                    appendEscaped1(out, in, '\'');
                }
                break;
            case APOSTROPHE | QUOTATION | GRAVE:
                scanner = new Scanner();
                scanner.accept(in);
                if (scanner.countSingle < scanner.countDouble) {
                    if (scanner.countApostrophe < scanner.countSingle) {
                        appendEscaped1(out, in, '`');
                    } else {
                        appendEscaped1(out, in, '\'');
                    }
                } else {
                    if (scanner.countApostrophe < scanner.countDouble) {
                        appendEscaped1(out, in, '`');
                    } else {
                        appendEscaped1(out, in, '"');
                    }
                }
                break;
            default:
                throw new IllegalStateException();
        }
    }

    private void appendEscaped1(@NotNull StringBuilder out, int in, int quotationMark) {
        boolean undefined = false;
        out.append((char) quotationMark);
        switch (in) {
            case '\b':
                out.append("\\b");
                break;
            case '\t':
                out.append("\\t");
                break;
            case '\n':
                out.append("\\n");
                break;
            case '\f':
                out.append("\\f");
                break;
            case '\r':
                out.append("\\r");
                break;
            case '\\':
                out.append("\\\\");
                break;
            case '\'':
            case '"':
            case '`':
                if (in == quotationMark) {
                    out.append('\\').append((char) in);
                } else {
                    out.append((char) in);
                }
                break;
            default:
                if (in < 0x100) {
                    if (0x20 <= in && in < 0x7f) {
                        out.append((char) in);
                    } else {
                        appendByteEscape(out, in);
                    }
                } else if (Character.isDefined(in)) {
                    appendUnicode(out, in);
                } else {
                    undefined = true;
                }
                break;
        }
        out.append((char) quotationMark);
        if (undefined) {
            out.append("(ud)"); // undefined character
        }
    }

    private void appendEscaped1(@NotNull StringBuilder out, String in, int quotationMark) {
        out.append((char) quotationMark);
        final int length = in.length();
        for (int index = 0; index < length; index++) {
            int ch = in.charAt(index);
            switch (ch) {
                case '\b':
                    out.append("\\b");
                    break;
                case '\t':
                    out.append("\\t");
                    break;
                case '\n':
                    out.append("\\n");
                    break;
                case '\f':
                    out.append("\\f");
                    break;
                case '\r':
                    out.append("\\r");
                    break;
                case '\\':
                    out.append("\\\\");
                    break;
                case '\'':
                case '"':
                case '`':
                    if (ch == quotationMark) {
                        out.append('\\').append((char) ch);
                    } else {
                        out.append((char) ch);
                    }
                    break;
                default:
                    if (ch < 0x100) {
                        if (0x20 <= ch && ch < 0x7f) {
                            out.append((char) ch);
                        } else {
                            appendByteEscape(out, ch);
                        }
                    } else {
                        appendUnicode(out, ch);
                    }
                    break;
            }
        }
        out.append((char) quotationMark);
    }

    private void appendEscaped3(@NotNull StringBuilder out, int in, char quotationMark) {
        boolean undefined = false;
        out.append(quotationMark);
        out.append(quotationMark);
        out.append(quotationMark);
        switch (in) {
            case '\b':
                out.append("\\b");
                break;
            case '\t':
                out.append("\\t");
                break;
            case '\n':
                out.append("\\n");
                break;
            case '\f':
                out.append("\\f");
                break;
            case '\r':
                out.append("\\r");
                break;
            case '\\':
                out.append("\\\\");
                break;
            default:
                if (in < 0x100) {
                    if (0x20 <= in && in < 0x7f) {
                        out.append((char) in);
                    } else {
                        appendByteEscape(out, in);
                    }
                } else if (Character.isDefined(in)) {
                    appendUnicode(out, in);
                } else {
                    undefined = true;
                }
                break;
        }
        out.append(quotationMark);
        out.append(quotationMark);
        out.append(quotationMark);
        if (undefined) {
            out.append("(ud)"); // undefined character
        }
    }

    private void appendEscaped3(@NotNull StringBuilder out, @NotNull String in, char quotationMark) {
        out.append(quotationMark);
        out.append(quotationMark);
        out.append(quotationMark);
        final int length = in.length();
        for (int index = 0; index < length; index++) {
            int ch = in.charAt(index);
            switch (ch) {
                case '\b':
                    out.append("\\b");
                    break;
                case '\t':
                    out.append("\\t");
                    break;
                case '\n':
                    out.append("\\n");
                    break;
                case '\f':
                    out.append("\\f");
                    break;
                case '\r':
                    out.append("\\r");
                    break;
                case '\\':
                    out.append("\\\\");
                    break;
                default:
                    if (ch < 0x100) {
                        if (0x20 <= ch && ch < 0x7f) {
                            out.append((char) ch);
                        } else {
                            appendByteEscape(out, ch);
                        }
                    } else {
                        appendUnicode(out, ch);
                    }
                    break;
            }
        }
        out.append(quotationMark);
        out.append(quotationMark);
        out.append(quotationMark);
    }

    private void appendUnicode(@NotNull StringBuilder out, int ch) {
        if (0x3000 <= ch && (ch < 0x4DC0 || 0x4E00 <= ch && ch < 0x9FF0 || 0xFF00 <= ch)) {
            out.append((char) ch);
        } else {
            appendUnicodeEscape(out, ch);
        }
    }

    private void appendByteEscape(@NotNull StringBuilder out, int ch) {
        if ((valueStyle & HEX_VALUE) != 0) {
            out.append("\\x");
        } else {
            out.append("\\u00");
        }
        hex4(out, ch >> 4);
        hex4(out, ch);
    }

    private void appendUnicodeEscape(@NotNull StringBuilder out, int ch) {
        if ((valueStyle & UNICODE_PREFIX) != 0) {
            out.append("\\u");
            hex4(out, ch >> 12);
            hex4(out, ch >> 8);
            hex4(out, ch >> 4);
            hex4(out, ch);
        } else if ((valueStyle & CURLY_BRACKET) != 0) {
            out.append("\\u{");
            hexMax24(out, ch);
            out.append('}');
        } else {
            throw new IllegalArgumentException();
        }
    }

    @CodeHistory(date = "2022/5/25", project = "Ultramarine", name = "QuoteScanner")
    @CodeHistory(date = "2023/4/4", project = "Ultramarine")
    private static class Scanner implements Consumer<CharSequence>, Serializable {

        private static final long serialVersionUID = 0x05ea958418577dbbL;

        int countSingle;

        int countDouble;

        int countApostrophe;

        public Scanner() {
            super();
        }

        @Override
        public void accept(@NotNull CharSequence string) {
            final int length = string.length();
            for (int index = 0; index < length; index++) {
                switch (string.charAt(index)) {
                    case '\'':
                        countSingle++;
                        break;
                    case '"':
                        countDouble++;
                        break;
                    case '`':
                        countApostrophe++;
                        break;
                }
            }
        }
    }

    public void append(@NotNull Object[] array, @NotNull StringBuilder out) {
        out.append("o[");
        boolean subsequent = false;
        for (Object item : array) {
            if (subsequent) {
                out.append(", ");
            }
            if (item == null) {
                out.append("-");
            } else {
                append(item, out);
            }
            subsequent = true;
        }
        out.append("]");
    }

    public void append(@NotNull Object object, @NotNull StringBuilder out) {
        try {
            MethodHandle methodHandle = OfObject.MAP.get(object.getClass());
            if (methodHandle != null) {
                methodHandle.invoke(this, object, out);
                return;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        out.append(object);
    }

    @CodeHistory(date = "2025/6/3")
    private static class OfObject {

        static final HashMap<Class<?>, MethodHandle> MAP = new HashMap<>();

        static {
            try {
                MethodHandle methodHandle;
                methodHandle = MethodHandles.lookup().findVirtual(Quote.class, "append", MethodType.methodType(void.class, byte.class, StringBuilder.class));
                MAP.put(Byte.TYPE, methodHandle);
                MAP.put(Byte.class, methodHandle);
                methodHandle = MethodHandles.lookup().findVirtual(Quote.class, "append", MethodType.methodType(void.class, byte[].class, StringBuilder.class));
                MAP.put(byte[].class, methodHandle);
                methodHandle = MethodHandles.lookup().findVirtual(Quote.class, "append", MethodType.methodType(void.class, char.class, StringBuilder.class));
                MAP.put(Character.TYPE, methodHandle);
                MAP.put(Character.class, methodHandle);
                methodHandle = MethodHandles.lookup().findVirtual(Quote.class, "append", MethodType.methodType(void.class, char[].class, StringBuilder.class));
                MAP.put(char[].class, methodHandle);
                methodHandle = MethodHandles.lookup().findVirtual(Quote.class, "append", MethodType.methodType(void.class, String.class, StringBuilder.class));
                MAP.put(String.class, methodHandle);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }
    }
}
