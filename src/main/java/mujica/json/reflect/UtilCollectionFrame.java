package mujica.json.reflect;

import mujica.ds.generic.set.CollectionConstant;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2026/4/8")
class UtilCollectionFrame extends NopFrame {

    @NotNull
    final JsonType componentType;

    @NotNull
    final Object collection;

    public UtilCollectionFrame(@NotNull JsonContext context, @NotNull JsonType componentType, @NotNull Object collection) {
        super(context);
        this.componentType = componentType;
        this.collection = collection;
    }

    public UtilCollectionFrame(@NotNull NopFrame bottom, @NotNull JsonType componentType, @NotNull Object collection) {
        super(bottom);
        this.componentType = componentType;
        this.collection = collection;
    }

    @NotNull
    @Override
    NopFrame open() {
        setKey(null);
        return componentType.createFrame(this);
    }

    @Nullable
    @Override
    Object close() {
        return collection;
    }

    @Override
    public void structureValue(@Nullable Object value) {
        if (value != CollectionConstant.UNDEFINED) {
            try {
                context.getCollectionAdd().set(collection, value);
            } catch (Throwable e) {
                context.getLogger().warn("add structure {} to {}", value, collection.getClass(), e);
            }
        }
    }

    @Override
    public void simpleValue(@Nullable Object value) {
        value = componentType.from(value).toObject(context);
        if (value != CollectionConstant.UNDEFINED) {
            try {
                context.getCollectionAdd().set(collection, value);
            } catch (Throwable e) {
                context.getLogger().warn("add simple {} to {}", value, collection.getClass(), e);
            }
        }
    }
}
