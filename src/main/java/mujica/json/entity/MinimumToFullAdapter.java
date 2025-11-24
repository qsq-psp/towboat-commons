package mujica.json.entity;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2025/11/6")
public class MinimumToFullAdapter extends MinimumToClassifiedAdapter implements JsonConsumer {

    public MinimumToFullAdapter(@NotNull MinimumJsonConsumer jc) {
        super(jc);
    }

    @Override
    public void emptyArrayValue() {
        openArray();
        closeArray();
    }

    @Override
    public void arrayValue(@NotNull boolean[] array) {
        openArray();
        for (boolean value : array) {
            booleanValue(value);
        }
        closeArray();
    }

    @Override
    public void arrayValue(@NotNull int[] array) {
        openArray();
        for (int value : array) {
            numberValue(value);
        }
        closeArray();
    }

    @Override
    public void arrayValue(@NotNull long[] array) {
        openArray();
        for (long value : array) {
            numberValue(value);
        }
        closeArray();
    }

    @Override
    public void arrayValue(@NotNull float[] array) {
        openArray();
        for (float value : array) {
            numberValue(value);
        }
        closeArray();

    }

    @Override
    public void arrayValue(@NotNull double[] array) {
        openArray();
        for (double value : array) {
            numberValue(value);
        }
        closeArray();
    }

    @Override
    public void arrayValue(@NotNull String[] array) {
        openArray();
        for (String value : array) {
            stringValue(value);
        }
        closeArray();
    }

    @Override
    public void arrayValue(@NotNull Object[] array) {
        openArray();
        for (Object value : array) {
            if (value == null) {
                nullValue();
            } else {
                stringValue(value.toString());
            }
        }
        closeArray();
    }

    @Override
    public void arrayValue(@NotNull Iterable<?> array) {
        openArray();
        for (Object value : array) {
            if (value == null) {
                nullValue();
            } else {
                stringValue(value.toString());
            }
        }
        closeArray();
    }

    @Override
    public void multipleDimensionArrayValue(@NotNull int[][] array) {
        openArray();
        for (int[] row : array) {
            arrayValue(row);
        }
        closeArray();
    }

    @Override
    public void multipleDimensionArrayValue(@NotNull long[][] array) {
        openArray();
        for (long[] row : array) {
            arrayValue(row);
        }
        closeArray();
    }

    @Override
    public void multipleDimensionArrayValue(@NotNull float[][] array) {
        openArray();
        for (float[] row : array) {
            arrayValue(row);
        }
        closeArray();
    }

    @Override
    public void multipleDimensionArrayValue(@NotNull double[][] array) {
        openArray();
        for (double[] row : array) {
            arrayValue(row);
        }
        closeArray();
    }

    @Override
    public void multipleDimensionArrayValue(@NotNull Object[] array) {
        openArray();
        for (Object value : array) {
            if (value == null) {
                nullValue();
            } else if (value instanceof Object[]) {
                multipleDimensionArrayValue((Object[]) value);
            } else {
                stringValue(value.toString());
            }
        }
        closeArray();
    }

    @Override
    public void multipleDimensionArrayValue(@NotNull Iterable<?> array) {
        openArray();
        for (Object value : array) {
            if (value == null) {
                nullValue();
            } else if (value instanceof Iterable) {
                multipleDimensionArrayValue((Iterable<?>) value);
            } else {
                stringValue(value.toString());
            }
        }
        closeArray();
    }

    @Override
    public void emptyObjectValue() {
        openObject();
        closeObject();
    }

    @Override
    public void intKey(int key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void optionalIntKey(int key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void optionalStringKey(@Nullable String key) {
        if (key != null) {
            stringKey(key);
        }
    }

    @Override
    public void optionalStringKey(@Nullable FastString key) {
        if (key != null) {
            stringKey(key);
        }
    }
}
