package indi.um.json.api;

import org.jetbrains.annotations.NotNull;

/**
 * Created on 2022/6/4.
 */
public interface JsonConsumer extends JsonFlatConsumer {

    void openArray();

    void closeArray();

    void openObject();

    void closeObject();

    /**
     * An empty array
     */
    default void arrayValue() {
        openArray();
        closeArray();
    }

    default void arrayValue(@NotNull int[] array) {
        openArray();
        for (int value : array) {
            numberValue(value);
        }
        closeArray();
    }

    default void arrayValue(@NotNull int[][] array) {
        openArray();
        for (int[] row : array) {
            arrayValue(row);
        }
        closeArray();
    }

    default void arrayValue(@NotNull long[] array) {
        openArray();
        for (long value : array) {
            numberValue(value);
        }
        closeArray();
    }

    default void arrayValue(@NotNull float[] array) {
        openArray();
        for (float value : array) {
            numberValue(value);
        }
        closeArray();
    }

    default void arrayValue(@NotNull double[] array) {
        openArray();
        for (double value : array) {
            numberValue(value);
        }
        closeArray();
    }

    default void arrayValue(@NotNull String[] array) {
        openArray();
        for (String value : array) {
            if (value != null) {
                stringValue(value);
            } else {
                nullValue();
            }
        }
        closeArray();
    }

    default void arrayValue(@NotNull Object[] array) {
        openArray();
        for (Object value : array) {
            if (value != null) {
                stringValue(value.toString());
            } else {
                nullValue();
            }
        }
        closeArray();
    }

    default void stringsValue(@NotNull Iterable<String> list) {
        openArray();
        for (String value : list) {
            if (value != null) {
                stringValue(value);
            } else {
                nullValue();
            }
        }
        closeArray();
    }

    default void structuresValue(@NotNull Iterable<? extends JsonStructure> list) {
        openArray();
        for (JsonStructure value : list) {
            if (value != null) {
                value.toJson(this);
            } else {
                nullValue();
            }
        }
        closeArray();
    }

    default void objectsValue(@NotNull Iterable<Object> list) {
        openArray();
        for (Object value : list) {
            if (value != null) {
                stringValue(value.toString());
            } else {
                nullValue();
            }
        }
        closeArray();
    }

    default void enumNamesValue(@NotNull Iterable<? extends Enum<?>> list) {
        openArray();
        for (Enum<?> e : list) {
            stringValue(e.name());
        }
        closeArray();
    }

    default void objectValue() {
        openObject();
        closeObject();
    }

    /**
     * Usually a stack is used to check structure correctness
     */
    default boolean checksStructure() {
        return false;
    }
}
