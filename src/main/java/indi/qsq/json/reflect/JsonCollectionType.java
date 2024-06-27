package indi.um.json.reflect;

import indi.um.json.api.JsonConsumer;
import indi.um.json.api.ParseHint;
import indi.um.json.entity.JsonConstant;
import indi.um.json.io.JsonStringWriter;
import indi.um.json.value.IterableValueSerializer;
import indi.um.util.reflect.GenericTravel;
import indi.um.util.value.PublicBooleanSlot;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * Created on 2022/8/5.
 */
class JsonCollectionType extends JsonObjectType {

    private static final long serialVersionUID = 0x570001DBF06183F0L;

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonCollectionType.class);

    JsonType componentType;

    JsonCollectionType() {
        super();
    }

    @Override
    protected boolean linkCollectClass(Class<?> clazz) {
        super.linkCollectClass(clazz);
        return true;
    }

    @Override
    protected void linkCollectGenericType(Type genericType) {
        final Type componentGenericType = GenericTravel.search(genericType, Collection.class, "E");
        if (componentGenericType instanceof Class) {
            if (componentType == null) {
                componentType = JsonTypeBuilder.get((Class<?>) componentGenericType);
                if (componentType.linkCollectClass((Class<?>) componentGenericType)) {
                    componentType.linkCollectGenericType(componentGenericType);
                }
            }
        } else if (componentGenericType instanceof ParameterizedType) {
            Type rawGenericType = ((ParameterizedType) componentGenericType).getRawType();
            if (rawGenericType instanceof Class<?>) {
                if (componentType == null) {
                    componentType = JsonTypeBuilder.get((Class<?>) rawGenericType);
                    if (componentType.linkCollectClass((Class<?>) rawGenericType)) {
                        componentType.linkCollectGenericType(componentGenericType);
                    }
                }
            }
        } else {
            LOGGER.info("linkCollectGenericType {} {}", this, componentGenericType);
        }
        if (componentType == null) {
            componentType = new JsonType();
            componentType.setConversionConfig(this);
        }
    }

    @Override
    protected void linkSetConversionConfig(@NotNull ConversionConfig that) {
        setConversionConfig(that);
        componentType.linkSetConversionConfig(that);
    }

    @Override
    protected void linkCollectAnnotation(AnnotatedElement element, int layer) {
        super.linkCollectAnnotation(element, layer);
        if (layer == 0) {
            componentType.linkSetConversionConfig(this);
        }
        componentType.linkCollectAnnotation(element, layer + 1);
    }

    @Override
    protected JsonType finishCollection() {
        componentType = componentType.finishCollection();
        return this;
    }

    @Override
    protected void serializeValue(String key, Object value, JsonConsumer jc, JsonSerializer js) {
        IterableValueSerializer.INSTANCE.serialize(key, (Iterable<?>) value, jc, this, js);
    }

    @Override
    protected ConverterFrame frame(Object self, Getter read, boolean isObject, JsonConverter jv) {
        if (isObject) {
            if (anyParseConfig(ParseHint.WRAP_SINGLE_VALUE)) {
                return new SingleValueFrame(componentType.frame(null, null, true, jv));
            }
            return ConverterFrame.INSTANCE;
        }
        final PublicBooleanSlot modified = new PublicBooleanSlot();
        final Object value = useValue(self, read, modified, jv);
        if (value != null) {
            return new CollectionFrame(value, modified);
        } else {
            return ConverterFrame.INSTANCE;
        }
    }

    class SingleValueFrame extends DelegateFrame {

        public SingleValueFrame(@NotNull ConverterFrame delegate) {
            super(delegate);
            LOGGER.info("SingleValueFrame {} {}", JsonCollectionType.this, delegate);
        }

        @Override
        protected Object finish(JsonParser jp) {
            return parseSingleValue(delegate.finish(jp), jp);
        }
    }

    static final MethodHandle COLLECTION_ADD;

    static {
        MethodHandle collectionAdd;
        try {
            collectionAdd = MethodHandles.lookup().findVirtual(Collection.class, "add", MethodType.methodType(boolean.class, Object.class));
        } catch (ReflectiveOperationException e) {
            LOGGER.error("static init boolean Collection.add(E)", e);
            collectionAdd = null;
        }
        COLLECTION_ADD = collectionAdd;
    }

    class CollectionFrame extends ConverterFrame {

        final Object collection;

        final PublicBooleanSlot modified;

        CollectionFrame(Object collection, PublicBooleanSlot modified) {
            super();
            this.collection = collection;
            this.modified = modified;
        }

        @Override
        protected Object finish(JsonParser jp) {
            if (modified.value) {
                return collection;
            } else {
                return JsonConstant.UNDEFINED;
            }
        }

        @Override
        protected ConverterFrame openValue(boolean isObject, JsonConverter jv) {
            return componentType.frame(isObject, jv);
        }

        @Override
        protected void closeValue(Object value, boolean isObject, JsonConverter jv) {
            add(value, jv);
        }

        @Override
        protected void nullValue(JsonConverter jv) {
            add(componentType.parseNull(jv), jv);
        }

        @Override
        protected void booleanValue(boolean value, JsonConverter jv) {
            add(componentType.parseBoolean(value, jv), jv);
        }

        @Override
        protected void numberValue(long value, JsonConverter jv) {
            add(componentType.parseNumber(value, jv), jv);
        }

        @Override
        protected void numberValue(double value, JsonConverter jv) {
            add(componentType.parseNumber(value, jv), jv);
        }

        @Override
        protected void stringValue(String value, JsonConverter jv) {
            add(componentType.parseString(value, jv), jv);
        }

        void add(Object value, JsonConverter jv) {
            if (value instanceof JsonConstant) {
                if (jv.logEnabled()) {
                    LOGGER.warn("Unexpected value {} to collection {} while parsing {}", value, collection.getClass().getName(), jv);
                }
            } else {
                try {
                    COLLECTION_ADD.invoke(collection, value);
                } catch (Throwable e) {
                    if (jv.logEnabled()) {
                        LOGGER.warn("method handle invoke", e);
                    }
                }
            }
        }

        @Override
        public String toString() {
            return JsonStringWriter.stringify("JsonCollectionType.Frame", JsonCollectionType.this, null);
        }
    }

    protected Object parseSingleValue(Object value, JsonConverter jv) {
        if (!anyParseConfig(ParseHint.WRAP_SINGLE_VALUE) || value == JsonConstant.UNDEFINED) {
            return JsonConstant.UNDEFINED;
        }
        final PublicBooleanSlot modified = new PublicBooleanSlot();
        final Object collection = useValue(null, null, modified, jv);
        if (collection != null) {
            try {
                COLLECTION_ADD.invoke(collection, value);
                if (modified.value) {
                    return collection;
                }
            } catch (Throwable e) {
                if (jv.logEnabled()) {
                    LOGGER.warn("method handle invoke", e);
                }
            }
        }
        return JsonConstant.UNDEFINED;
    }

    @Override
    protected Object parseNull(JsonConverter jv) {
        return parseSingleValue(componentType.parseNull(jv), jv);
    }

    @Override
    protected Object parseBoolean(boolean value, JsonConverter jv) {
        return parseSingleValue(componentType.parseBoolean(value, jv), jv);
    }

    @Override
    protected Object parseNumber(long value, JsonConverter jv) {
        return parseSingleValue(componentType.parseNumber(value, jv), jv);
    }

    @Override
    protected Object parseNumber(double value, JsonConverter jv) {
        return parseSingleValue(componentType.parseNumber(value, jv), jv);
    }

    @Override
    protected Object parseString(String value, JsonConverter jv) {
        return parseSingleValue(componentType.parseString(value, jv), jv);
    }

    @Override
    public void toJsonEntries(@NotNull JsonConsumer jc) {
        super.toJsonEntries(jc);
        jc.key("componentType");
        componentType.toJson(jc);
    }

    @Override
    public String typeName() {
        return "collection";
    }
}
