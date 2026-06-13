package mujica.json.reflect;

import mujica.algebra.random.RandomContext;
import mujica.ds.generic.set.CollectionConstant;
import mujica.json.container.FastNumber;
import mujica.json.handler.JsonHandler;
import mujica.json.handler.JsonStructure;
import mujica.json.io.JsonCharSequenceReader;
import mujica.json.io.JsonStringBuilderWriter;
import mujica.json.io.JsonSyncReader;
import mujica.json.modifier.JsonHint;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.regex.Pattern;

@CodeHistory(date = "2021/1/2", project = "webbiton", name = "JsonLayerStack")
@CodeHistory(date = "2021/12/28", project = "va", name = "JsonParser")
@CodeHistory(date = "2022/1/3", project = "infrastructure", name = "JsonParser")
@CodeHistory(date = "2022/6/9", project = "Ultramarine", name = "JsonSerializer")
@CodeHistory(date = "2022/6/10", project = "Ultramarine", name = "JsonConverter")
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
    final HashMap<String, JsonType> reflectCache = new HashMap<>();

    public void logReflectCache() {
        logger.debug("reflectCache.size = " + reflectCache.size());
        for (Map.Entry<String, JsonType> entry : reflectCache.entrySet()) {
            logger.debug("{} = {}", entry.getKey(), entry.getValue());
        }
    }

    @NotNull
    public String reflectCacheToString() {
        return reflectCache.toString();
    }

    static final Module MODULE = JsonContext.class.getModule();

    @NotNull
    JsonType forClass(@NotNull Class<?> clazz) {
        final String className = clazz.getName();
        JsonType type = reflectCache.get(className);
        if (type != null) {
            return type;
        }
        if ((clazz.getModifiers() & Modifier.PUBLIC) != 0 && clazz.getModule().isExported(clazz.getPackageName(), MODULE)) {
            type = newForClass(clazz);
            reflectCache.put(className, type);
            type = type.collectType(clazz, this);
            reflectCache.put(className, type);
            return type;
        }
        final Class<?> superClass = clazz.getSuperclass();
        if (superClass != null) {
            type = forClass(superClass);
            if (type != JsonType.NOP) {
                return type;
            }
        }
        for (Class<?> superInterface : clazz.getInterfaces()) {
            type = forClass(superInterface);
            if (type != JsonType.NOP) {
                return type;
            }
        }
        return JsonType.NOP;
    }
    
    private void put(@NotNull Class<?> clazz, @NotNull JsonType type) {
        reflectCache.put(clazz.getName(), type);
    }

    private void remove(@NotNull Class<?> clazz) {
        reflectCache.remove(clazz.getName());
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

    public void removeContainerObject(@NotNull Object object) {
        loopDetector.remove(object);
    }

    JsonContext(long flags) {
        super(flags);
    }

    public JsonContext() {
        super();
    }

    @NotNull
    public JsonContext loadBasic() {
        put(Object.class, JsonType.NOP);
        final long flags = this.flags & ~(JsonHint.UNSIGNED | JsonHint.DERIVED);
        put(boolean.class, new BooleanType(flags & ~JsonHint.NULLABLE));
        put(Boolean.class, new BooleanType(flags));
        put(byte.class, new I8Type(flags & ~JsonHint.NULLABLE));
        put(Byte.class, new I8Type(flags));
        put(int.class, new IntType(flags & ~JsonHint.NULLABLE));
        put(Integer.class, new IntType(flags));
        put(long.class, new LongType(flags & ~JsonHint.NULLABLE));
        put(Long.class, new LongType(flags));
        put(BigInteger.class, new BigIntegerType(flags));
        put(FastNumber.class, new FastNumberType(flags));
        put(String.class, new StringType(flags));
        return this;
    }

    @NotNull
    public JsonContext loadOptimized() {
        final long flags = this.flags & ~(JsonHint.UNSIGNED | JsonHint.DERIVED);
        put(int[].class, new IntArrayType(flags, (IntType) reflectCache.get(int.class.getName())));
        return this;
    }

    @NotNull
    public JsonContext unloadOptimized() {
        remove(int[].class);
        return this;
    }

    @NotNull
    JsonContext loadProvided(@NotNull String resource) {
        final Pattern continuousWhitespace = Pattern.compile("\\s+");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(JsonContext.class.getResourceAsStream(resource), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] segments = continuousWhitespace.split(line);
                int count = segments.length;
                if (count < 2) {
                    continue;
                }
                ProvidedReadOnlyType provided = new ProvidedReadOnlyType(segments[0]);
                for (int index = 1; index < count; index++) {
                    reflectCache.put(segments[index], provided);
                }
            }
        } catch (Exception e) {
            getLogger().error("{}", resource, e);
        }
        return this;
    }

    @NotNull
    public JsonContext loadProvidedBase() {
        return loadProvided("/mujica/json/provided/package.txt");
    }

    @NotNull
    public JsonContext loadProvidedXml() {
        return loadProvided("/mujica/json/provided/xml/package.txt");
    }

    @NotNull
    public JsonContext loadProvidedNetty() {
        return loadProvided("/mujica/json/provided/netty/package.txt");
    }

    @NotNull
    public JsonContext loadProvidedDesktop() {
        return loadProvided("/mujica/json/provided/desktop/package.txt");
    }

    @NotNull
    public JsonContext loadProvidedManagement() {
        return loadProvided("/mujica/json/provided/management/package.txt");
    }

    public void parse(@NotNull JsonSyncReader reader, @NotNull Object root) {
        final ParserStack stack = new ParserStack(new ImmutableFrame(this, root));
        try {
            reader.read(stack);
        } finally {
            stack.close();
        }
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
