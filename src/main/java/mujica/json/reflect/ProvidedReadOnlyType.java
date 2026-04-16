package mujica.json.reflect;

import mujica.json.entity.JsonHandler;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@CodeHistory(date = "2026/4/6")
class ProvidedReadOnlyType extends JsonType {

    private static final long serialVersionUID = 0x1BB94FD0B2BE09CDL;

    @NotNull
    final String transformerName;

    @Nullable
    ReflectedTransformer transformer;

    public ProvidedReadOnlyType(@NotNull String transformerName) {
        super();
        this.transformerName = transformerName;
    }

    @NotNull
    public ReflectedTransformer getTransformer(@NotNull JsonContext context) throws ReflectiveOperationException {
        ReflectedTransformer transformer = this.transformer;
        if (transformer == null) {
            Class<?> clazz = Class.forName(transformerName);
            JsonContextTransformer<?> instance;
            try {
                Field field = clazz.getField("INSTANCE");
                if (((Modifier.PUBLIC | Modifier.PRIVATE | Modifier.PROTECTED | Modifier.STATIC) & field.getModifiers()) == (Modifier.PUBLIC | Modifier.STATIC) && field.getType() == clazz) {
                    instance = (JsonContextTransformer<?>) field.get(null);
                } else {
                    throw new NoSuchFieldException("not accepted INSTANCE field");
                }
            } catch (NoSuchFieldException e) {
                instance = (JsonContextTransformer<?>) clazz.getConstructor().newInstance();
            }
            transformer = context.getJsonTransform().bind(instance);
            this.transformer = transformer;
        }
        return transformer;
    }

    @Override
    public void transform(Object in, @NotNull JsonHandler out, @NotNull JsonContext context) {
        try {
            getTransformer(context).transform(in, out, context);
        } catch (Throwable e) {
            context.getLogger().error("", e);
        }
    }
}
