package mujica.json.reflect;

import io.netty.buffer.ByteBuf;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedList;

@CodeHistory(date = "2026/4/19")
class StrictOrderObjectFrame extends PlainObjectFrame {

    final HashMap<String, ByteBuf> map = new HashMap<>();

    final LinkedList<PlainObjectField> queue;

    StrictOrderObjectFrame(@NotNull NopFrame bottom, @NotNull PlainObjectType type, @NotNull Object self) {
        super(bottom, type, self);
        queue = new LinkedList<>(type.fieldCollection());
    }

    @Nullable
    @Override
    Object close() {
        for (ByteBuf byteBuf : map.values()) {
            byteBuf.release();
        }
        return self;
    }
}
