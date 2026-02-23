package mujica.text.format;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.DefaultByteBufHolder;
import io.netty.buffer.Unpooled;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

@CodeHistory(date = "2026/1/31")
public class CharSequenceOperatorUsingByteBuf extends DefaultByteBufHolder implements CharSequenceOperator {

    @NotNull
    final CharSequenceAppender appender;

    public CharSequenceOperatorUsingByteBuf(@NotNull ByteBuf data, @NotNull CharSequenceAppender appender) {
        super(data);
        this.appender = appender;
    }

    public CharSequenceOperatorUsingByteBuf(@NotNull ByteBufAllocator allocator, @NotNull CharSequenceAppender appender) {
        this(allocator.buffer(), appender);
    }

    public CharSequenceOperatorUsingByteBuf(@NotNull CharSequenceAppender appender) {
        this(Unpooled.buffer(), appender);
    }

    @NotNull
    @Override
    public String apply(@NotNull CharSequence string) {
        final ByteBuf buf = content();
        try {
            appender.write(string, buf);
            return buf.toString(StandardCharsets.UTF_8);
        } finally {
            buf.clear();
        }
    }

    @NotNull
    @Override
    public String apply(@NotNull CharSequence string, int startIndex, int endIndex) {
        final ByteBuf buf = content();
        try {
            appender.write(string, startIndex, endIndex, buf);
            return buf.toString(StandardCharsets.UTF_8);
        } finally {
            buf.clear();
        }
    }
}
