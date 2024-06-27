package indi.qsq.json.api;

import indi.qsq.json.entity.RawNumber;
import indi.qsq.util.reflect.ClassUtility;
import indi.qsq.util.text.Quote;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 2022/8/26.
 */
@SuppressWarnings("ArrayHashCode")
public class DebugJsonConsumer implements JsonConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DebugJsonConsumer.class);

    @NotNull
    final JsonConsumer jc;

    @Nullable
    final Object id;

    public DebugJsonConsumer(@NotNull JsonConsumer jc, @Nullable Object id) {
        super();
        this.jc = jc;
        this.id = id;
    }

    public DebugJsonConsumer(JsonConsumer jc) {
        this(jc, "this");
    }

    public DebugJsonConsumer() {
        this(JsonNullConsumer.INSTANCE, "this");
    }

    @Override
    public void key(@NotNull String key) {
        LOGGER.debug("{}.key({})", id, Quote.DEFAULT.apply(key, "null"));
        jc.key(key);
    }

    @Override
    public void jsonValue(@NotNull CharSequence json) {
        LOGGER.debug("{}.jsonValue({} {})", id, ClassUtility.nearlyFull(json), json);
        jc.jsonValue(json);
    }

    @Override
    public void jsonValue(ByteBuf json) {
        LOGGER.debug("{}.jsonValue({} {})", id, ClassUtility.nearlyFull(json), json);
        jc.jsonValue(json);
    }

    @Override
    public void nullValue() {
        LOGGER.debug("{}.nullValue()", id);
        jc.nullValue();
    }

    @Override
    public void booleanValue(boolean value) {
        LOGGER.debug("{}.booleanValue({})", id, value);
        jc.booleanValue(value);
    }

    @Override
    public void numberValue(long value) {
        LOGGER.debug("{}.numberValue(long {})", id, value);
        jc.numberValue(value);
    }

    @Override
    public void numberValue(double value) {
        LOGGER.debug("{}.numberValue(double {})", id, value);
        jc.numberValue(value);
    }

    @Override
    public void numberValue(@NotNull RawNumber value) {
        LOGGER.debug("{}.numberValue(RawDecimal {})", id, value);
        jc.numberValue(value);
    }

    @Override
    public void stringValue(@NotNull String value) {
        LOGGER.debug("{}.stringValue({})", id, Quote.DEFAULT.apply(value, "null"));
        jc.stringValue(value);
    }

    @Override
    public boolean optionalStringEntry(@NotNull String key, String value) {
        LOGGER.debug("{}.optionalStringEntry({}, {})", id, key, value);
        return jc.optionalStringEntry(key, value);
    }

    @Override
    public boolean nonEmptyStringEntry(@NotNull String key, @Nullable String value) {
        LOGGER.debug("{}.nonEmptyStringEntry({}, {})", id, key, value);
        return jc.nonEmptyStringEntry(key, value);
    }

    @Override
    public void objectValue(Object value) {
        LOGGER.debug("{}.objectValue({})", id, ClassUtility.nearlyFull(value));
        jc.objectValue(value);
    }

    @Override
    public boolean optionalObjectEntry(String key, Object value) {
        LOGGER.debug("{}.optionalObjectEntry({}, {})", id, key, ClassUtility.nearlyFull(value));
        return jc.optionalObjectEntry(key, value);
    }

    @Override
    public void openArray() {
        LOGGER.debug("{}.openArray()", id);
        jc.openArray();
    }

    @Override
    public void closeArray() {
        LOGGER.debug("{}.closeArray()", id);
        jc.closeArray();
    }

    @Override
    public void openObject() {
        LOGGER.debug("{}.openObject()", id);
        jc.openObject();
    }

    @Override
    public void closeObject() {
        LOGGER.debug("{}.closeObject()", id);
        jc.closeObject();
    }

    @Override
    public void arrayValue() {
        LOGGER.debug("{}.arrayValue()", id);
        jc.arrayValue();
    }

    @Override
    public void arrayValue(@NotNull int[] array) {
        LOGGER.debug("{}.arrayValue(int[{}] @{})", id, array.length, Integer.toHexString(array.hashCode()));
        jc.arrayValue(array);
    }

    @Override
    public void arrayValue(@NotNull int[][] array) {
        LOGGER.debug("{}.arrayValue(int[{}][] @{})", id, array.length, Integer.toHexString(array.hashCode()));
        jc.arrayValue(array);
    }

    @Override
    public void arrayValue(@NotNull long[] array) {
        LOGGER.debug("{}.arrayValue(long[{}] @{})", id, array.length, Integer.toHexString(array.hashCode()));
        jc.arrayValue(array);
    }

    @Override
    public void arrayValue(@NotNull float[] array) {
        LOGGER.debug("{}.arrayValue(float[{}] @{})", id, array.length, Integer.toHexString(array.hashCode()));
        jc.arrayValue(array);
    }

    @Override
    public void arrayValue(@NotNull double[] array) {
        LOGGER.debug("{}.arrayValue(double[{}] @{})", id, array.length, Integer.toHexString(array.hashCode()));
        jc.arrayValue(array);
    }

    @Override
    public void arrayValue(@NotNull String[] array) {
        LOGGER.debug("{}.arrayValue(String[{}] @{})", id, array.length, Integer.toHexString(array.hashCode()));
        jc.arrayValue(array);
    }

    @Override
    public void arrayValue(@NotNull Object[] array) {
        LOGGER.debug("{}.arrayValue(String[{}] @{})", id, array.length, Integer.toHexString(array.hashCode()));
        jc.arrayValue(array);
    }

    @Override
    public void stringsValue(@NotNull Iterable<String> list) {
        LOGGER.debug("{}.stringsValue({} as Iterable<String> @{})", id, ClassUtility.nearlyFull(list), Integer.toHexString(System.identityHashCode(list)));
        jc.stringsValue(list);
    }

    @Override
    public void structuresValue(@NotNull Iterable<? extends JsonStructure> list) {
        LOGGER.debug("{}.structuresValue({} as Iterable<JsonStructure> @{})", id, ClassUtility.nearlyFull(list), Integer.toHexString(System.identityHashCode(list)));
        jc.structuresValue(list);
    }

    @Override
    public void objectsValue(@NotNull Iterable<Object> list) {
        LOGGER.debug("{}.structuresValue({} as Iterable<Object> @{})", id, ClassUtility.nearlyFull(list), Integer.toHexString(System.identityHashCode(list)));
        jc.objectsValue(list);
    }

    @Override
    public void enumNamesValue(@NotNull Iterable<? extends Enum<?>> list) {
        LOGGER.debug("{}.structuresValue({} as Iterable<Enum> @{})", id, ClassUtility.nearlyFull(list), Integer.toHexString(System.identityHashCode(list)));
        jc.enumNamesValue(list);
    }

    @Override
    public void objectValue() {
        LOGGER.debug("{}.objectValue()", id);
        jc.arrayValue();
    }

    @Override
    public boolean checksStructure() {
        final boolean value = jc.checksStructure();
        LOGGER.debug("{}.checksStructure() = {}", id, value);
        return value;
    }
}
