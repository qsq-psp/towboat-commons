package mujica.json.reflect;

import mujica.algebra.random.RandomContext;
import mujica.ds.generic.set.CollectionConstant;
import mujica.json.entity.FastNumber;
import mujica.json.entity.JsonHandler;
import mujica.json.entity.JsonStructure;
import mujica.json.io.JsonCharSequenceReader;
import mujica.json.io.JsonStringBuilderWriter;
import mujica.json.io.JsonSyncReader;
import mujica.json.modifier.JsonHint;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;

@CodeHistory(date = "2021/1/2", project = "webbiton", name = "JsonLayerStack")
@CodeHistory(date = "2021/12/28", project = "va", name = "JsonParser")
@CodeHistory(date = "2022/1/3", project = "infrastructure", name = "JsonParser")
@CodeHistory(date = "2022/6/9", project = "Ultramarine", name = "JsonSerializer")
@CodeHistory(date = "2022/7/12", project = "Ultramarine", name = "JsonParser")
@CodeHistory(date = "2025/10/28", name = "JsonParser")
@CodeHistory(date = "2026/4/3")
public class JsonContext extends ReflectConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonContext.class);

    @NotNull
    Logger logger = LOGGER;

    @NotNull
    public Logger getLogger() {
        return logger;
    }

    public void setLogger(@NotNull Logger logger) {
        this.logger = logger;
    }

    @NotNull
    final RandomContext randomContext = new RandomContext();

    @NotNull
    final ContainerConfig containerConfig = new ContainerConfig();

    @NotNull
    public ContainerConfig getContainerConfig() {
        return containerConfig;
    }

    @NotNull
    final HashMap<Class<?>, JsonType> reflectCache = new HashMap<>();

    @NotNull
    JsonType forClass(@NotNull Class<?> clazz) {
        // do not use computeIfAbsent; ConcurrentModificationException
        JsonType type = reflectCache.get(clazz);
        if (type == null) {
            type = newForClass(clazz);
            type = type.collectType(clazz, this);
            reflectCache.put(clazz, type);
        }
        return type;
    }

    @NotNull
    private JsonType newForClass(@NotNull Class<?> clazz) {
        if (clazz.isArray()) {
            return new JavaArrayType(flags | JsonHint.NULLABLE);
        }
        if (Collection.class.isAssignableFrom(clazz)) {
            return new UtilCollectionType(flags | JsonHint.NULLABLE);
        }
        if (clazz.isEnum()) {
            return new JavaEnumType(flags | JsonHint.NULLABLE);
        }
        return new PlainObjectType.MapBased(flags | JsonHint.NULLABLE);
    }

    private Setter collectionAdd;

    @NotNull
    Setter getCollectionAdd() throws ReflectiveOperationException {
        if (collectionAdd == null) {
            if ((flags & JsonHint.USE_METHOD_HANDLE) != 0L) {
                collectionAdd = new MethodHandleSetter(MethodHandles.lookup().findVirtual(Collection.class, "add", MethodType.methodType(boolean.class, Object.class)));
            } else {
                collectionAdd = new ClassicalMethodSetter(Collection.class.getDeclaredMethod("add", Object.class));
            }
        }
        return collectionAdd;
    }

    private Setter jsonTransform;

    @NotNull
    Setter getJsonTransform() throws ReflectiveOperationException {
        if (jsonTransform == null) {
            if ((flags & JsonHint.USE_METHOD_HANDLE) != 0L) {
                jsonTransform = new MethodHandleSetter(MethodHandles.lookup().findVirtual(JsonContextTransformer.class, "transform", MethodType.methodType(void.class, Object.class, JsonHandler.class, JsonContext.class)));
            } else {
                jsonTransform = new ClassicalMethodSetter(JsonContextTransformer.class.getDeclaredMethod("transform", Object.class, JsonHandler.class, JsonContext.class));
            }
        }
        return jsonTransform;
    }

    @NotNull
    final IdentityHashMap<Object, CollectionConstant> loopDetector = new IdentityHashMap<>();

    public boolean addContainerObject(@NotNull Object object) {
        return loopDetector.put(object, CollectionConstant.PRESENT) == null;
    }

    JsonContext(long flags) {
        super(flags);
        flags &= ~(JsonHint.UNSIGNED | JsonHint.DERIVED);
        reflectCache.put(boolean.class, new BooleanType(flags & ~JsonHint.NULLABLE));
        reflectCache.put(Boolean.class, new BooleanType(flags));
        reflectCache.put(byte.class, new ByteType(flags & ~JsonHint.NULLABLE));
        reflectCache.put(Byte.class, new ByteType(flags));
        reflectCache.put(int.class, new IntType(flags & ~JsonHint.NULLABLE));
        reflectCache.put(Integer.class, new IntType(flags));
        reflectCache.put(int[].class, new IntArrayType(flags, (IntType) reflectCache.get(int.class)));
        reflectCache.put(long.class, new LongType(flags & ~JsonHint.NULLABLE));
        reflectCache.put(Long.class, new LongType(flags));
        reflectCache.put(FastNumber.class, new FastNumberType(flags));
    }

    JsonContext() {
        this(0L);
    }

    public void parse(@NotNull JsonSyncReader reader, @NotNull Object root) {
        reader.read(new JsonParserStack(new ImmutableFrame(this, root)));
    }

    public void parse(@NotNull CharSequence string, @NotNull Object root) {
        parse(new JsonCharSequenceReader(string), root);
    }

    public void transform(Object in, @NotNull JsonHandler out) {
        if (in == null) {
            out.nullValue();
            return;
        }
        if (in instanceof JsonStructure) {
            if (in instanceof JsonStructure.ExposedEntries) {
                out.openObject();
                ((JsonStructure) in).json(out);
                out.closeObject();
            } else {
                ((JsonStructure) in).json(out);
            }
            return;
        }
        if (!loopDetector.isEmpty()) {
            loopDetector.clear();
        }
        forClass(in.getClass()).transform(in, out, this);
    }

    @NotNull
    public String stringify(Object in) {
        final JsonStringBuilderWriter writer = new JsonStringBuilderWriter();
        transform(in, writer);
        return writer.getString();
    }
}
