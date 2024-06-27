package indi.um.json.value;

import indi.um.json.api.JsonConsumer;
import indi.um.json.api.ValueSerializer;
import indi.um.json.reflect.ConversionConfig;
import indi.um.json.reflect.JsonSerializer;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.*;

/**
 * Created on 2022/7/22.
 */
@SuppressWarnings("unused")
public class TypeValueSerializer implements ValueSerializer<Type> {

    @Override
    public void serialize(String key, Type value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        jc.optionalKey(key);
        if (value == null) {
            jc.nullValue();
            return;
        }
        jc.openObject();
        if (value instanceof Class) {
            serializeClass((Class<?>) value, jc);
        } else if (value instanceof ParameterizedType) {
            jc.key("type");
            jc.stringValue("ParameterizedType");
            if (js.add(value)) {
                ParameterizedType parameterizedType = (ParameterizedType) value;
                jc.key("actualTypeArguments");
                jc.openArray();
                for (Type type : parameterizedType.getActualTypeArguments()) {
                    serialize(null, type, jc, cc, js);
                }
                jc.closeArray();
                serialize("rawType", parameterizedType.getRawType(), jc, cc, js);
                serialize("ownerType", parameterizedType.getOwnerType(), jc, cc, js);
                jc.closeArray();
                js.remove(value);
            } else {
                jc.key("recursive");
                jc.booleanValue(true);
            }
        } else if (value instanceof TypeVariable) {
            jc.key("type");
            jc.stringValue("TypeVariable");
            if (js.add(value)) {
                TypeVariable<?> typeVariable = (TypeVariable<?>) value;
                jc.key("name");
                jc.stringValue(typeVariable.getName());
                jc.key("bounds");
                jc.openArray();
                for (Type type : typeVariable.getBounds()) {
                    serialize(null, type, jc, cc, js);
                }
                jc.closeArray();
                jc.key("parameters");
                jc.openArray();
                for (Type type : typeVariable.getGenericDeclaration().getTypeParameters()) {
                    serialize(null, type, jc, cc, js);
                }
                jc.closeArray();
                js.remove(value);
            } else {
                jc.key("recursive");
                jc.booleanValue(true);
            }
        } else if (value instanceof GenericArrayType) {
            jc.key("type");
            jc.stringValue("GenericArrayType");
            if (js.add(value)) {
                serialize("component", ((GenericArrayType) value).getGenericComponentType(), jc, cc, js);
                js.remove(value);
            }
        } else if (value instanceof WildcardType) {
            jc.key("type");
            jc.stringValue("WildcardType");
            if (js.add(value)) {
                WildcardType wildcardType = (WildcardType) value;
                jc.key("upperBounds");
                jc.openArray();
                for (Type type : wildcardType.getUpperBounds()) {
                    serialize(null, type, jc, cc, js);
                }
                jc.closeArray();
                jc.key("lowerBounds");
                jc.openArray();
                for (Type type : wildcardType.getLowerBounds()) {
                    serialize(null, type, jc, cc, js);
                }
                jc.closeArray();
                js.remove(value);
            }
        } else {
            jc.key("type");
            jc.stringValue(value.getClass().getSimpleName());
        }
        jc.closeObject();
    }

    void serialize(String key, Type value, @NotNull JsonConsumer jc, int depth) {
        jc.optionalKey(key);
        if (value == null) {
            jc.nullValue();
            return;
        }
        if (--depth < 0) {
            jc.openObject();
            jc.key("recursive");
            jc.booleanValue(true);
            jc.closeObject();
            return;
        }
        jc.openObject();
        if (value instanceof Class) {
            serializeClass((Class<?>) value, jc);
        } else if (value instanceof ParameterizedType) {
            jc.key("type");
            jc.stringValue("ParameterizedType");
            ParameterizedType parameterizedType = (ParameterizedType) value;
            jc.key("actualTypeArguments");
            jc.openArray();
            for (Type type : parameterizedType.getActualTypeArguments()) {
                serialize(null, type, jc, depth);
            }
            jc.closeArray();
            serialize("rawType", parameterizedType.getRawType(), jc, depth);
            serialize("ownerType", parameterizedType.getOwnerType(), jc, depth);
            jc.closeArray();
        } else if (value instanceof TypeVariable) {
            jc.key("type");
            jc.stringValue("TypeVariable");
            TypeVariable<?> typeVariable = (TypeVariable<?>) value;
            jc.key("name");
            jc.stringValue(typeVariable.getName());
            jc.key("bounds");
            jc.openArray();
            for (Type type : typeVariable.getBounds()) {
                serialize(null, type, jc, depth);
            }
            jc.closeArray();
            jc.key("parameters");
            jc.openArray();
            for (Type type : typeVariable.getGenericDeclaration().getTypeParameters()) {
                serialize(null, type, jc, depth);
            }
            jc.closeArray();
        } else if (value instanceof GenericArrayType) {
            jc.key("type");
            jc.stringValue("GenericArrayType");
            serialize("component", ((GenericArrayType) value).getGenericComponentType(), jc, depth);
        } else if (value instanceof WildcardType) {
            jc.key("type");
            jc.stringValue("WildcardType");
            WildcardType wildcardType = (WildcardType) value;
            jc.key("upperBounds");
            jc.openArray();
            for (Type type : wildcardType.getUpperBounds()) {
                serialize(null, type, jc, depth);
            }
            jc.closeArray();
            jc.key("lowerBounds");
            jc.openArray();
            for (Type type : wildcardType.getLowerBounds()) {
                serialize(null, type, jc, depth);
            }
            jc.closeArray();
        } else {
            jc.key("type");
            jc.stringValue(value.getClass().getSimpleName());
        }
        jc.closeObject();
    }

    private void serializeClass(@NotNull Class<?> value, @NotNull JsonConsumer jc) {
        jc.key("type");
        jc.stringValue("Class");
        int array = 0;
        while (value.isArray()) {
            array++;
            value = value.getComponentType();
        }
        String module = value.getModule().getName();
        if (module != null) {
            jc.key("module");
            jc.stringValue(module);
        }
        if (array != 0) {
            jc.key("array");
            jc.numberValue(array);
        }
        jc.key("class");
        jc.stringValue(value.getName());
    }
}
