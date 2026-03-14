package mujica.text.format;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import mujica.io.stream.InputStreamUtil;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.PrintStream;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@CodeHistory(date = "2026/1/7")
public class CharSequenceAppender {

    public boolean isApplicable(@NotNull CharSequence string) {
        return true;
    }

    public boolean isApplicable(@NotNull CharSequence string,
                                @Index(of = "string") int startIndex,
                                @Index(of = "string", inclusive = false) int endIndex) {
        return isApplicable(string.subSequence(startIndex, endIndex));
    }

    public boolean isApplicable(char ch) {
        return isApplicable(new UnitCharSequence(ch));
    }

    public boolean isIdentity(@NotNull CharSequence string) {
        return true;
    }

    public boolean isIdentity(@NotNull CharSequence string,
                                @Index(of = "string") int startIndex,
                                @Index(of = "string", inclusive = false) int endIndex) {
        return isIdentity(string.subSequence(startIndex, endIndex));
    }

    public boolean isIdentity(char ch) {
        return isIdentity(new UnitCharSequence(ch));
    }

    public int deltaCharCount(@NotNull CharSequence string) {
        return 0;
    }

    public int deltaCharCount(@NotNull CharSequence string,
                                @Index(of = "string") int startIndex,
                                @Index(of = "string", inclusive = false) int endIndex) {
        return deltaCharCount(string.subSequence(startIndex, endIndex));
    }

    public int deltaCharCount(char ch) {
        return deltaCharCount(new UnitCharSequence(ch));
    }

    public int charEditDistance(@NotNull CharSequence string) {
        return 0;
    }

    public int charEditDistance(@NotNull CharSequence string,
                                @Index(of = "string") int startIndex,
                                @Index(of = "string", inclusive = false) int endIndex) {
        return deltaCharCount(string.subSequence(startIndex, endIndex));
    }

    public int charEditDistance(char ch) {
        return charEditDistance(new UnitCharSequence(ch));
    }

    public void write(@NotNull CharSequence string, @NotNull ByteBuffer out) {
        out.put(string.toString().getBytes(StandardCharsets.UTF_8));
    }

    public void write(@NotNull CharSequence string,
                      @Index(of = "string") int startIndex,
                      @Index(of = "string", inclusive = false) int endIndex,
                      @NotNull ByteBuffer out) {
        write(string.subSequence(startIndex, endIndex), out);
    }

    public void write(char ch, @NotNull ByteBuffer out) {
        write(new UnitCharSequence(ch), out);
    }

    @NotNull
    public byte[] serialize(@NotNull CharSequence string, @Nullable ByteBuffer buf) {
        if (buf == null) {
            buf = ByteBuffer.allocate(string.length() + 0x40); // 64 bytes
        } else {
            buf.clear();
        }
        while (true) {
            try {
                write(string, buf);
                break;
            } catch (BufferOverflowException e) {
                int capacity = buf.capacity();
                int newCapacity = Math.min(capacity << 1, InputStreamUtil.MAX_BUFFER_SIZE);
                if (capacity < newCapacity) {
                    buf = ByteBuffer.allocate(newCapacity);
                } else {
                    throw e;
                }
            }
        }
        final byte[] data = new byte[buf.flip().limit()];
        buf.get(data);
        return data;
    }

    public void write(@NotNull CharSequence string, @NotNull ByteBuf out) {
        out.writeCharSequence(string, StandardCharsets.UTF_8);
    }

    public void write(@NotNull CharSequence string,
                      @Index(of = "string") int startIndex,
                      @Index(of = "string", inclusive = false) int endIndex,
                      @NotNull ByteBuf out) {
        write(string.subSequence(startIndex, endIndex), out);
    }

    public void write(char ch, @NotNull ByteBuf out) {
        write(new UnitCharSequence(ch), out);
    }

    @NotNull
    public byte[] serialize(@NotNull CharSequence string, @Nullable ByteBuf buf) {
        if (buf == null) {
            buf = Unpooled.buffer();
        } else {
            buf.clear().retain();
        }
        try {
            write(string, buf);
            byte[] data = new byte[buf.readableBytes()];
            buf.readBytes(data);
            return data;
        } finally {
            buf.release();
        }
    }

    public void append(@NotNull CharSequence string, @NotNull StringBuilder out) {
        out.append(string);
    }

    public void append(@NotNull CharSequence string,
                       @Index(of = "string") int startIndex,
                       @Index(of = "string", inclusive = false) int endIndex,
                       @NotNull StringBuilder out) {
        append(string.subSequence(startIndex, endIndex), out);
    }

    public void append(char ch, @NotNull StringBuilder out) {
        append(new UnitCharSequence(ch), out);
    }

    @NotNull
    public String stringify(@NotNull CharSequence string, @Nullable StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        } else {
            sb.delete(0, sb.length());
        }
        append(string, sb);
        return sb.toString();
    }

    @NotNull
    public String stringify(@NotNull CharSequence string,
                            @Index(of = "string") int startIndex,
                            @Index(of = "string", inclusive = false) int endIndex,
                            @Nullable StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        } else {
            sb.delete(0, sb.length());
        }
        append(string, startIndex, endIndex, sb);
        return sb.toString();
    }

    @NotNull
    public String stringify(char ch, @Nullable StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        } else {
            sb.delete(0, sb.length());
        }
        append(ch, sb);
        return sb.toString();
    }

    public void append(@NotNull CharSequence string, @NotNull StringBuffer out) {
        out.append(string);
    }

    public void append(@NotNull CharSequence string,
                       @Index(of = "string") int startIndex,
                       @Index(of = "string", inclusive = false) int endIndex,
                       @NotNull StringBuffer out) {
        append(string.subSequence(startIndex, endIndex), out);
    }

    public void append(char ch, @NotNull StringBuffer out) {
        append(new UnitCharSequence(ch), out);
    }

    @NotNull
    public String stringify(@NotNull CharSequence string, @Nullable StringBuffer sb) {
        if (sb == null) {
            sb = new StringBuffer();
        } else {
            sb.delete(0, sb.length());
        }
        append(string, sb);
        return sb.toString();
    }

    @NotNull
    public String stringify(@NotNull CharSequence string,
                            @Index(of = "string") int startIndex,
                            @Index(of = "string", inclusive = false) int endIndex,
                            @Nullable StringBuffer sb) {
        if (sb == null) {
            sb = new StringBuffer();
        } else {
            sb.delete(0, sb.length());
        }
        append(string, startIndex, endIndex, sb);
        return sb.toString();
    }

    @NotNull
    public String stringify(char ch, @Nullable StringBuffer sb) {
        if (sb == null) {
            sb = new StringBuffer();
        } else {
            sb.delete(0, sb.length());
        }
        append(ch, sb);
        return sb.toString();
    }

    public void print(@NotNull CharSequence string, @NotNull PrintStream out) {
        out.print(string); // append is also OK
    }

    public void print(@NotNull CharSequence string,
                       @Index(of = "string") int startIndex,
                       @Index(of = "string", inclusive = false) int endIndex,
                       @NotNull PrintStream out) {
        print(string.subSequence(startIndex, endIndex), out);
    }

    public void print(char ch, @NotNull PrintStream out) {
        print(new UnitCharSequence(ch), out);
    }

    /**
     * @param out accepted types:
     *            String
     *            char[]
     *            Boolean
     *            Integer for CodePoint
     *            Character
     *            Long
     */
    public void addTokens(@NotNull CharSequence string, @NotNull List<Object> out) {
        out.add(string.toString());
    }

    public void addTokens(@NotNull CharSequence string,
                       @Index(of = "string") int startIndex,
                       @Index(of = "string", inclusive = false) int endIndex,
                       @NotNull List<Object> out) {
        addTokens(string.subSequence(startIndex, endIndex), out);
    }

    public void addTokens(char ch, @NotNull List<Object> out) {
        addTokens(new UnitCharSequence(ch), out);
    }

    @Nullable
    public Object mergeTokens(@NotNull CharSequence string, @Nullable List<Object> list) {
        if (list == null) {
            list = new ArrayList<>();
        } else {
            list.clear();
        }
        addTokens(string, list);
        final int size = list.size();
        if (size == 0) {
            return null;
        }
        int previousStep = 1;
        int currentStep = 2;
        while (previousStep < size) {
            for (int index = previousStep; index < size; index += currentStep) {
                int parent = index - previousStep;
                list.set(parent, concatToken(list.get(parent), list.get(index)));
            }
            previousStep = currentStep;
            currentStep <<= 1;
        }
        return list.get(0);
    }

    public Object concatToken(Object left, Object right) {
        return left + "" + right;
    }

    public static final List<Class<? extends CharSequence>> CHAR_SEQUENCE_CLASSES = List.of(
            String.class,
            StringBuilder.class,
            StringBuffer.class,
            CharBuffer.class
    );

    @CodeHistory(date = "2026/3/8")
    public static final class Json {

        public static final CharSequenceAppender INSTANCE = new QuoteAppender(new SwitchEscapeAppender(
                SwitchEscapeAppender.FLAG_BS | SwitchEscapeAppender.FLAG_HT | SwitchEscapeAppender.FLAG_LF | SwitchEscapeAppender.FLAG_FF
                        | SwitchEscapeAppender.FLAG_CR | SwitchEscapeAppender.FLAG_QUOT | SwitchEscapeAppender.FLAG_SL
                        | SwitchEscapeAppender.FLAG_BSL
        ), "\"");

        private Json() {
            super();
        }
    }

    @CodeHistory(date = "2026/3/11")
    @ReferencePage(title = "String literals", href = "https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Lexical_grammar#string_literals")
    public static final class JavaScript {

        public static final CharSequenceAppender APOSTROPHE = new QuoteAppender(
                (new IntMapEscapeAppender()).escapeControlX().escapeNTR().escapeBackSlash().escapeApostrophe(),
                "'");

        public static final CharSequenceAppender QUOTATION_MARK = new QuoteAppender(
                (new IntMapEscapeAppender()).escapeControlX().escapeNTR().escapeBackSlash().escapeQuotationMark(),
                "\"");

        @ReferencePage(title = "Template literals", href = "https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Template_literals")
        public static final CharSequenceAppender GRAVE_ACCENT = new QuoteAppender(
                (new IntMapEscapeAppender()).escapeControlX().escapeGraveAccent(),
                "`");

        public static final CharSequenceAppender AUTO = new SelectiveAppender.LeastCharCount(new CharSequenceAppender[] {
                APOSTROPHE, QUOTATION_MARK, GRAVE_ACCENT
        });

        public static final CharSequenceAppender REGEX = new QuoteAppender(
                (new IntMapEscapeAppender()).escapeRegex(),
                "/");

    }

    @CodeHistory(date = "2026/3/10")
    @ReferencePage(title = "String and Bytes literals", href = "https://docs.python.org/3/reference/lexical_analysis.html#string-and-bytes-literals")
    public static final class Python {

        public static final CharSequenceAppender QUOTATION_MARK = new QuoteAppender(
                (new IntMapEscapeAppender()).escapeControlX().escapeNTR().escapeBell().escapeBackspace().escapeVerticalTabulation().escapeFormFeed()
                .escapeBackSlash().escapeQuotationMark(),
                "\"");

        public static final CharSequenceAppender APOSTROPHE = new QuoteAppender(
                (new IntMapEscapeAppender()).escapeControlX().escapeNTR().escapeBell().escapeBackspace().escapeVerticalTabulation().escapeFormFeed()
                .escapeBackSlash().escapeApostrophe(),
                "'");

        public static final CharSequenceAppender TRIPLE_QUOTATION_MARK = (new QuoteAppender(
                (new IntMapEscapeAppender()).escapeControlX().escapeNTR().escapeBackSlash(),
                "\"\"\"")).setCheckEnabled(true);

        public static final CharSequenceAppender TRIPLE_APOSTROPHE = (new QuoteAppender(
                (new IntMapEscapeAppender()).escapeControlX().escapeNTR().escapeBackSlash(),
                "'''")).setCheckEnabled(true);

        public static final CharSequenceAppender AUTO = new SelectiveAppender.LeastCharCount(new CharSequenceAppender[] {
                QUOTATION_MARK, APOSTROPHE, TRIPLE_QUOTATION_MARK, TRIPLE_APOSTROPHE
        });

        private Python() {
            super();
        }
    }
}
