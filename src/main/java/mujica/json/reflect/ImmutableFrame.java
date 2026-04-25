package mujica.json.reflect;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2020/12/23", project = "webbiton", name = "JsonObjectLayer")
@CodeHistory(date = "2022/1/3", project = "infrastructure", name = "BottomFrame")
@CodeHistory(date = "2022/7/20", project = "Ultramarine", name = "BottomFrame")
@CodeHistory(date = "2026/4/9")
class ImmutableFrame extends NopFrame {

    public ImmutableFrame(@NotNull JsonContext context, @NotNull Object key) {
        super(context); // given frame is always bottom
        this.key = key;
    }

    @Override
    public void setKey(Object key) {
        // pass; key is given and immutable
    }

    @NotNull
    @Override
    NopFrame open() {
        return context.forClass(key.getClass()).createFrame(this);
    }

    @Nullable
    @Override
    Object close() {
        return key;
    }
}
