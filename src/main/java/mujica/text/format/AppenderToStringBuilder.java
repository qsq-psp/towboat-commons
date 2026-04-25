package mujica.text.format;

import mujica.ds.generic.set.CollectionConstant;
import mujica.ds.of_char.sequence.TowboatCharSequence;
import mujica.reflect.modifier.AccessStructure;
import mujica.reflect.modifier.CodeHistory;
import mujica.text.number.DefaultNumberAppender;
import mujica.text.number.IntegralAppender;
import mujica.text.number.MarkedNumberAppender;
import mujica.text.sanitizer.CharSequenceAppender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Function;

@CodeHistory(date = "2026/2/1", name = "UniversalStringifierUsingStringBuilder")
@CodeHistory(date = "2026/3/6")
public class AppenderToStringBuilder implements Function<Object, String>, BiConsumer<Object, StringBuilder> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppenderToStringBuilder.class);

    @Override
    @NotNull
    public String apply(@Nullable Object object) {
        final StringBuilder sb = new StringBuilder();
        accept(object, sb);
        return sb.toString();
    }

    @AccessStructure(online = false, local = true)
    @NotNull
    private final Map<Class<?>, MethodHandle> methods = new HashMap<>();

    private void invoke(@NotNull Object object, @NotNull Class<?> class0, @NotNull StringBuilder out) {
        Class<?> class1 = class0;
        do {
            MethodHandle method = methods.get(class1);
            if (method != null) {
                try {
                    method.invoke(object, out);
                } catch (Throwable e) {
                    LOGGER.warn("invoke {} {}", class0.getName(), class1.getName(), e);
                }
                return;
            }
            class1 = class1.getSuperclass();
        } while (class1 != null);
        LOGGER.warn("invoke {}", class0.getName());
    }

    private void invokePrimitive(@NotNull Object object, @NotNull Class<?> class0, @NotNull StringBuilder out) {
        MethodHandle method = methods.get(class0);
        if (method != null) {
            try {
                method.invoke(object, out);
            } catch (Throwable e) {
                LOGGER.warn("invokePrimitive {}", class0.getName(), e);
            }
            return;
        }
        method = methods.get(Object.class);
        if (method != null) {
            try {
                method.invoke(object, out);
            } catch (Throwable e) {
                LOGGER.warn("invokePrimitive {}", class0.getName(), e);
            }
            return;
        }
        LOGGER.warn("invokePrimitive {}", class0.getName());
    }

    @NotNull
    private String nullString = "null";

    @NotNull
    public AppenderToStringBuilder setNullString(@NotNull String nullString) {
        this.nullString = nullString;
        return this;
    }

    void acceptNullable(@Nullable Object object, @NotNull StringBuilder out) {
        if (object == null) {
            out.append(nullString);
        } else {
            invoke(object, object.getClass(), out);
        }
    }

    @NotNull
    private final AtomicInteger depth = new AtomicInteger();

    @NotNull
    private final IdentityHashMap<Object, CollectionConstant> loopDetector = new IdentityHashMap<>();

    @Override
    public void accept(@Nullable Object object, @NotNull StringBuilder out) {
        if (object == null) {
            out.append(nullString);
            return;
        }
        if (depth.compareAndSet(0, 1)) {
            try {
                invoke(object, object.getClass(), out);
                if (!depth.compareAndSet(1, 0)) {
                    throw new IllegalStateException("depth = " + depth.get());
                }
            } catch (Throwable e) {
                loopDetector.clear(); // clear is slow even if map is empty
                throw e;
            } finally {
                depth.set(0);
            }
        } else {
            throw new IllegalStateException("depth = " + depth.get());
        }
    }

    @CodeHistory(date = "2026/3/9")
    public class ArrayStyle {

        @NotNull
        AppenderToStringBuilder context() {
            return AppenderToStringBuilder.this;
        }

        @NotNull
        String left, right, itemSeparator, empty, loop;

        void acceptNotNull(@NotNull Object object, @NotNull StringBuilder out) {
            try {
                int length = Array.getLength(object); // throws IllegalArgumentException if not array
                if (length > 0) {
                    if (loopDetector.put(object, CollectionConstant.PRESENT) == null) {
                        depth.incrementAndGet();
                        out.append(left);
                        acceptNullable(Array.get(object, 0), out);
                        for (int index = 1; index < length; index++) {
                            out.append(itemSeparator);
                            acceptNullable(Array.get(object, index), out);
                        }
                        out.append(right);
                        depth.getAndDecrement();
                        loopDetector.remove(object);
                    } else {
                        out.append(loop);
                    }
                } else {
                    out.append(empty);
                }
            } catch (IllegalArgumentException e) {
                out.append(object.toString());
            }
        }

        @Nullable
        Class<?> getComponentType(@NotNull Class<?> clazz) {
            return clazz.getComponentType(); // array check included
        }

        void fastAcceptNotNull(@NotNull Object object, @NotNull StringBuilder out) {
            final Class<?> class0 = object.getClass();
            Class<?> class1 = class0.getComponentType();
            while (class1 != null) {
                MethodHandle method = methods.get(class1);
                if (method != null) {
                    try {
                        method.invoke(object, out);
                    } catch (Throwable e) {
                        LOGGER.warn("fast invoke {} {}", class0.getName(), class1.getName(), e);
                    }
                    return;
                }
                class1 = class1.getSuperclass();
            }
            LOGGER.warn("fast invoke {}", class0.getName());
        }

        void fastAcceptNotNull(@NotNull MethodHandle method, @NotNull Object object, @NotNull StringBuilder out) {
            int length = Array.getLength(object); // throws IllegalArgumentException if not array
            if (length > 0) {
                if (loopDetector.put(object, CollectionConstant.PRESENT) == null) {
                    depth.incrementAndGet();
                    out.append(left);
                    fastAcceptNullable(method, Array.get(object, 0), out);
                    for (int index = 1; index < length; index++) {
                        out.append(itemSeparator);
                        fastAcceptNullable(method, Array.get(object, index), out);
                    }
                    out.append(right);
                    depth.getAndDecrement();
                    loopDetector.remove(object);
                } else {
                    out.append(loop);
                }
            } else {
                out.append(empty);
            }
        }

        void fastAcceptNullable(@NotNull MethodHandle method, @Nullable Object object, @NotNull StringBuilder out) {
            if (object == null) {
                out.append(nullString);
            } else {
                try {
                    method.invoke(object, out);
                } catch (Throwable e) {
                    LOGGER.warn("fast invoke", e);
                }
            }
        }

        ArrayStyle() {
            super();
            left = "[";
            right = "]";
            itemSeparator = ", ";
            empty = "[]";
            loop = "loop";
        }

        @NotNull
        public ArrayStyle setLeft(@NotNull String left) {
            this.left = left;
            return this;
        }

        @NotNull
        public ArrayStyle setRight(@NotNull String right) {
            this.right = right;
            return this;
        }

        @NotNull
        public ArrayStyle setItemSeparator(@NotNull String itemSeparator) {
            this.itemSeparator = itemSeparator;
            return this;
        }

        @NotNull
        public ArrayStyle setEmpty(@NotNull String empty) {
            this.empty = empty;
            return this;
        }

        @NotNull
        public ArrayStyle setLoop(@NotNull String loop) {
            this.loop = loop;
            return this;
        }
    }

    @NotNull
    public ArrayStyle newArrayStyle() {
        return new ArrayStyle();
    }

    @CodeHistory(date = "2026/3/18")
    public class MatrixStyle extends ArrayStyle {

        @NotNull
        String rowSeparator;

        @Override
        void acceptNotNull(@NotNull Object object, @NotNull StringBuilder out) {
            try {
                int length = Array.getLength(object); // throws IllegalArgumentException if not array
                if (length > 0) {
                    if (loopDetector.put(object, CollectionConstant.PRESENT) == null) {
                        depth.incrementAndGet();
                        out.append(left);
                        matrixAcceptNullable(Array.get(object, 0), out);
                        for (int index = 1; index < length; index++) {
                            out.append(rowSeparator);
                            matrixAcceptNullable(Array.get(object, index), out);
                        }
                        out.append(right);
                        depth.getAndDecrement();
                        loopDetector.remove(object);
                    } else {
                        out.append(loop);
                    }
                } else {
                    out.append(empty);
                }
            } catch (IllegalArgumentException e) {
                out.append(object.toString());
            }
        }

        void matrixAcceptNullable(@Nullable Object object, @NotNull StringBuilder out) {
            if (object == null) {
                out.append(nullString);
                return;
            }
            try {
                int length = Array.getLength(object); // throws IllegalArgumentException if not array
                if (length > 0) {
                    if (loopDetector.put(object, CollectionConstant.PRESENT) == null) {
                        depth.incrementAndGet();
                        acceptNullable(Array.get(object, 0), out);
                        for (int index = 1; index < length; index++) {
                            out.append(itemSeparator);
                            acceptNullable(Array.get(object, index), out);
                        }
                        depth.getAndDecrement();
                        loopDetector.remove(object);
                    } else {
                        out.append(loop);
                    }
                }
            } catch (IllegalArgumentException e) {
                out.append(object.toString());
            }
        }

        @Nullable
        @Override
        Class<?> getComponentType(@NotNull Class<?> clazz) {
            clazz = clazz.getComponentType();
            if (clazz != null) {
                clazz = clazz.getComponentType();
            }
            return clazz;
        }

        MatrixStyle() {
            super();
            rowSeparator = "; ";
        }

        @NotNull
        public MatrixStyle setRowSeparator(@NotNull String rowSeparator) {
            this.rowSeparator = rowSeparator;
            return this;
        }
    }

    @NotNull
    public MatrixStyle newMatrixStyle() {
        return new MatrixStyle();
    }

    @CodeHistory(date = "2026/3/28")
    public class IndentStyle extends ArrayStyle {

        @NotNull
        String indent;

        IndentStyle() {
            super();
            final String lineSeparator = System.lineSeparator();
            left = "[" + lineSeparator;
            right = lineSeparator + "]";
            itemSeparator = "," + lineSeparator;
            indent = "  "; // two spaces
        }

        @NotNull
        public IndentStyle setIndent(@NotNull String indent) {
            this.indent = indent;
            return this;
        }
    }

    @NotNull
    public IndentStyle newIndentStyle() {
        return new IndentStyle();
    }

    private static void acceptBooleanTrueFalse(boolean value, @NotNull StringBuilder out) {
        out.append(value ? "True" : "False");
    }

    private static void acceptBooleanYesNo(boolean value, @NotNull StringBuilder out) {
        out.append(value ? "Yes" : "No");
    }

    private static void acceptBooleanOnOff(boolean value, @NotNull StringBuilder out) {
        out.append(value ? "On" : "Off");
    }

    private void acceptInt(@NotNull Number number, @NotNull StringBuilder out) {
        invokePrimitive(number.intValue(), int.class, out);
    }

    private void acceptLong(@NotNull Number number, @NotNull StringBuilder out) {
        invokePrimitive(number.longValue(), long.class, out);
    }

    private void acceptFloat(@NotNull Number number, @NotNull StringBuilder out) {
        invokePrimitive(number.floatValue(), float.class, out);
    }

    private void acceptDouble(@NotNull Number number, @NotNull StringBuilder out) {
        invokePrimitive(number.doubleValue(), double.class, out);
    }

    private static void acceptDate(@NotNull DateFormat format, @NotNull Date value, @NotNull StringBuilder out) {
        out.append(format.format(value));
    }

    private static void acceptCalendar(@NotNull DateFormat format, @NotNull Calendar value, @NotNull StringBuilder out) {
        acceptDate(format, value.getTime(), out);
    }

    private static void acceptTimestamp(@NotNull DateFormat format, long value, @NotNull StringBuilder out) {
        acceptDate(format, new Date(value), out);
    }

    private static void acceptObjectDefault(@NotNull Object object, @NotNull StringBuilder out) {
        out.append(object.getClass().getName()).append("@").append(System.identityHashCode(object));
    }

    @CodeHistory(date = "2026/3/6")
    public class Config {

        @NotNull
        final MethodHandles.Lookup lookup;

        Config(@NotNull MethodHandles.Lookup lookup) {
            super();
            this.lookup = lookup;
        }

        @NotNull
        public Config use(@NotNull ArrayStyle style, @NotNull Class<?>... classes) throws ReflectiveOperationException {
            if (AppenderToStringBuilder.this != style.context()) {
                throw new IllegalArgumentException("context");
            }
            MethodHandle method = lookup.findVirtual(ArrayStyle.class, "acceptNotNull", MethodType.methodType(void.class, Object.class, StringBuilder.class)).bindTo(style);
            for (Class<?> clazz : classes) {
                if (clazz.isInterface()) {
                    LOGGER.warn("{} is useless", clazz); // "interface xxx.Yyy is useless"
                }
                methods.put(clazz, method);
            }
            return this;
        }

        @NotNull
        public Config use(@NotNull IntegralAppender appender) throws ReflectiveOperationException {
            MethodHandle method;
            method = lookup.findVirtual(IntegralAppender.class, "acceptByte", MethodType.methodType(void.class, byte.class, StringBuilder.class)).bindTo(appender);
            methods.put(Byte.class, method);
            methods.put(byte.class, method);
            method = lookup.findVirtual(IntegralAppender.class, "acceptShort", MethodType.methodType(void.class, short.class, StringBuilder.class)).bindTo(appender);
            methods.put(Short.class, method);
            methods.put(short.class, method);
            method = lookup.findVirtual(IntegralAppender.class, "acceptChar", MethodType.methodType(void.class, char.class, StringBuilder.class)).bindTo(appender);
            methods.put(Character.class, method);
            methods.put(char.class, method);
            method = lookup.findVirtual(IntegralAppender.class, "acceptInt", MethodType.methodType(void.class, int.class, StringBuilder.class)).bindTo(appender);
            methods.put(Integer.class, method);
            methods.put(int.class, method);
            method = lookup.findVirtual(IntegralAppender.class, "acceptLong", MethodType.methodType(void.class, long.class, StringBuilder.class)).bindTo(appender);
            methods.put(Long.class, method);
            methods.put(long.class, method);
            return this;
        }

        @NotNull
        public Config use(@NotNull CharSequenceAppender appender) throws ReflectiveOperationException {
            MethodHandle method;
            method = lookup.findVirtual(CharSequenceAppender.class, "append", MethodType.methodType(void.class, CharSequence.class, StringBuilder.class)).bindTo(appender);
            for (Class<?> clazz : TowboatCharSequence.CHAR_SEQUENCE_CLASSES) {
                methods.put(clazz, method);
            }
            method = lookup.findVirtual(CharSequenceAppender.class, "append", MethodType.methodType(void.class, char.class, StringBuilder.class)).bindTo(appender);
            methods.put(Character.class, method);
            methods.put(char.class, method);
            return this;
        }

        @NotNull
        public Config use(@NotNull DateFormat format) throws ReflectiveOperationException {
            MethodHandle method;
            method = lookup.findStatic(AppenderToStringBuilder.class, "acceptDate", MethodType.methodType(void.class, DateFormat.class, Date.class, StringBuilder.class)).bindTo(format);
            methods.put(Date.class, method);
            method = lookup.findStatic(AppenderToStringBuilder.class, "acceptCalendar", MethodType.methodType(void.class, DateFormat.class, Calendar.class, StringBuilder.class)).bindTo(format);
            methods.put(Date.class, method);
            method = lookup.findStatic(AppenderToStringBuilder.class, "acceptTimestamp", MethodType.methodType(void.class, DateFormat.class, long.class, StringBuilder.class)).bindTo(format);
            methods.put(Long.class, method);
            methods.put(long.class, method);
            return this;
        }

        @NotNull
        public Config booleanTrueFalse() throws ReflectiveOperationException {
            MethodHandle method = lookup.findStatic(AppenderToStringBuilder.class, "acceptBooleanTrueFalse", MethodType.methodType(void.class, boolean.class, StringBuilder.class));
            methods.put(Boolean.class, method);
            methods.put(boolean.class, method);
            return this;
        }

        @NotNull
        public Config booleanYesNo() throws ReflectiveOperationException {
            MethodHandle method = lookup.findStatic(AppenderToStringBuilder.class, "acceptBooleanYesNo", MethodType.methodType(void.class, boolean.class, StringBuilder.class));
            methods.put(Boolean.class, method);
            methods.put(boolean.class, method);
            return this;
        }

        @NotNull
        public Config booleanOnOff() throws ReflectiveOperationException {
            MethodHandle method = lookup.findStatic(AppenderToStringBuilder.class, "acceptBooleanOnOff", MethodType.methodType(void.class, boolean.class, StringBuilder.class));
            methods.put(Boolean.class, method);
            methods.put(boolean.class, method);
            return this;
        }

        @NotNull
        public Config numberIntegral() throws ReflectiveOperationException {
            MethodHandle method;
            method = lookup.findVirtual(AppenderToStringBuilder.class, "acceptInt", MethodType.methodType(void.class, Number.class, StringBuilder.class)).bindTo(AppenderToStringBuilder.this);
            for (Class<?> clazz : DefaultNumberAppender.NUMBER_INT_CLASSES) {
                methods.put(clazz, method);
            }
            method = lookup.findVirtual(AppenderToStringBuilder.class, "acceptLong", MethodType.methodType(void.class, Number.class, StringBuilder.class)).bindTo(AppenderToStringBuilder.this);
            for (Class<?> clazz : DefaultNumberAppender.NUMBER_LONG_CLASSES) {
                methods.put(clazz, method);
            }
            return this;
        }

        @NotNull
        public Config numberDecimal() throws ReflectiveOperationException {
            MethodHandle method;
            method = lookup.findVirtual(AppenderToStringBuilder.class, "acceptFloat", MethodType.methodType(void.class, Number.class, StringBuilder.class)).bindTo(AppenderToStringBuilder.this);
            for (Class<?> clazz : DefaultNumberAppender.NUMBER_FLOAT_CLASSES) {
                methods.put(clazz, method);
            }
            method = lookup.findVirtual(AppenderToStringBuilder.class, "acceptDouble", MethodType.methodType(void.class, Number.class, StringBuilder.class)).bindTo(AppenderToStringBuilder.this);
            for (Class<?> clazz : DefaultNumberAppender.NUMBER_DOUBLE_CLASSES) {
                methods.put(clazz, method);
            }
            return this;
        }

        @NotNull
        public Config objectDefault(@NotNull Class<?>... classes) throws ReflectiveOperationException {
            final MethodHandle method = lookup.findStatic(AppenderToStringBuilder.class, "acceptObjectDefault", MethodType.methodType(void.class, Object.class, StringBuilder.class));
            for (Class<?> clazz : classes) {
                if (clazz.isInterface()) {
                    LOGGER.warn("{} is useless", clazz); // "interface xxx.Yyy is useless"
                }
                methods.put(clazz, method);
            }
            return this;
        }
    }

    @NotNull
    public Config config() {
        return new Config(MethodHandles.lookup());
    }

    AppenderToStringBuilder() {
        super();
    }

    @NotNull
    public static AppenderToStringBuilder create() throws ReflectiveOperationException {
        final AppenderToStringBuilder instance = new AppenderToStringBuilder();
        instance.config().use(instance.newArrayStyle(), Object.class);
        return instance;
    }

    @CodeHistory(date = "2026/3/8")
    public static final class Java {

        private static final ThreadLocal<AppenderToStringBuilder> THREAD_LOCAL = ThreadLocal.withInitial(() -> {
            try {
                AppenderToStringBuilder instance = new AppenderToStringBuilder();
                instance.config().use(instance.newArrayStyle().setLeft("{").setRight("}").setEmpty("{ }"), Object.class)
                        .use(MarkedNumberAppender.INSTANCE)
                        .use(CharSequenceAppender.Java.AUTO);
                return instance;
            } catch (ReflectiveOperationException e) {
                LOGGER.error("java", e);
                throw new RuntimeException(e);
            }
        });

        @NotNull
        public static AppenderToStringBuilder get() {
            return THREAD_LOCAL.get();
        }
    }

    @CodeHistory(date = "2026/3/9")
    public static final class Json {

        private static final ThreadLocal<AppenderToStringBuilder> THREAD_LOCAL = ThreadLocal.withInitial(() -> {
            try {
                AppenderToStringBuilder instance = new AppenderToStringBuilder();
                instance.config().use(instance.newArrayStyle(), Object.class, int[].class, long[].class)
                        .use(CharSequenceAppender.Json.ESSENTIAL);
                return instance;
            } catch (ReflectiveOperationException e) {
                LOGGER.error("json", e);
                throw new RuntimeException(e);
            }
        });

        @NotNull
        public static AppenderToStringBuilder get() {
            return THREAD_LOCAL.get();
        }
    }

    @CodeHistory(date = "2026/3/21")
    public static final class JavaScript {

        private static final ThreadLocal<AppenderToStringBuilder> THREAD_LOCAL = ThreadLocal.withInitial(() -> {
            try {
                AppenderToStringBuilder instance = new AppenderToStringBuilder();
                instance.config().use(instance.newArrayStyle(), Object.class, int[].class)
                        .use(CharSequenceAppender.JavaScript.AUTO).numberIntegral().numberDecimal();
                return instance;
            } catch (ReflectiveOperationException e) {
                LOGGER.error("javascript", e);
                throw new RuntimeException(e);
            }
        });

        @NotNull
        public static AppenderToStringBuilder get() {
            return THREAD_LOCAL.get();
        }
    }

    @CodeHistory(date = "2026/3/18")
    public static final class MatLab {

        private static final ThreadLocal<AppenderToStringBuilder> THREAD_LOCAL = ThreadLocal.withInitial(() -> {
            try {
                AppenderToStringBuilder instance = new AppenderToStringBuilder();
                instance.config().use(instance.newArrayStyle(), Object.class, int[].class, long[].class, float[].class, double[].class)
                        .use(instance.newMatrixStyle(), int[][].class, long[][].class, float[][].class, double[][].class)
                        .use(CharSequenceAppender.Java.AUTO);
                return instance;
            } catch (ReflectiveOperationException e) {
                LOGGER.error("matlab", e);
                throw new RuntimeException(e);
            }
        });

        @NotNull
        public static AppenderToStringBuilder get() {
            return THREAD_LOCAL.get();
        }
    }
}
