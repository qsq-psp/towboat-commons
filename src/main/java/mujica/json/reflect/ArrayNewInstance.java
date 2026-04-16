package mujica.json.reflect;

import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;

/**
 * Created on 2026/4/10.
 */
class ArrayNewInstance extends Getter {

    @NotNull
    final Class<?> arrayClass;

    ArrayNewInstance(@NotNull Class<?> arrayClass) {
        super();
        this.arrayClass = arrayClass;
    }

    @Override
    protected Object get(Object self) {
        return Array.newInstance(arrayClass.getComponentType(), (Integer) self);
    }

    @NotNull
    @Override
    protected Getter unreflect(@NotNull MethodHandles.Lookup lookup) {
        return new MethodHandleGetter(MethodHandles.arrayConstructor(arrayClass));
    }
}
