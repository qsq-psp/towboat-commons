package mujica.text.format;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created on 2025/9/11.
 */
class BooleanFailureMessage implements LocalizedFailureMessage {

    private static final long serialVersionUID = 0x508B4BA193B5AA30L;

    static final BooleanFailureMessage SUCCESS = new BooleanFailureMessage(true);

    static final BooleanFailureMessage FAIL = new BooleanFailureMessage(false);

    final boolean pass;

    BooleanFailureMessage(boolean pass) {
        super();
        this.pass = pass;
    }

    @NotNull
    @Override
    public LocalizedFailureMessage replace(@NotNull String mark, @NotNull String content) {
        return this;
    }

    @NotNull
    @Override
    public LocalizedFailureMessage replaceQuoted(@NotNull String mark, @NotNull String content) {
        return this;
    }

    @NotNull
    @Override
    public LocalizedFailureMessage replace(@NotNull String mark, int value) {
        return this;
    }

    @NotNull
    @Override
    public LocalizedFailureMessage replace(@NotNull String mark, char value) {
        return this;
    }

    @NotNull
    @Override
    public LocalizedFailureMessage replaceQuoted(@NotNull String mark, char value) {
        return this;
    }

    @Nullable
    @Override
    public String get() {
        return pass ? null : "";
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(pass);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BooleanFailureMessage && this.pass == ((BooleanFailureMessage) obj).pass;
    }

    @Override
    public String toString() {
        return "BooleanFailureMessage[" + pass + "]";
    }
}
