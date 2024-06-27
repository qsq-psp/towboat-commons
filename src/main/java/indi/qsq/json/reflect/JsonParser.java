package indi.um.json.reflect;

import indi.um.json.api.JsonConsumer;
import indi.um.json.api.ReflectOperations;
import indi.um.json.entity.JsonConstant;
import indi.um.json.io.JsonCharSequenceReader;
import indi.um.json.io.SyncReader;
import indi.um.util.ds.TruncateList;
import indi.um.util.reflect.ArtificialParameterizedType;
import indi.um.util.reflect.ClassUtility;
import indi.um.util.text.CommandLine;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created in webbiton on 2021/1/2, named JsonLayerStack.
 * Recreated in va on 2021/12/28, named JsonParser.
 * Recreated in infrastructure on 2022/1/3.
 * Recreated on 2022/7/12.
 */
public class JsonParser extends JsonConverter implements JsonConsumer {

    protected ConverterFrame top;

    protected final TruncateList<ConverterFrame> stack = new TruncateList<>();

    protected SyncReader reader;

    public JsonParser() {
        super();
    }

    @Override
    public int operation() {
        return ReflectOperations.JSON_PARSE;
    }

    protected Object get() {
        return top.finish(this);
    }

    public Object parse(SyncReader reader, Object root) {
        return frameParse(reader, new BottomFrame(root));
    }

    public Object parse(CharSequence string, Object root) {
        return parse(new JsonCharSequenceReader(string), root);
    }

    public Object parse(CharSequence string) {
        return parse(string, null);
    }

    public Object parseOrDereference(String string) {
        try {
            return parse(string);
        } catch (RuntimeException e) {
            return dereference(string);
        }
    }

    public Object parse(SyncReader reader, Type rootType) {
        return frameParse(reader, JsonTypeBuilder.get(rootType).objectFrame(null));
    }

    public Object parse(SyncReader reader, Object root, Type rootType) {
        return frameParse(reader, JsonTypeBuilder.get(rootType).objectFrame(root));
    }

    public Object parse(CharSequence string, Object root, Type rootType) {
        return parse(new JsonCharSequenceReader(string), root, rootType);
    }

    public Object parse(CommandLine commandLine, Type rootType) {
        final JsonType jsonType = JsonTypeBuilder.get(rootType);
        if (jsonType instanceof JsonObjectType) {
            Object object = ((JsonObjectType) jsonType).build.invoke(null, this);
            if (object instanceof JsonConstant) {
                return null;
            }
            ReflectClass.get(object.getClass()).parseCommandLine(object, commandLine, this);
            return object;
        } else if (commandLine.indexedArgumentLength() == 1) {
            return jsonType.parseString(commandLine.indexedArgument(0), this);
        } else {
            return null;
        }
    }

    public Object parse(ResultSet resultSet, Class<?> elementClass) {
        return parse(resultSetReader(resultSet, elementClass), new ArtificialParameterizedType(elementClass, ArrayList.class));
    }

    public <T> void parse(ResultSet resultSet, List<T> list, Class<T> elementClass) {
        parse(resultSetReader(resultSet, elementClass), list, new ArtificialParameterizedType(elementClass, List.class));
    }

    private Object frameParse(SyncReader reader, ConverterFrame rootFrame) {
        stack.clear();
        this.top = rootFrame;
        this.reader = reader;
        reader.read(this);
        return get();
    }

    /**
     * Created on 2022/11/3.
     */

    public Object empty(Type rootType) {
        return empty(rootType, true);
    }

    public Object empty(Type rootType, boolean isObject) {
        final ConverterFrame frame = JsonTypeBuilder.get(rootType).objectFrame(null);
        final ConverterFrame next = frame.openValue(isObject, this);
        frame.closeValue(next.finish(this), isObject, this);
        return frame.finish(this);
    }

    public static Object dereference(String string) {
        return dereference(string, Object.class);
    }

    private static final int EXPECTED_MODIFIERS = Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL;

    @SuppressWarnings("unchecked")
    public static <T> T dereference(Class<?> clazz, Class<T> filter) {
        try {
            try {
                Field field = clazz.getDeclaredField("INSTANCE");
                if ((field.getModifiers() & EXPECTED_MODIFIERS) == EXPECTED_MODIFIERS && filter.isAssignableFrom(field.getType())) {
                    return (T) field.get(null);
                } else {
                    throw new NoSuchFieldException();
                }
            } catch (NoSuchFieldException e) {
                return (T) clazz.getConstructor().newInstance();
            }
        } catch (ReflectiveOperationException e) {
            if (filter.isAssignableFrom(Class.class)) {
                return (T) clazz;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T dereference(Class<?> clazz, String fieldName, Class<T> filter) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            if ((field.getModifiers() & EXPECTED_MODIFIERS) == EXPECTED_MODIFIERS) {
                Object object = field.get(null);
                if (filter.isInstance(object)) {
                    return (T) object;
                }
            } else {
                throw new NoSuchFieldException();
            }
        } catch (ReflectiveOperationException ignore) { }
        return null;
    }

    public static <T> T dereference(String string, Class<T> filter) {
        try {
            return dereference(Class.forName(string), filter);
        } catch (ClassNotFoundException e) {
            int div = string.lastIndexOf('.');
            if (0 < div && div + 1 < string.length()) {
                try {
                    return dereference(Class.forName(string.substring(0, div)), string.substring(div + 1), filter);
                } catch (ClassNotFoundException ignore) { }
            }
        }
        return null;
    }

    @Override
    public void openArray() {
        final ConverterFrame next = top.openValue(false, this);
        stack.add(top);
        top = next;
    }

    @Override
    public void closeArray() {
        final ConverterFrame previous = stack.removeLast();
        previous.closeValue(top.finish(this), false, this);
        top = previous;
    }

    @Override
    public void openObject() {
        final ConverterFrame next = top.openValue(true, this);
        stack.add(top);
        top = next;
    }

    @Override
    public void closeObject() {
        final ConverterFrame previous = stack.removeLast();
        previous.closeValue(top.finish(this), true, this);
        top = previous;
    }

    @Override
    public void key(@NotNull String key) {
        top.key(key, this);
    }

    @Override
    public void jsonValue(@NotNull CharSequence json) {
        throw new IllegalStateException();
    }

    @Override
    public void nullValue() {
        top.nullValue(this);
    }

    @Override
    public void booleanValue(boolean value) {
        top.booleanValue(value, this);
    }

    @Override
    public void numberValue(long value) {
        top.numberValue(value, this);
    }

    @Override
    public void numberValue(double value) {
        top.numberValue(value, this);
    }

    @Override
    public void stringValue(@NotNull String value) {
        top.stringValue(value, this);
    }

    public void stringify(StringBuilder sb) {
        super.stringify(sb);
        sb.append(", top = ").append(ClassUtility.nearlyFull(top));
        sb.append(", stack = ").append(stack.size());
        if (reader != null) {
            sb.append(", near ");
            reader.stringifyNeighbors(sb);
        }
    }
}
