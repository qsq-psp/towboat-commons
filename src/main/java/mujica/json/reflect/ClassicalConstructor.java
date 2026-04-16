package mujica.json.reflect;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;

@CodeHistory(date = "2022/6/4", project = "Ultramarine", name = "ConstructorReflectGetter")
@CodeHistory(date = "2026/4/10")
class ClassicalConstructor extends Getter {

    @NotNull
    final Constructor<?> constructor;

    ClassicalConstructor(@NotNull Constructor<?> constructor) {
        super();
        this.constructor = constructor;
    }

    @Override
    protected Object get(Object self) throws Throwable {
        return constructor.newInstance();
    }

    @NotNull
    @Override
    protected Getter unreflect(@NotNull MethodHandles.Lookup lookup) throws Throwable {
        return new MethodHandleGetter(lookup.unreflectConstructor(constructor));
    }
}
