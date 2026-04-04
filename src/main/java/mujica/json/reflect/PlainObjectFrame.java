package mujica.json.reflect;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2026/4/3")
public class PlainObjectFrame extends JsonParserFrame {

    @NotNull
    final JsonType type;

    @NotNull
    final Object self;

    public PlainObjectFrame(@NotNull JsonContext context, @NotNull JsonType type, @NotNull Object self) {
        super(context);
        this.type = type;
        this.self = self;
    }

    public PlainObjectFrame(@NotNull JsonParserFrame bottom, @NotNull JsonType type, @NotNull Object self) {
        super(bottom);
        this.type = type;
        this.self = self;
    }

    @Override
    public void structureValue(@Nullable Object value) {
        if (key instanceof PlainObjectField) {
            ((PlainObjectField) key).setter.invoke(self, value);
        }
    }

    @Override
    public void simpleValue(@Nullable Object value) {
        if (key instanceof PlainObjectField) {
            ((PlainObjectField) key).setter.invoke(self, value);
        }
    }
}
