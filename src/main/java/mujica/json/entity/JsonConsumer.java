package mujica.json.entity;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created on 2025/10/12.
 */
@CodeHistory(date = "2022/6/4", project = "Ultramarine")
@CodeHistory(date = "2025/10/12")
public interface JsonConsumer extends ClassifiedJsonConsumer {

    @Override
    void openArray();

    @Override
    void closeArray();

    void emptyArrayValue();

    void arrayValue(@NotNull boolean[] array);

    void arrayValue(@NotNull int[] array);

    void arrayValue(@NotNull long[] array);

    void arrayValue(@NotNull float[] array);

    void arrayValue(@NotNull double[] array);

    void arrayValue(@NotNull String[] array);

    void arrayValue(@NotNull Object[] array);

    void arrayValue(@NotNull Iterable<?> array);

    void multipleDimensionArrayValue(@NotNull int[][] array);

    void multipleDimensionArrayValue(@NotNull long[][] array);

    void multipleDimensionArrayValue(@NotNull float[][] array);

    void multipleDimensionArrayValue(@NotNull double[][] array);

    void multipleDimensionArrayValue(@NotNull Object[] array);

    void multipleDimensionArrayValue(@NotNull Iterable<?> array);

    @Override
    void openObject();

    @Override
    void closeObject();

    void emptyObjectValue();

    void intKey(int key);

    void optionalIntKey(int key);

    @Override
    void stringKey(@NotNull String key);

    @Override
    void stringKey(@NotNull FastString key);

    void optionalStringKey(@Nullable String key);

    void optionalStringKey(@Nullable FastString key);

    @Override
    void jsonValue(@NotNull String value);

    @Override
    void nullValue();

    @Override
    void booleanValue(boolean value);

    @Override
    void numberValue(long value);

    @Override
    void numberValue(double value);

    @Override
    void numberValue(@NotNull FastNumber value);

    @Override
    void stringValue(@NotNull String value);

    @Override
    void stringValue(@NotNull FastString value);
}
