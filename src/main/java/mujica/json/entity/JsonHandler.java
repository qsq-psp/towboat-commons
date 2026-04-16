package mujica.json.entity;

import io.netty.buffer.ByteBuf;
import mujica.ds.generic.set.CollectionConstant;
import mujica.reflect.modifier.CodeHistory;
import mujica.text.sanitizer.CharSequenceAppender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;

@CodeHistory(date = "2022/6/4", project = "Ultramarine", name = "JsonConsumer")
@CodeHistory(date = "2025/10/12", name = "JsonConsumer")
@CodeHistory(date = "2026/1/4")
public abstract class JsonHandler implements TypePreference {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonHandler.class);

    @Override
    public int typePreference() {
        return 0;
    }

    public void openArray() {
        LOGGER.warn("openArray()");
    }

    public void closeArray() {
        LOGGER.warn("closeArray()");
    }

    public void openObject() {
        LOGGER.warn("openObject()");
    }

    public void closeObject() {
        LOGGER.warn("closeObject()");
    }

    public void stringKey(@NotNull String key) {
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn("stringKey({})", CharSequenceAppender.Json.ESSENTIAL.stringify(key, (StringBuilder) null));
        }
    }

    public void stringKey(@NotNull FastString key) {
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn("stringKey(fast\"{}\")", key.string);
        }
    }

    public void simpleValue(@Nullable Object value) {
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn("objectValue({})", value);
        }
    }

    public void skippedValue() {
        simpleValue(CollectionConstant.UNDEFINED);
    }

    public void skippedValue(@NotNull ByteBuf value) {
        try {
            simpleValue(value);
        } finally {
            value.release();
        }
    }

    public void nullValue() {
        simpleValue(null);
    }

    public void booleanValue(boolean value) {
        simpleValue(value);
    }

    public void numberValue(int value) {
        simpleValue(value);
    }

    public void numberValue(long value) {
        simpleValue(value);
    }

    public void numberValue(float value) {
        simpleValue(value);
    }

    public void numberValue(double value) {
        simpleValue(value);
    }

    public void numberValue(@NotNull BigInteger value) {
        simpleValue(value);
    }

    public void numberValue(@NotNull FastNumber value) {
        simpleValue(value);
    }

    public void stringValue(@NotNull CharSequence value) {
        simpleValue(value.toString());
    }

    public void stringValue(@NotNull FastString value) {
        simpleValue(value.toString());
    }

    public void emptyArrayValue() {
        openArray();
        closeArray();
    }

    public void arrayValue(@NotNull boolean[] array) {
        openArray();
        for (boolean value : array) {
            booleanValue(value);
        }
        closeArray();
    }

    public void arrayValue(@NotNull int[] array) {
        openArray();
        for (int value : array) {
            numberValue(value);
        }
        closeArray();
    }

    public void arrayValue(@NotNull long[] array) {
        openArray();
        for (long value : array) {
            numberValue(value);
        }
        closeArray();
    }

    public void arrayValue(@NotNull float[] array) {
        openArray();
        for (float value : array) {
            numberValue(value);
        }
        closeArray();

    }

    public void arrayValue(@NotNull double[] array) {
        openArray();
        for (double value : array) {
            numberValue(value);
        }
        closeArray();
    }

    public void arrayValue(@NotNull String[] array) {
        openArray();
        for (String value : array) {
            stringValue(value);
        }
        closeArray();
    }

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

    public void multipleDimensionArrayValue(@NotNull int[][] array) {
        openArray();
        for (int[] row : array) {
            arrayValue(row);
        }
        closeArray();
    }

    public void multipleDimensionArrayValue(@NotNull long[][] array) {
        openArray();
        for (long[] row : array) {
            arrayValue(row);
        }
        closeArray();
    }

    public void multipleDimensionArrayValue(@NotNull float[][] array) {
        openArray();
        for (float[] row : array) {
            arrayValue(row);
        }
        closeArray();
    }

    public void multipleDimensionArrayValue(@NotNull double[][] array) {
        openArray();
        for (double[] row : array) {
            arrayValue(row);
        }
        closeArray();
    }

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

    public void emptyObjectValue() {
        openObject();
        closeObject();
    }
}
