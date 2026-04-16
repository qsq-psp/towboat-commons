package mujica.json.reflect;

import mujica.json.modifier.JsonHint;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Modifier;

@CodeHistory(date = "2022/7/23", project = "Ultramarine", name = "JsonObjectType")
@CodeHistory(date = "2026/4/10")
abstract class BuiltType extends JsonType {

    private static final long serialVersionUID = 0xb0b855e077ad6f57L;

    @NotNull
    Getter builder = Getter.NOP;

    BuiltType(long flags) {
        super(flags);
    }

    BuiltType(long flags, @NotNull Getter builder) {
        super(flags);
        this.builder = builder;
    }

    @NotNull
    @Override
    JsonType collectType(@NotNull Class<?> clazz, @NotNull JsonContext context) {
        collectConfig(clazz, false);
        collectConstructor(clazz, context);
        return this;
    }

    void collectConstructor(@NotNull Class<?> clazz, @NotNull JsonContext context) {
        final int modifiers = clazz.getModifiers();
        if ((Modifier.FINAL & modifiers) != 0) {
            flags &= ~JsonHint.DYNAMIC_TYPE;
        }
        if (((Modifier.PUBLIC | Modifier.INTERFACE | Modifier.ABSTRACT) & modifiers) == Modifier.PUBLIC) { // public and not abstract class
            try {
                builder = new ClassicalConstructor(clazz.getConstructor());
                if ((flags & JsonHint.USE_METHOD_HANDLE) != 0L) {
                    builder = builder.unreflect(MethodHandles.lookup());
                }
            } catch (Throwable e) {
                context.getLogger().warn("", e);
            }
        }
    }
}
