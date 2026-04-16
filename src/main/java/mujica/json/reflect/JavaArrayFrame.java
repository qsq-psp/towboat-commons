package mujica.json.reflect;

import mujica.ds.generic.set.CollectionConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created on 2026/4/10.
 */
class JavaArrayFrame extends NopFrame {

    @NotNull
    final JavaArrayType type;

    @NotNull
    final ArrayList<Object> list = new ArrayList<>();

    JavaArrayFrame(@NotNull JsonContext context, @NotNull JavaArrayType type) {
        super(context);
        this.type = type;
    }

    JavaArrayFrame(@NotNull NopFrame bottom, @NotNull JavaArrayType type) {
        super(bottom);
        this.type = type;
    }

    @NotNull
    @Override
    public NopFrame open() {
        setKey(null);
        return type.componentType.createFrame(this);
    }

    @Nullable
    @Override
    public Object close() {
        final int length = list.size();
        try {
            Object array = type.builder.get(length);
            for (int index = 0; index < length; index++) {
                Array.set(array, index, list.get(index));
            }
            return array;
        } catch (Throwable e) {
            context.getLogger().warn("", e);
            return CollectionConstant.UNDEFINED;
        }
    }

    @Override
    public void structureValue(@Nullable Object value) {
        list.add(value);
    }

    @Override
    public void simpleValue(@Nullable Object value) {
        list.add(value);
    }
}
