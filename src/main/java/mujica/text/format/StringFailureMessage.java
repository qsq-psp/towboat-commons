package mujica.text.format;

import mujica.reflect.modifier.CodeHistory;
import mujica.text.escape.Quote;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2025/9/11")
class StringFailureMessage implements LocalizedFailureMessage {

    private static final long serialVersionUID = 0x9db8a2d64fa4e809L;

    @NotNull
    String string;

    StringFailureMessage(@NotNull String string) {
        super();
        this.string = string;
    }

    @NotNull
    @Override
    public LocalizedFailureMessage replace(@NotNull String mark, @NotNull String content) {
        string = string.replace(mark, content);
        return this;
    }

    @NotNull
    @Override
    public LocalizedFailureMessage replaceQuoted(@NotNull String mark, @NotNull String content) {
        string = string.replace(mark, Quote.AUTO.apply(content));
        return this;
    }

    @NotNull
    @Override
    public LocalizedFailureMessage replace(@NotNull String mark, int value) {
        string = string.replace(mark, Integer.toString(value));
        return this;
    }

    @NotNull
    @Override
    public LocalizedFailureMessage replace(@NotNull String mark, char value) {
        string = string.replace(mark, Character.toString(value));
        return this;
    }

    @NotNull
    @Override
    public LocalizedFailureMessage replaceQuoted(@NotNull String mark, char value) {
        string = string.replace(mark, Quote.AUTO.apply(value));
        return this;
    }

    @Nullable
    @Override
    public String get() {
        return string;
    }

    @Override
    public int hashCode() {
        return string.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof StringFailureMessage && this.string.equals(((StringFailureMessage) obj).string);
    }

    @Override
    public String toString() {
        return "StringFailureMessage[" + Quote.DEFAULT.apply(string) + "]";
    }
}
