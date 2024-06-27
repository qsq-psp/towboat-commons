package indi.qsq.json.api;

import indi.qsq.json.entity.RawNumber;
import indi.qsq.util.text.Quote;
import indi.qsq.util.text.TypedString;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * Created in webbiton on 2021/1/2, named JsonLeafHandler.
 * Recreated in infrastructure on 2021/9/19, named JsonPrimitivesConsumer.
 * Recreated on 2022/6/4.
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public interface JsonFlatConsumer {

    void key(@NotNull String key);

    default void key(@NotNull TypedString key) {
        key(key.getString());
    }

    default void optionalKey(String key) {
        if (key != null) {
            key(key);
        }
    }

    default void optionalKey(TypedString key) {
        if (key != null) {
            key(key);
        }
    }

    void jsonValue(@NotNull CharSequence json);

    default void jsonValue(ByteBuf json) {
        jsonValue(json.toString(StandardCharsets.UTF_8));
    }

    default void nullValue() {
        jsonValue("null");
    }

    default void booleanValue(boolean value) {
        jsonValue(Boolean.toString(value));
    }

    default void numberValue(long value) {
        jsonValue(Long.toString(value));
    }

    default void numberValue(double value) {
        jsonValue(Double.toString(value));
    }

    default void numberValue(@NotNull RawNumber value) {
        jsonValue(value.value);
    }

    default void optionalIntegerEntry(String key, Optional<Integer> optional) {
        if (optional.isPresent()) {
            key(key);
            numberValue(optional.get());
        }
    }

    default void stringValue(@NotNull String value) {
        final StringBuilder sb = new StringBuilder();
        Quote.JSON.append(sb, value);
        jsonValue(sb);
    }

    default void stringEntry(@NotNull String key, @NotNull String value) {
        key(key);
        stringValue(value);
    }

    default boolean optionalStringEntry(@NotNull String key, @Nullable String value) {
        if (value != null) {
            key(key);
            stringValue(value);
            return true;
        } else {
            return false;
        }
    }

    default void optionalStringEntry(@NotNull String key, @NotNull Optional<String> optional) {
        if (optional.isPresent()) {
            key(key);
            stringValue(optional.get());
        }
    }

    default boolean nonEmptyStringEntry(@NotNull String key, @Nullable String value) {
        if (value != null && !value.isEmpty()) {
            key(key);
            stringValue(value);
            return true;
        } else {
            return false;
        }
    }

    default void objectValue(@Nullable Object value) {
        if (value != null) {
            stringValue(value.toString());
        } else {
            nullValue();
        }
    }

    default void objectEntry(String key, Object value) {
        key(key);
        objectValue(value);
    }

    default boolean optionalObjectEntry(String key, Object value) {
        if (value != null) {
            key(key);
            stringValue(value.toString());
            return true;
        } else {
            return false;
        }
    }
}
