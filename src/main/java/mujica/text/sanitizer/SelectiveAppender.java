package mujica.text.sanitizer;

import io.netty.buffer.ByteBuf;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

@CodeHistory(date = "2026/1/30")
public abstract class SelectiveAppender extends CharSequenceAppender {

    @NotNull
    protected final CharSequenceAppender[] sequence;

    protected SelectiveAppender(@NotNull CharSequenceAppender[] sequence) {
        super();
        this.sequence = sequence;
    }

    @NotNull
    protected abstract CharSequenceAppender select(@NotNull CharSequence string);

    @NotNull
    protected abstract CharSequenceAppender select(@NotNull CharSequence string,
                                          @Index(of = "string") int startIndex,
                                          @Index(of = "string", inclusive = false) int endIndex);

    @Override
    public boolean isApplicable(@NotNull CharSequence string) {
        // reverse iteration, because always-applicable appender is likely at last position
        for (int index = sequence.length - 1; index >= 0; index--) {
            if (sequence[index].isApplicable(string)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isApplicable(@NotNull CharSequence string, int startIndex, int endIndex) {
        // reverse iteration, because always-applicable appender is likely at last position
        for (int index = sequence.length - 1; index >= 0; index--) {
            if (sequence[index].isApplicable(string, startIndex, endIndex)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isIdentity(@NotNull CharSequence string) {
        return select(string).isIdentity(string);
    }

    @Override
    public boolean isIdentity(@NotNull CharSequence string, int startIndex, int endIndex) {
        return select(string, startIndex, endIndex).isIdentity(string, startIndex, endIndex);
    }

    @Override
    public int deltaCharCount(@NotNull CharSequence string) {
        return select(string).deltaCharCount(string);
    }

    @Override
    public int deltaCharCount(@NotNull CharSequence string, int startIndex, int endIndex) {
        return select(string, startIndex, endIndex).deltaCharCount(string, startIndex, endIndex);
    }

    @Override
    public int charEditDistance(@NotNull CharSequence string) {
        return select(string).charEditDistance(string);
    }

    @Override
    public int charEditDistance(@NotNull CharSequence string, int startIndex, int endIndex) {
        return select(string, startIndex, endIndex).charEditDistance(string, startIndex, endIndex);
    }

    @Override
    public void write(@NotNull CharSequence string, @NotNull ByteBuffer out) {
        select(string).write(string, out);
    }

    @Override
    public void write(@NotNull CharSequence string, int startIndex, int endIndex, @NotNull ByteBuffer out) {
        select(string, startIndex, endIndex).write(string, startIndex, endIndex, out);
    }

    @Override
    public void write(@NotNull CharSequence string, @NotNull ByteBuf out) {
        select(string).write(string, out);
    }

    @Override
    public void write(@NotNull CharSequence string, int startIndex, int endIndex, @NotNull ByteBuf out) {
        select(string, startIndex, endIndex).write(string, startIndex, endIndex, out);
    }

    @Override
    public void append(@NotNull CharSequence string, @NotNull StringBuilder out) {
        select(string).append(string, out);
    }

    @Override
    public void append(@NotNull CharSequence string, int startIndex, int endIndex, @NotNull StringBuilder out) {
        select(string, startIndex, endIndex).append(string, startIndex, endIndex, out);
    }

    @Override
    public void append(@NotNull CharSequence string, @NotNull StringBuffer out) {
        select(string).append(string, out);
    }

    @Override
    public void append(@NotNull CharSequence string, int startIndex, int endIndex, @NotNull StringBuffer out) {
        select(string, startIndex, endIndex).append(string, startIndex, endIndex, out);
    }

    @Override
    public void print(@NotNull CharSequence string, @NotNull PrintStream out) {
        select(string).print(string, out);
    }

    @Override
    public void print(@NotNull CharSequence string, int startIndex, int endIndex, @NotNull PrintStream out) {
        select(string, startIndex, endIndex).print(string, startIndex, endIndex, out);
    }

    @Override
    public void addTokens(@NotNull CharSequence string, @NotNull List<Object> out) {
        select(string).addTokens(string, out);
    }

    @Override
    public void addTokens(@NotNull CharSequence string, int startIndex, int endIndex, @NotNull List<Object> out) {
        select(string, startIndex, endIndex).addTokens(string, startIndex, endIndex, out);
    }

    @CodeHistory(date = "2026/1/31")
    public static class NotApplicableException extends RuntimeException {

        public NotApplicableException() {
            super();
        }
    }

    @CodeHistory(date = "2026/1/30")
    public static class FirstApplicable extends SelectiveAppender {

        public FirstApplicable(@NotNull CharSequenceAppender[] sequence) {
            super(sequence);
        }

        @NotNull
        @Override
        protected CharSequenceAppender select(@NotNull CharSequence string) {
            for (CharSequenceAppender appender : sequence) {
                if (appender.isApplicable(string)) {
                    return appender;
                }
            }
            throw new NotApplicableException();
        }

        @NotNull
        @Override
        protected CharSequenceAppender select(@NotNull CharSequence string, int startIndex, int endIndex) {
            for (CharSequenceAppender appender : sequence) {
                if (appender.isApplicable(string, startIndex, endIndex)) {
                    return appender;
                }
            }
            throw new NotApplicableException();
        }
    }

    @CodeHistory(date = "2026/1/31")
    public static class LeastCharCount extends SelectiveAppender {

        public LeastCharCount(@NotNull CharSequenceAppender[] sequence) {
            super(sequence);
        }

        @NotNull
        @Override
        protected CharSequenceAppender select(@NotNull CharSequence string) {
            CharSequenceAppender minAppender = null;
            int minValue = Integer.MAX_VALUE;
            for (CharSequenceAppender appender : sequence) {
                if (appender.isApplicable(string)) {
                    int value = appender.deltaCharCount(string);
                    if (value < minValue) {
                        minAppender = appender;
                        minValue = value;
                    }
                }
            }
            if (minAppender == null) {
                throw new NotApplicableException();
            }
            return minAppender;
        }

        @NotNull
        @Override
        protected CharSequenceAppender select(@NotNull CharSequence string, int startIndex, int endIndex) {
            CharSequenceAppender minAppender = null;
            int minValue = Integer.MAX_VALUE;
            for (CharSequenceAppender appender : sequence) {
                if (appender.isApplicable(string, startIndex, endIndex)) {
                    int value = appender.deltaCharCount(string, startIndex, endIndex);
                    if (value < minValue) {
                        minAppender = appender;
                        minValue = value;
                    }
                }
            }
            if (minAppender == null) {
                throw new NotApplicableException();
            }
            return minAppender;
        }
    }

    @Override
    public String toString() {
        return "SelectiveAppender" + Arrays.toString(sequence);
    }
}
