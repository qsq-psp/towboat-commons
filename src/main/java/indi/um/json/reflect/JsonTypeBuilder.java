package indi.um.json.reflect;

import indi.um.json.api.JsonStructure;
import indi.um.util.reflect.GenericTravel;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Hashtable;

/**
 * Created on 2022/8/27.
 */
public final class JsonTypeBuilder {

    static final Hashtable<Class<?>, JsonType> TYPE = new Hashtable<>();

    static {
        JsonType prototype = new JsonObjectType(); // it was new JsonType();
        TYPE.put(Object.class, prototype);
        prototype = new JsonBooleanTypeBuilder();
        TYPE.put(boolean.class, prototype);
        TYPE.put(Boolean.class, prototype);
        prototype = new JsonByteType();
        TYPE.put(byte.class, prototype);
        TYPE.put(Byte.class, prototype);
        prototype = new JsonIntTypeBuilder();
        TYPE.put(int.class, prototype);
        TYPE.put(Integer.class, prototype);
        prototype = new JsonLongType();
        TYPE.put(long.class, prototype);
        TYPE.put(Long.class, prototype);
        prototype = new JsonFloatType();
        TYPE.put(float.class, prototype);
        TYPE.put(Float.class, prototype);
        prototype = new JsonDoubleType();
        TYPE.put(double.class, prototype);
        TYPE.put(Double.class, prototype);
        prototype = new JsonStringType();
        TYPE.put(String.class, prototype);
        prototype = new JsonByteArrayType();
        TYPE.put(byte[].class, prototype);
        TYPE.put(Byte[].class, prototype);
        TYPE.put(ByteBuffer.class, prototype);
        TYPE.put(ByteBuf.class, prototype);
    }

    static JsonType get(Class<?> clazz) {
        JsonType type = TYPE.get(clazz); // prototype, should clone before use, avoid direct use
        if (type != null) {
            type = type.clone();
        } else if (clazz.isArray()) {
            type = new JsonArrayType(get(clazz.getComponentType()));
        } else if (Collection.class.isAssignableFrom(clazz)) {
            type = new JsonCollectionType();
        } else if (JsonStructure.class.isAssignableFrom(clazz)) {
            type = new JsonStructureObjectType();
        } else if (clazz.isEnum()) {
            type = new JsonEnumType();
        } else {
            type = new JsonObjectType();
        }
        return type;
    }

    public static JsonType get(Type genericType) {
        Class<?> clazz = GenericTravel.castToClass(genericType);
        if (clazz == null) {
            clazz = Object.class;
        }
        final JsonType jsonType = get(clazz);
        if (jsonType.linkCollectClass(clazz)) {
            jsonType.linkCollectGenericType(genericType);
        }
        return jsonType.finishCollection();
    }
}
