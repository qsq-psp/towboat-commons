package mujica.text.format;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.DefaultByteBufHolder;
import io.netty.buffer.Unpooled;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

@CodeHistory(date = "2026/2/3")
public class UniversalStringifierUsingByteBuf extends DefaultByteBufHolder implements UniversalStringifier {

    @NotNull
    final UniversalAppender appender;

    public UniversalStringifierUsingByteBuf(@NotNull ByteBuf data, @NotNull UniversalAppender appender) {
        super(data);
        this.appender = appender;
    }

    public UniversalStringifierUsingByteBuf(@NotNull ByteBufAllocator allocator, @NotNull UniversalAppender appender) {
        this(allocator.buffer(), appender);
    }

    public UniversalStringifierUsingByteBuf(@NotNull UniversalAppender appender) {
        this(Unpooled.buffer(), appender);
    }

    @NotNull
    @Override
    public String apply(@NotNull Object object) {
        final ByteBuf buf = content();
        try {
            appender.write(object, buf);
            return buf.toString(StandardCharsets.UTF_8);
        } finally {
            buf.clear();
        }
    }
}
