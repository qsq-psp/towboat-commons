package indi.qsq.json.reflect;

import indi.qsq.json.api.*;
import indi.qsq.json.io.IndexedColumn;
import indi.qsq.json.io.SyncReader;
import indi.qsq.json.io.RecursiveReader;
import indi.qsq.util.random.RandomContext;
import indi.qsq.util.text.CommandLine;
import indi.qsq.util.text.Quote;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.util.*;

/**
 * Created in va on 2021/12/24, named JsonClass.
 * Recreated in infrastructure on 2021/12/30, named ReflectedClass.
 * Recreated on 2022/6/4, named ReflectedClass.
 * Renamed on 2022/7/13.
 *
 * The base class is empty and contains no mapping, ListBased contains small number of mappings, MapBased contains large number of mappings
 */
class ReflectClass extends ClosureConfig {

    private static final long serialVersionUID = 0x09C112151B161872L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectClass.class);

    @Nullable
    ValueSerializer<Object> serializer;

    ReflectClass() {
        super();
    }

    @Override
    protected ReflectClass load(Class<?> clazz) {
        return this;
    }

    @SuppressWarnings("unchecked")
    void collectSerializer(String reference) {
        final ValueSerializer<Object> object = JsonParser.dereference(reference, ValueSerializer.class);
        if (object != null) {
            serializer = object;
        } else {
            LOGGER.warn("Resolving serializer {}", reference);
        }
    }

    void collect(Class<?> clazz) {
        collectConfig(clazz);
        final String[] names = collectOrderConfig(clazz);
        if (names != null) {
            for (String name : names) {
                if (name.isEmpty()) {
                    LOGGER.debug("Ignore empty name in {}", clazz);
                }
                addMapping(new Mapping(name));
            }
            if (anyClassConfig(FLAG_COLLECT_FIELDS)) {
                for (Field field : clazz.getDeclaredFields()) {
                    if (acceptField(field.getModifiers())) {
                        completeField(field);
                    }
                }
            }
            if (anyClassConfig(FLAG_COLLECT_SETTERS | FLAG_COLLECT_GETTERS)) {
                for (Method method : clazz.getDeclaredMethods()) {
                    if (acceptMethod(method.getModifiers())) {
                        if (acceptSetter(method)) {
                            completeSetter(method);
                        }
                        if (acceptGetter(method)) {
                            completeGetter(method);
                        }
                    }
                }
            }
        } else {
            //System.out.println("LoadedClass.collect " + clazz);
            if (anyClassConfig(FLAG_COLLECT_FIELDS)) {
                for (Field field : clazz.getDeclaredFields()) {
                    //System.out.println("LoadedClass.collect " + field);
                    if (acceptField(field.getModifiers())) {
                        collectField(field);
                    }
                }
            }
            if (anyClassConfig(FLAG_COLLECT_SETTERS | FLAG_COLLECT_GETTERS)) {
                for (Method method : clazz.getDeclaredMethods()) {
                    if (acceptMethod(method.getModifiers())) {
                        if (acceptSetter(method)) {
                            collectSetter(method);
                        }
                        if (acceptGetter(method)) {
                            collectGetter(method);
                        }
                    }
                }
            }
        }
        if (anyClassConfig(FLAG_SUPER_CLASS)) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && superClass != Object.class) {
                get(superClass).mergeTo(this, superClass, names == null);
            }
        }
        if (anyClassConfig(FLAG_SUPER_INTERFACE)) {
            for (Class<?> superInterface : clazz.getInterfaces()) {
                get(superInterface).mergeTo(this, superInterface, names == null);
            }
        }
        MethodHandles.Lookup lookup = null;
        if (anyClassConfig(FLAG_USE_METHOD_HANDLE)) {
            lookup = MethodHandles.lookup();
        }
        for (Mapping mapping : mappings()) {
            mapping.finish(this, lookup);
        }
    }

    void collectConfig(@NotNull AnnotatedElement element) {
        final SerializeToUndefined serializeToUndefined = element.getAnnotation(SerializeToUndefined.class);
        if (serializeToUndefined != null) {
            int config = serializeToUndefined.value();
            serializeConfig = (serializeConfig & ~(SerializeFrom.MASK << UNDEFINED_SHIFT)) | ((config & SerializeFrom.MASK) << UNDEFINED_SHIFT);
        }
        final SerializeToNull serializeToNull = element.getAnnotation(SerializeToNull.class);
        if (serializeToNull != null) {
            int config = serializeToNull.value();
            serializeConfig = (serializeConfig & ~SerializeFrom.MASK) | (config & SerializeFrom.MASK);
        }
        final ParseHint parseHint = element.getAnnotation(ParseHint.class);
        if (parseHint != null) {
            parseConfig = parseHint.value();
        }
        final ReflectionLog reflectionLog = element.getAnnotation(ReflectionLog.class);
        if (reflectionLog != null) {
            int when = reflectionLog.when();
            closureConfig &= ~(ReflectOperations.ALL << SHIFT_LOGGER);
            closureConfig |= (when & ReflectOperations.ALL) << SHIFT_LOGGER;
        }
    }

    @Nullable
    String[] collectOrderConfig(@NotNull AnnotatedElement element) {
        FieldOrder fieldOrder = element.getAnnotation(FieldOrder.class);
        if (fieldOrder != null) {
            closureConfig &= ~(ReflectOperations.ALL << SHIFT_ORDERED);
            closureConfig |= (fieldOrder.ordered() & ReflectOperations.ALL) << SHIFT_ORDERED;
            closureConfig &= ~(ReflectOperations.ALL << SHIFT_RANDOM);
            closureConfig |= (fieldOrder.random() & ReflectOperations.ALL) << SHIFT_RANDOM;
            String[] names = fieldOrder.value();
            if (names.length > 0) {
                return names;
            }
        }
        return null;
    }

    boolean acceptField(int modifiers) {
        if (anyClassConfig(FLAG_COLLECT_NON_PUBLIC)) {
            return ((Modifier.STATIC | Modifier.TRANSIENT) & modifiers) == 0;
        } else {
            return ((Modifier.PUBLIC | Modifier.PRIVATE | Modifier.PROTECTED | Modifier.STATIC | Modifier.TRANSIENT) & modifiers) == Modifier.PUBLIC;
        }
    }

    void completeField(Field field) {
        final String name = getName(field);
        final Mapping mapping = getMapping(name);
        if (mapping != null) {
            mapping.collectField(field, this);
        }
    }

    void collectField(Field field) {
        final String name = getName(field);
        if (name.isEmpty()) {
            if (anyClassConfig(ReflectOperations.REFLECT_COLLECT << SHIFT_LOGGER)) {
                LOGGER.warn("Ignore field {} with empty name", field);
            }
            return;
        }
        if (getMapping(name) != null) {
            if (anyClassConfig(ReflectOperations.REFLECT_COLLECT << SHIFT_LOGGER)) {
                LOGGER.warn("Ignore field {} with duplicate name {}", field, Quote.DEFAULT.apply(name));
            }
            return;
        }
        final Mapping mapping = new Mapping(name);
        mapping.collectField(field, this);
        addMapping(mapping);
    }

    String getName(Field field) {
        final JsonName name = field.getAnnotation(JsonName.class);
        if (name != null) {
            return name.value();
        }
        return field.getName();
    }

    boolean acceptMethod(int modifiers) {
        if (anyClassConfig(FLAG_COLLECT_NON_PUBLIC)) {
            return (Modifier.STATIC & modifiers) == 0;
        } else {
            return ((Modifier.PRIVATE | Modifier.PROTECTED | Modifier.STATIC) & modifiers) == 0 && (Modifier.PUBLIC & modifiers) != 0;
        }
    }

    boolean acceptSetter(@NotNull Method method) {
        return anyClassConfig(FLAG_COLLECT_SETTERS) && method.getParameterCount() == 1 && method.getReturnType() == void.class;
    }

    boolean acceptGetter(@NotNull Method method) {
        return anyClassConfig(FLAG_COLLECT_GETTERS) && method.getParameterCount() == 0 && method.getReturnType() != void.class;
    }

    void completeSetter(@NotNull Method method) {
        final JsonName nameAnnotation = method.getAnnotation(JsonName.class);
        String name;
        if (nameAnnotation != null) {
            name = nameAnnotation.value();
        } else {
            name = removePrefix(method.getName(), "set");
            if (name == null) {
                return;
            }
        }
        final Mapping mapping = getMapping(name);
        if (mapping != null) {
            mapping.collectSetter(method, this);
        }
    }

    void completeGetter(@NotNull Method method) {
        final JsonName nameAnnotation = method.getAnnotation(JsonName.class);
        String name;
        if (nameAnnotation != null) {
            name = nameAnnotation.value();
        } else {
            name = removePrefix(method.getName(), "get");
            if (name == null) {
                name = removePrefix(method.getName(), "is");
                if (name == null) {
                    return;
                }
            } else if (rejectFromGetter(name)) {
                return;
            }
        }
        final Mapping mapping = getMapping(name);
        if (mapping != null) {
            mapping.collectGetter(method, this);
        }
    }

    void collectSetter(@NotNull Method method) {
        final JsonName nameAnnotation = method.getAnnotation(JsonName.class);
        String name;
        if (nameAnnotation != null) {
            name = nameAnnotation.value();
        } else {
            name = removePrefix(method.getName(), "set");
            if (name == null) {
                return;
            }
        }
        if (name.isEmpty()) {
            if (anyClassConfig(ReflectOperations.REFLECT_COLLECT << SHIFT_LOGGER)) {
                LOGGER.warn("Ignore setter method {} with empty name", method);
            }
            return;
        }
        getNonNullMapping(name).collectSetter(method, this); // methods override fields
    }

    void collectGetter(@NotNull Method method) {
        final JsonName nameAnnotation = method.getAnnotation(JsonName.class);
        String name;
        if (nameAnnotation != null) {
            name = nameAnnotation.value();
        } else {
            name = removePrefix(method.getName(), "get");
            if (name == null) {
                name = removePrefix(method.getName(), "is");
                if (name == null) {
                    return;
                }
            } else if (rejectFromGetter(name)) {
                return;
            }
        }
        if (name.isEmpty()) {
            if (anyClassConfig(ReflectOperations.REFLECT_COLLECT << SHIFT_LOGGER)) {
                LOGGER.warn("Ignore getter method {} with empty name", method);
            }
            return;
        }
        getNonNullMapping(name).collectGetter(method, this); // methods override fields
    }

    @Nullable
    String removePrefix(@NotNull String string, @NotNull String prefix) {
        if (string.startsWith(prefix)) {
            int prefixLength = prefix.length();
            if (prefixLength < string.length() && Character.isUpperCase(string.charAt(prefixLength))) {
                return Character.toLowerCase(string.charAt(prefixLength)) + string.substring(prefixLength + 1);
            }
        }
        return null;
    }

    boolean rejectFromGetter(@NotNull String name) {
        return name.equals("class") || name.startsWith("as"); // getClass(), getAsInt(), getAsDouble(), etc
    }

    void mergeTo(ReflectClass that, Class<?> thisClass, boolean addNew) {
        for (Mapping mapping : mappings()) {
            that.mergeFrom(mapping, thisClass, addNew);
        }
    }

    void mergeFrom(Mapping superMapping, Class<?> superClass, boolean addNew) {
        final Mapping thisMapping = getMapping(superMapping.getName());
        if (thisMapping == null) {
            if (addNew) {
                addMapping(superMapping);
            }
        } else {
            thisMapping.mergeFrom(superMapping, superClass);
        }
    }

    @NotNull
    protected Collection<Mapping> mappings() {
        return List.of();
    }

    protected ReflectClass simplify() {
        return this;
    }

    protected void addMapping(Mapping mapping) {
        // pass
    }

    protected Mapping getMapping(String name) {
        return null; // pass
    }

    Mapping getNonNullMapping(String name) {
        Mapping mapping = getMapping(name);
        if (mapping == null) {
            mapping = new Mapping(name);
            addMapping(mapping);
        }
        return mapping;
    }

    void serialize(String key, Object value, JsonConsumer jc, ConversionConfig cc, JsonSerializer js) {
        if (serializer != null) {
            serializer.serialize(key, value, jc, cc, js);
            return;
        }
        if (js.add(value)) {
            try {
                final Collection<Mapping> mappings = mappings();
                if (mappings.isEmpty()) {
                    if (cc.anySerializeConfig(SerializeFrom.EMPTY_OBJECT, true)) {
                        if (js.logEnabled()) {
                            LOGGER.debug("Serialize empty object to undefined in {}", js);
                        }
                        return;
                    }
                    if (cc.anySerializeConfig(SerializeFrom.EMPTY_OBJECT, false)) {
                        if (js.logEnabled()) {
                            LOGGER.debug("Serialize empty object to undefined in {}", js);
                        }
                        jc.optionalKey(key);
                        jc.nullValue();
                        return;
                    }
                }
                jc.optionalKey(key);
                jc.openObject();
                serialize(mappings, value, jc, js);
                jc.closeObject();
            } finally {
                js.remove(value);
            }
        } else {
            // System.out.println("ReflectClass.serialize " + js.traceToString());
            if (cc.anySerializeConfig(SerializeFrom.CYCLIC_OBJECT, true)) {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize cyclic object to undefined in {}", js);
                }
                return;
            }
            if (cc.anySerializeConfig(SerializeFrom.CYCLIC_OBJECT, false)) {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize cyclic object to undefined in {}", js);
                }
                jc.optionalKey(key);
                jc.nullValue();
                return;
            }
            throw new IllegalArgumentException("Cyclic object");
        }
    }

    void serialize(Collection<Mapping> mappings, Object value, JsonConsumer jc, JsonSerializer js) {
        if (anyClassConfig(js.operation() << SHIFT_RANDOM)) {
            ArrayList<Mapping> list = new ArrayList<>(mappings);
            (new RandomContext()).shuffleList(list);
            mappings = list;
        }
        for (Mapping mapping : mappings) {
            js.thisObject = value;
            mapping.serialize(value, jc, js);
        }
    }

    ConverterFrame frame(Object object, JsonConverter jv) {
        jv.thisObject = object;
        if (anyClassConfig(jv.operation() << SHIFT_ORDERED)) {
            return orderedFrame(object);
        }
        if (anyClassConfig(jv.operation() << SHIFT_RANDOM)) {
            return randomFrame(object);
        }
        return new SelectorObjectFrame(object);
    }

    protected ConverterFrame orderedFrame(Object object) {
        return ConverterFrame.INSTANCE;
    }

    protected ConverterFrame randomFrame(Object object) {
        return ConverterFrame.INSTANCE;
    }

    void parseCommandLine(Object object, CommandLine commandLine, JsonParser jp) {
        for (Mapping mapping : mappings()) {
            CommandLineArgument commandLineArgument = mapping.getCommandLineArgument();
            if (commandLineArgument != null) {
                String key = commandLineArgument.key();
                if (key.isEmpty()) {
                    key = mapping.getName();
                }
                String value = commandLine.keyedArgument(key);
                if (value == null) {
                    value = commandLine.indexedArgument(commandLineArgument.index());
                }
                if (value != null) {
                    mapping.write.invoke(object, mapping.type.parseString(value, jp));
                }
            }
        }
    }

    ArrayList<IndexedColumn> indexedColumnList(ResultSet resultSet) {
        final ArrayList<IndexedColumn> list = new ArrayList<>();
        for (Mapping mapping : mappings()) {
            IndexedColumn item = mapping.indexedColumn(resultSet);
            if (item != null) {
                list.add(item);
            }
        }
        return list;
    }

    /**
     * Created in webbiton on 2020/12/23, named JsonObjectLayer.
     * Recreated in infrastructure on 2022/1/3, named ReflectedClass$ObjectFrame.
     * Created on 2022/7/20.
     */
    class SelectorObjectFrame extends BottomFrame {

        Mapping selected;

        SelectorObjectFrame(Object object) {
            super(object);
        }

        @Override
        protected void key(String key, JsonParser jp) {
            selected = getMapping(key);
        }

        @Override
        protected ConverterFrame openValue(boolean isObject, JsonConverter jv) {
            if (selected != null) {
                jv.thisObject = object;
                return selected.frame(object, isObject, jv);
            } else {
                return INSTANCE;
            }
        }

        @Override
        protected void closeValue(Object value, boolean isObject, JsonConverter jv) {
            if (selected == null) {
                return;
            }
            selected.write.invoke(object, value);
            selected = null;
        }

        @Override
        protected void nullValue(JsonConverter jv) {
            if (selected == null) {
                return;
            }
            selected.write.invoke(object, selected.type.parseNull(jv));
            selected = null;
        }

        @Override
        protected void booleanValue(boolean value, JsonConverter jv) {
            if (selected == null) {
                return;
            }
            selected.write.invoke(object, selected.type.parseBoolean(value, jv));
            selected = null;
        }

        @Override
        protected void numberValue(long value, JsonConverter jv) {
            if (selected == null) {
                return;
            }
            selected.write.invoke(object, selected.type.parseNumber(value, jv));
            selected = null;
        }

        @Override
        protected void numberValue(double value, JsonConverter jv) {
            if (selected == null) {
                return;
            }
            selected.write.invoke(object, selected.type.parseNumber(value, jv));
            selected = null;
        }

        @Override
        protected void stringValue(String value, JsonConverter jv) {
            if (selected == null) {
                return;
            }
            selected.write.invoke(object, selected.type.parseString(value, jv));
            selected = null;
        }

        @Override
        public String toString() {
            return "ReflectClass.SelectorObjectFrame[" + object + "]";
        }
    }

    static class ListBased extends ReflectClass {

        private static final long serialVersionUID = 0x2078CB85BEA19A3BL;

        final ArrayList<Mapping> list;

        ListBased(Collection<Mapping> mappings) {
            super();
            list = new ArrayList<>(mappings);
        }

        @NotNull
        @Override
        protected Collection<Mapping> mappings() {
            return list;
        }

        @Override
        protected void addMapping(Mapping mapping) {
            list.add(mapping);
        }

        @Override
        protected Mapping getMapping(String name) {
            for (Mapping mapping : list) {
                if (mapping.getName().equals(name)) {
                    return mapping;
                }
            }
            return null;
        }

        @Override
        protected ReflectClass simplify() {
            if (list.isEmpty()) {
                ReflectClass simple = new ReflectClass();
                simple.setClosureConfig(this);
                simple.serializer = this.serializer;
                return simple;
            } else {
                list.trimToSize();
            }
            return this;
        }

        @Override
        protected ConverterFrame orderedFrame(Object object) {
            if (list.size() > 1) {
                return new ListOrderedObjectFrame(object);
            } else {
                return new SelectorObjectFrame(object);
            }
        }

        /**
         * Created in infrastructure on 2022/1/4, named ReflectedClass$OrderedObjectFrame.
         * Created on 2022/7/20.
         */
        class ListOrderedObjectFrame extends SelectorObjectFrame {

            final int[] marks = new int[list.size()];

            ListOrderedObjectFrame(Object object) {
                super(object);
            }

            @Override
            protected Object finish(JsonParser jp) {
                final SyncReader reader = jp.reader;
                if (!(reader instanceof RecursiveReader)) {
                    LOGGER.warn("Ordered read not supported by {} in {}", reader, jp);
                    return super.finish(jp);
                }
                final RecursiveReader recursiveReader = (RecursiveReader) reader;
                final int endMark = recursiveReader.getPosition();
                final int length = marks.length;
                for (int index = 0; index < length; index++) {
                    if (marks[index] == 0) {
                        continue;
                    }
                    selected = list.get(index);
                    recursiveReader.setPosition(marks[index]);
                    recursiveReader.read(jp);
                }
                recursiveReader.setPosition(endMark);
                return object;
            }

            @Override
            protected void key(String key, JsonParser jp) {
                final SyncReader reader = jp.reader;
                if (!(reader instanceof RecursiveReader)) {
                    LOGGER.warn("Ordered read not supported by {} in {}", reader, jp);
                    super.key(key, jp);
                    return;
                }
                final RecursiveReader recursiveReader = (RecursiveReader) reader;
                final int length = marks.length;
                for (int index = 0; index < length; index++) {
                    if (list.get(index).getName().equals(key)) {
                        int newMark = recursiveReader.getPosition();
                        if (marks[index] != 0 && jp.logEnabled()) {
                            LOGGER.debug("Override order mark {} to {} at index {} in {}", marks[index], newMark, index, jp);
                        }
                        marks[index] = newMark;
                        break;
                    }
                }
                recursiveReader.skip(this);
            }

            @Override
            public String toString() {
                return "ReflectClass.ListBased.ListOrderedObjectFrame[" + object + "]";
            }
        }

        @Override
        protected ConverterFrame randomFrame(Object object) {
            if (list.size() > 1) {
                ListBased copy = new ListBased(list);
                (new RandomContext()).shuffleList(copy.list);
                return copy.orderedFrame(object);
            } else {
                return new SelectorObjectFrame(object);
            }
        }
    }

    static class MapBased extends ReflectClass {

        private static final long serialVersionUID = 0xF02A125F0B001A2BL;

        final LinkedHashMap<String, Mapping> map = new LinkedHashMap<>();

        @NotNull
        @Override
        protected Collection<Mapping> mappings() {
            return map.values();
        }

        @Override
        protected void addMapping(Mapping mapping) {
            map.put(mapping.getName(), mapping);
        }

        @Override
        protected Mapping getMapping(String name) {
            return map.get(name);
        }

        @Override
        protected ReflectClass simplify() {
            if (map.isEmpty()) {
                ReflectClass simple = new ReflectClass();
                simple.setClosureConfig(this);
                simple.serializer = this.serializer;
                return simple;
            } else if (map.size() <= 10) {
                ListBased simple = new ListBased(mappings());
                // simple.list.trimToSize();
                simple.setClosureConfig(this);
                simple.serializer = this.serializer;
                return simple;
            }
            return this;
        }

        @Override
        protected ConverterFrame orderedFrame(Object object) {
            if (map.size() > 1) {
                return new MapOrderedObjectFrame(object);
            } else {
                return new SelectorObjectFrame(object);
            }
        }

        /**
         * Created on 2022/8/29.
         */
        class MapOrderedObjectFrame extends SelectorObjectFrame {

            final HashMap<Mapping, Integer> marks = new HashMap<>();

            MapOrderedObjectFrame(Object object) {
                super(object);
            }

            @Override
            protected Object finish(JsonParser jp) {
                final SyncReader reader = jp.reader;
                if (!(reader instanceof RecursiveReader)) {
                    LOGGER.warn("Ordered read not supported by {} in {}", reader, jp);
                    return super.finish(jp);
                }
                final RecursiveReader recursiveReader = (RecursiveReader) reader;
                final int endMark = recursiveReader.getPosition();
                for (Mapping mapping : map.values()) {
                    Integer mark = marks.get(mapping);
                    if (mark == null) {
                        continue;
                    }
                    selected = mapping;
                    recursiveReader.setPosition(mark);
                    recursiveReader.read(jp);
                }
                recursiveReader.setPosition(endMark);
                return object;
            }

            @Override
            protected void key(String key, JsonParser jp) {
                final Mapping mapping = map.get(key);
                if (mapping == null) {
                    return;
                }
                final SyncReader reader = jp.reader;
                if (!(reader instanceof RecursiveReader)) {
                    LOGGER.warn("Ordered read not supported by {} in {}", reader, jp);
                    super.key(key, jp);
                    return;
                }
                final RecursiveReader recursiveReader = (RecursiveReader) reader;
                int newMark = recursiveReader.getPosition();
                if (jp.logEnabled() && marks.containsKey(mapping)) {
                    LOGGER.debug("Override order mark {} to {} at key {} in {}", marks.get(mapping), newMark, mapping.getName(), jp);
                }
                marks.put(mapping, newMark);
                recursiveReader.skip(this);
            }

            @Override
            public String toString() {
                return "ReflectClass.MapBased.MapOrderedObjectFrame[" + object + "]";
            }
        }

        @Override
        protected ConverterFrame randomFrame(Object object) {
            if (map.size() > 1) {
                ListBased copy = new ListBased(map.values());
                (new RandomContext()).shuffleList(copy.list);
                return copy.orderedFrame(object);
            } else {
                return new SelectorObjectFrame(object);
            }
        }
    }

    @Override
    public void toJsonEntries(@NotNull JsonConsumer jc) {
        super.toJsonEntries(jc);
        jc.key("mappings");
        jc.structuresValue(mappings());
        if (serializer != null) {
            jc.key("serializer");
            jc.stringValue(serializer.getClass().getName());
        }
    }

    static final HashMap<Class<?>, ReflectClass> MAP = new HashMap<>();

    static ReflectClass get(@NotNull Class<?> clazz) {
        ReflectClass value;
        synchronized (MAP) {
            value = MAP.get(clazz);
        }
        if (value == null) {
            value = ClosurePreset.create(clazz);
            ReflectClass other;
            synchronized (MAP) {
                other = MAP.putIfAbsent(clazz, value);
            }
            if (other != null) {
                if (other.anyClassConfig(ReflectOperations.REFLECT_COLLECT << SHIFT_LOGGER)) {
                    LOGGER.debug("Concurrent override failure {}", clazz);
                }
            }
        }
        return value;
    }

    static ReflectClass query(@NotNull Class<?> clazz) {
        synchronized (MAP) {
            return MAP.get(clazz);
        }
    }

    static ArrayList<Class<?>> queryClasses() {
        synchronized (MAP) {
            return new ArrayList<>(MAP.keySet());
        }
    }

    static void queryAll(@NotNull JsonConsumer jc) {
        jc.openObject();
        final ArrayList<Map.Entry<Class<?>, ReflectClass>> list;
        synchronized (MAP) {
            list = new ArrayList<>(MAP.entrySet());
        }
        for (Map.Entry<Class<?>, ReflectClass> entry : list) {
            jc.key(entry.getKey().getName());
            entry.getValue().toJson(jc); // may contain calls to modify MAP
        }
        jc.closeObject();
    }
}
