package mujica.text.format;

import mujica.reflect.modifier.AccessStructure;
import mujica.reflect.modifier.CodeHistory;
import mujica.text.number.IntegralAppender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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

    private void invoke(@NotNull Object object, @NotNull Class<?> clazz0, @NotNull StringBuilder out) {
        Class<?> clazz1 = clazz0;
        do {
            MethodHandle method = methods.get(clazz1);
            if (method != null) {
                try {
                    method.invoke(object, out);
                } catch (Throwable e) {
                    LOGGER.warn("invoke {} {}", clazz0.getName(), clazz1.getName(), e);
                }
                return;
            }
            clazz1 = clazz1.getSuperclass();
        } while (clazz1 != null);
        LOGGER.warn("invoke {}", clazz0.getName());
    }

    @NotNull
    private String nullString = "null";

    @NotNull
    public String getNullString() {
        return nullString;
    }

    @NotNull
    public AppenderToStringBuilder setNullString(@NotNull String nullString) {
        this.nullString = nullString;
        return this;
    }

    @Override
    public void accept(@Nullable Object object, @NotNull StringBuilder out) {
        if (object == null) {
            out.append(nullString);
            return;
        }
        invoke(object, object.getClass(), out);
    }

    @CodeHistory(date = "2026/3/9")
    public class ArrayStyle {

        @NotNull
        private String left, right, separator, empty, loop;
    }

    private void acceptObject(@NotNull Object object, @NotNull StringBuilder out) {
        try {
            int length = Array.getLength(object); // throws IllegalArgumentException if not array
            if (length > 0) {
                out.append("[");
                accept(Array.get(object, 0), out);
                for (int index = 1; index < length; index++) {
                    out.append(", ");
                    accept(Array.get(object, index), out);
                }
                out.append("]");
            } else {
                out.append("[]");
            }
        } catch (IllegalArgumentException e) {
            out.append(object.toString());
        }
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
        invoke(number.intValue(), int.class, out);
    }

    private void acceptLong(@NotNull Number number, @NotNull StringBuilder out) {
        invoke(number.longValue(), long.class, out);
    }

    private void acceptFloat(@NotNull Number number, @NotNull StringBuilder out) {
        invoke(number.floatValue(), float.class, out);
    }

    private void acceptDouble(@NotNull Number number, @NotNull StringBuilder out) {
        invoke(number.doubleValue(), double.class, out);
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

    public AppenderToStringBuilder() throws ReflectiveOperationException {
        super();
        methods.put(Object.class, MethodHandles.lookup()
                .findVirtual(AppenderToStringBuilder.class, "acceptObject", MethodType.methodType(void.class, Object.class, StringBuilder.class)).bindTo(this)
        );
    }

    @NotNull
    public Config config() {
        return new Config(MethodHandles.lookup());
    }

    public class Config {

        @NotNull
        final MethodHandles.Lookup lookup;

        Config(@NotNull MethodHandles.Lookup lookup) {
            super();
            this.lookup = lookup;
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
            for (Class<?> clazz : IntegralAppender.NUMBER_INT_CLASSES) {
                methods.put(clazz, method);
            }
            method = lookup.findVirtual(AppenderToStringBuilder.class, "acceptLong", MethodType.methodType(void.class, Number.class, StringBuilder.class)).bindTo(AppenderToStringBuilder.this);
            for (Class<?> clazz : IntegralAppender.NUMBER_LONG_CLASSES) {
                methods.put(clazz, method);
            }
            return this;
        }

        @NotNull
        public Config numberDecimal() throws ReflectiveOperationException {
            MethodHandle method;
            method = lookup.findVirtual(AppenderToStringBuilder.class, "acceptFloat", MethodType.methodType(void.class, Number.class, StringBuilder.class)).bindTo(AppenderToStringBuilder.this);
            for (Class<?> clazz : IntegralAppender.NUMBER_INT_CLASSES) {
                methods.put(clazz, method);
            }
            method = lookup.findVirtual(AppenderToStringBuilder.class, "acceptDouble", MethodType.methodType(void.class, Number.class, StringBuilder.class)).bindTo(AppenderToStringBuilder.this);
            for (Class<?> clazz : IntegralAppender.NUMBER_LONG_CLASSES) {
                methods.put(clazz, method);
            }
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

        @NotNull
        public Config use(@NotNull IntegralAppender appender) throws ReflectiveOperationException {
            MethodHandle method;
            method = lookup.findVirtual(IntegralAppender.class, "acceptByte", MethodType.methodType(void.class, byte.class, StringBuilder.class)).bindTo(appender);
            methods.put(Byte.class, method);
            methods.put(byte.class, method);
            method = lookup.findVirtual(IntegralAppender.class, "acceptShort", MethodType.methodType(void.class, short.class, StringBuilder.class)).bindTo(appender);
            methods.put(Short.class, method);
            methods.put(short.class, method);
            method = lookup.findVirtual(IntegralAppender.class, "acceptCharacter", MethodType.methodType(void.class, char.class, StringBuilder.class)).bindTo(appender);
            methods.put(Character.class, method);
            methods.put(char.class, method);
            method = lookup.findVirtual(IntegralAppender.class, "acceptInteger", MethodType.methodType(void.class, int.class, StringBuilder.class)).bindTo(appender);
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
            for (Class<?> clazz : CharSequenceAppender.CHAR_SEQUENCE_CLASSES) {
                methods.put(clazz, method);
            }
            method = lookup.findVirtual(CharSequenceAppender.class, "append", MethodType.methodType(void.class, char.class, StringBuilder.class)).bindTo(appender);
            methods.put(Character.class, method);
            methods.put(char.class, method);
            return this;
        }
    }

    @CodeHistory(date = "2026/3/8")
    public static final class Java {

        private static final ThreadLocal<AppenderToStringBuilder> THREAD_LOCAL = ThreadLocal.withInitial(() -> {
            try {
                return new AppenderToStringBuilder();
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
    public static final class Java13 { // text blocks

    }
}
