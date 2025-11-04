package mujica.reflect.modifier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

@CodeHistory(date = "2019/12/26", project = "coo", name = "IndexStringMap")
@CodeHistory(date = "2021/9/15", project = "nettyon")
@CodeHistory(date = "2022/4/1", project = "infrastructure")
@CodeHistory(date = "2022/6/3", project = "Ultramarine")
@CodeHistory(date = "2025/3/12")
@Stable(date = "2025/7/25")
public class EnumMapping implements Serializable {

    private static final long serialVersionUID = 0xf10dfa3a9fb3e15bL;

    private static final Logger LOGGER = LoggerFactory.getLogger(EnumMapping.class);

    private static final Object DEFAULT_KEY = new Object();
    private static final Object DEFAULT_VALUE = new Object();

    private static String unifyCase(String string) {
        return string.toUpperCase();
    }

    /**
     * EnumMapping supports multiple threads, so LocaleInteger is immutable and not cached like Attributes3Impl.IndexKey or Attributes3Impl.NameKey
     */
    private static class LocaleInteger implements Serializable {

        private static final long serialVersionUID = 0xbb502d69cbea5e7aL;

        @NotNull
        final Locale locale;

        final int value;

        private LocaleInteger(Locale locale, int value) {
            super();
            this.locale = Objects.requireNonNull(locale);
            this.value = value;
        }

        /**
         * @return hashCode() is necessary for it works as a HashMap key
         */
        @Override
        public int hashCode() {
            return locale.hashCode() * 0xfd3715b1 + value;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof LocaleInteger) {
                LocaleInteger that = (LocaleInteger) obj;
                return this.value == that.value && this.locale.equals(that.locale);
            }
            return false;
        }

        @Override
        public String toString() {
            return "(" + locale + ", " + value + ")";
        }
    }

    private final HashMap<Object, Object> map = new HashMap<>();

    private final boolean caseInsensitive;

    public EnumMapping(boolean caseInsensitive) {
        super();
        this.caseInsensitive = caseInsensitive;
    }

    public EnumMapping() {
        this(false);
    }

    public EnumMapping(@NotNull EnumMapping that) {
        super();
        this.map.putAll(that.map);
        this.caseInsensitive = that.caseInsensitive;
    }

    @NotNull
    public EnumMapping add(@NotNull String key, @NotNull Integer value) {
        Objects.requireNonNull(value);
        if (caseInsensitive) {
            map.put(unifyCase(key), value);
        } else {
            Objects.requireNonNull(key);
            map.put(key, value);
        }
        map.put(value, key);
        return this;
    }

    @NotNull
    public EnumMapping add(@NotNull String key, int value, @NotNull Locale locale) {
        final LocaleInteger localeValue = new LocaleInteger(locale, value);
        if (caseInsensitive) {
            map.put(unifyCase(key), localeValue);
        } else {
            map.put(key, localeValue);
        }
        map.put(localeValue, key);
        return this;
    }

    @NotNull
    public EnumMapping addDefault(@NotNull String key, @NotNull Integer value) {
        Objects.requireNonNull(value);
        if (caseInsensitive) {
            map.put(unifyCase(key), value);
        } else {
            Objects.requireNonNull(key);
            map.put(key, value);
        }
        map.put(value, key);
        map.put(DEFAULT_KEY, key);
        map.put(DEFAULT_VALUE, value);
        return this;
    }

    private static final int INTERESTED_MODIFIER = Modifier.STATIC | Modifier.FINAL;

    private boolean testType(@NotNull Class<?> clazz) {
        return int.class == clazz || short.class == clazz || byte.class == clazz || Integer.class == clazz || Short.class == clazz || Byte.class == clazz;
    }

    @NotNull
    public EnumMapping addFromClass(@NotNull Class<?> clazz, @Nullable Predicate<String> nameFilter, @Nullable UnaryOperator<String> nameTransformer, boolean publicOnly) {
        int interestedModifier = INTERESTED_MODIFIER;
        if (publicOnly) {
            interestedModifier |= Modifier.PUBLIC;
        }
        for (Field field : clazz.getDeclaredFields()) {
            if ((field.getModifiers() & interestedModifier) == interestedModifier && testType(field.getType())) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(null);
                    if (value instanceof Number) {
                        String name = field.getName();
                        if (nameFilter == null || nameFilter.test(name)) {
                            if (nameTransformer != null) {
                                name = nameTransformer.apply(name);
                            }
                            add(name, ((Number) value).intValue());
                        }
                    } else {
                        LOGGER.warn("Unexpected type {} out of {}", value, field);
                    }
                } catch (ReflectiveOperationException e) {
                    LOGGER.warn("Fail to reflect on field {}", field, e);
                }
            }
        }
        return this;
    }

    @NotNull
    public EnumMapping addFromClass(@NotNull Class<?> clazz, @Nullable Predicate<String> nameFilter, @Nullable UnaryOperator<String> nameTransformer) {
        return addFromClass(clazz, nameFilter, nameTransformer, true);
    }

    @NotNull
    public EnumMapping remove(@NotNull String key) {
        Object value;
        if (caseInsensitive) {
            value = map.remove(unifyCase(key));
        } else {
            value = map.remove(key);
        }
        if (value instanceof Integer) {
            map.remove(value);
            map.remove(DEFAULT_KEY, key);
            map.remove(DEFAULT_VALUE, value);
        }
        return this;
    }

    @NotNull
    public EnumMapping remove(@NotNull Integer value) {
        final Object key = map.remove(value);
        if (key instanceof String) {
            if (caseInsensitive) {
                map.remove(unifyCase(((String) key)));
            } else {
                map.remove(key);
            }
            map.remove(DEFAULT_KEY, key);
            map.remove(DEFAULT_VALUE, value);
        }
        return this;
    }

    /**
     * @param value actual type: Integer
     */
    public Object get(Object value) {
        return map.get(value);
    }

    public Object get(int value, @NotNull Locale locale) {
        return map.get(new LocaleInteger(locale, value));
    }

    /**
     * @return Expect String here, but no cast is applied
     */
    public Object getDefaultKey() {
        return map.get(DEFAULT_KEY);
    }

    /**
     * @return Expect Integer here, but no cast is applied
     */
    public Object getDefaultValue() {
        return map.get(DEFAULT_VALUE);
    }

    public Integer forValue(String key) {
        if (caseInsensitive && key != null) {
            key = unifyCase(key);
        }
        final Object value = map.get(key);
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value == null) {
            return (Integer) map.get(DEFAULT_VALUE);
        } else {
            return null;
        }
    }

    @SuppressWarnings("ReplaceNullCheck")
    public int forValue(String key, int fallback) {
        final Integer value = forValue(key);
        if (value != null) {
            return value;
        } else {
            return fallback;
        }
    }

    public String forKey(Integer value) {
        final Object key = map.get(value);
        if (key instanceof String) {
            return (String) key;
        } else if (key == null) {
            return (String) map.get(DEFAULT_KEY);
        } else {
            return null;
        }
    }

    public void forKey(int value, @NotNull StringBuilder sb) {
        final String key = forKey(value);
        if (key != null) {
            sb.append(key);
        } else {
            sb.append(value);
        }
    }

    @NotNull
    public Iterable<String> keys() {
        return new KeysIterator();
    }

    private class KeysIterator implements Iterable<String>, Iterator<String> {

        private Iterator<Object> iterator;

        private String next, prev;

        @Override
        @NotNull
        public Iterator<String> iterator() {
            iterator = map.keySet().iterator();
            return this;
        }

        @Override
        public boolean hasNext() {
            if (next != null) {
                return true;
            }
            while (iterator.hasNext()) {
                Object object = iterator.next();
                if (object instanceof String) {
                    next = (String) object;
                    return true;
                }
            }
            return false;
        }

        @Override
        public String next() {
            if (next != null) {
                prev = next;
                next = null; // consume
                return prev;
            }
            // should not happen if every next() call is after a hasNext() call
            while (true) {
                Object object = iterator.next();
                if (object instanceof String) {
                    prev = (String) object;
                    return prev;
                }
            }
        }

        @Override
        public void remove() {
            if (prev != null) {
                EnumMapping.this.remove(prev);
                prev = null;
            }
        }
    }

    @NotNull
    public Iterable<Integer> values() {
        return new ValuesIterator();
    }

    private class ValuesIterator implements Iterable<Integer>, Iterator<Integer> {

        private Iterator<Object> iterator;

        private Integer next, prev;

        @Override
        @NotNull
        public Iterator<Integer> iterator() {
            iterator = map.keySet().iterator();
            return this;
        }

        @Override
        public boolean hasNext() {
            if (next != null) {
                return true;
            }
            while (iterator.hasNext()) {
                Object object = iterator.next();
                if (object instanceof Integer) {
                    next = (Integer) object;
                    return true;
                }
            }
            return false;
        }

        @Override
        public Integer next() {
            if (next != null) {
                prev = next;
                next = null; // consume
                return prev;
            }
            // should not happen if every next() call is after a hasNext() call
            while (true) {
                Object object = iterator.next();
                if (object instanceof Integer) {
                    prev = (Integer) object;
                    return prev;
                }
            }
        }

        @Override
        public void remove() {
            if (prev != null) {
                EnumMapping.this.remove(prev);
                prev = null;
            }
        }
    }

    @Override
    public int hashCode() {
        return map.hashCode() ^ Boolean.hashCode(caseInsensitive);
    }
}
