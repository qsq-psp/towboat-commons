package mujica.text.format;

import io.netty.util.concurrent.FastThreadLocal;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/1/31.
 */
public class DebugFormat {

    @NotNull
    final CharSequenceAppender appender;

    @NotNull
    final CharSequenceOperator operator;

    @NotNull
    final UniversalAppender universal;

    @NotNull
    final UniversalStringifier stringifier;

    public DebugFormat(
            @NotNull CharSequenceAppender appender, @NotNull CharSequenceOperator operator,
            @NotNull UniversalAppender universal, @NotNull UniversalStringifier stringifier) {
        super();
        this.appender = appender;
        this.operator = operator;
        this.universal = universal;
        this.stringifier = stringifier;
    }

    @NotNull
    public CharSequenceAppender getAppender() {
        return appender;
    }

    @NotNull
    public CharSequenceOperator getOperator() {
        return operator;
    }

    @NotNull
    public UniversalAppender getUniversal() {
        return universal;
    }

    @NotNull
    public UniversalStringifier getStringifier() {
        return stringifier;
    }

    private static final FastThreadLocal<DebugFormat> AUTO_QUOTE = new FastThreadLocal<>();

    @NotNull
    public static DebugFormat autoQuote() {
        return AUTO_QUOTE.get();
    }
}
