package mujica.json.provided;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.modifier.JsonEmpty;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.json.reflect.ReflectConfig;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.*;

@CodeHistory(date = "2022/7/22", project = "Ultramarine", name = "TypeValueSerializer")
@CodeHistory(date = "2026/5/20")
public class TypeTransformer implements JsonContextTransformer<Type> {

    static final FastString TYPE = new FastString("type");

    @Override
    public void transform(@NotNull Type in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(ClassLoaderTransformer.CLASS);
            out.stringValue(in.getClass().getName());
            out.stringKey(ClassLoaderTransformer.NAME);
            out.stringValue(in.getTypeName());
        }
        if (in instanceof Class) {
            out.closeObject();
            return;
        }
        if (context == null) {
            context = new JsonContext();
        }
        if (context.addContainerObject(in)) {
            try {
                if (in instanceof ParameterizedType) {
                    transformExposedParameterized((ParameterizedType) in, out, context);
                } else if (in instanceof TypeVariable) {
                    transformExposedTypeVariable((TypeVariable<?>) in, out, context);
                } else if (in instanceof WildcardType) {
                    transformExposedWildcard((WildcardType) in, out, context);
                } else if (in instanceof GenericArrayType) {
                    transformExposedGenericArray((GenericArrayType) in, out, context);
                }
            } finally {
                context.removeContainerObject(in);
            }
        }
        out.closeObject();
    }

    static void transformNullable(@Nullable Type in, @NotNull JsonHandler out, @NotNull JsonContext context) {
        if (in == null) {
            out.nullValue();
            return;
        }
        if (context.addContainerObject(in)) {
            try {
                out.openObject();
                {
                    out.stringKey(ClassLoaderTransformer.CLASS);
                    out.stringValue(in.getClass().getName());
                    out.stringKey(ClassLoaderTransformer.NAME);
                    out.stringValue(in.getTypeName());
                }
                if (in instanceof ParameterizedType) {
                    transformExposedParameterized((ParameterizedType) in, out, context);
                } else if (in instanceof TypeVariable) {
                    transformExposedTypeVariable((TypeVariable<?>) in, out, context);
                } else if (in instanceof WildcardType) {
                    transformExposedWildcard((WildcardType) in, out, context);
                } else if (in instanceof GenericArrayType) {
                    transformExposedGenericArray((GenericArrayType) in, out, context);
                }
                out.closeObject();
            } finally {
                context.removeContainerObject(in);
            }
        } else if (context.any(((long) JsonEmpty.FROM_LOOP_OBJECT) << ReflectConfig.UNDEFINED_SHIFT)) {
            context.getLogger().debug("type loop to undefined");
            out.skippedValue();
        } else if (context.any(((long) JsonEmpty.FROM_LOOP_OBJECT) << ReflectConfig.NULL_SHIFT)) {
            context.getLogger().debug("type loop to null");
            out.nullValue();
        } else {
            throw new RuntimeException("loop");
        }
    }

    static void transformExposedParameterized(@NotNull ParameterizedType in, @NotNull JsonHandler out, @NotNull JsonContext context) {
        {
            Type[] arguments = in.getActualTypeArguments();
            out.stringKey("arguments");
            out.openArray();
            for (Type argument : arguments) {
                transformNullable(argument, out, context);
            }
            out.closeArray();
        }
        {
            Type raw = in.getRawType();
            out.stringKey("raw");
            transformNullable(raw, out, context);
        }
        {
            Type owner = in.getOwnerType();
            out.stringKey("owner");
            transformNullable(owner, out, context);
        }
    }

    static void transformExposedTypeVariable(@NotNull TypeVariable<?> in, @NotNull JsonHandler out, @NotNull JsonContext context) {
        {
            Type[] bounds = in.getBounds();
            out.stringKey("bounds");
            out.openArray();
            for (Type bound : bounds) {
                transformNullable(bound, out, context);
            }
            out.closeArray();
        }
        {
            out.stringKey("typeVariableName");
            out.stringValue(in.getName());
        }
    }

    static void transformExposedWildcard(@NotNull WildcardType in, @NotNull JsonHandler out, @NotNull JsonContext context) {
        {
            Type[] upperBounds = in.getUpperBounds();
            out.stringKey("upperBounds");
            out.openArray();
            for (Type bound : upperBounds) {
                transformNullable(bound, out, context);
            }
            out.closeArray();
        }
        {
            Type[] lowerBounds = in.getLowerBounds();
            out.stringKey("lowerBounds");
            out.openArray();
            for (Type bound : lowerBounds) {
                transformNullable(bound, out, context);
            }
            out.closeArray();
        }
    }

    static void transformExposedGenericArray(@NotNull GenericArrayType in, @NotNull JsonHandler out, @NotNull JsonContext context) {
        final Type component = in.getGenericComponentType();
        out.stringKey("component");
        transformNullable(component, out, context);
    }
}
