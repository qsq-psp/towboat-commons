package mujica.json.io;

import io.netty.buffer.ByteBuf;
import mujica.ds.of_int.list.CopyOnResizeIntList;
import mujica.json.entity.JsonHandler;
import mujica.json.entity.StructureChecked;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import mujica.text.format.AppenderToStringBuilder;
import mujica.text.sanitizer.CharSequenceAppender;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

@CodeHistory(date = "2021/12/30", project = "infrastructure", name = "JsonWriter")
@CodeHistory(date = "2022/6/4", project = "Ultramarine", name = "Writer")
@CodeHistory(date = "2026/1/6")
@DirectSubclass({JsonStringWriter.class, JsonStreamWriter.class, JsonByteBufWriter.class})
public abstract class JsonWriter extends JsonHandler implements StructureChecked {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonWriter.class);

    @CodeHistory(date = "2026/4/7")
    protected interface ConfigFlags {

        int ESCAPE_EXTRA = 0x01;
        int INFINITY_TO_NULL = 0x02;
        int NAN_TO_NULL = 0x04;
        int UPPERCASE_HEX = 0x10;
        int UPPERCASE_E = 0x20;
    }

    @MagicConstant(flagsFromClass = ConfigFlags.class)
    protected int flags;

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public int getFlags() {
        return flags;
    }

    // additional states to mujica.json.entity.StructureChecked
    public static final int STATE_NEW_ARRAY = 5;
    public static final int STATE_NEW_OBJECT = 6;
    public static final int STATE_JSONP = 7;

    protected final CopyOnResizeIntList stack = new CopyOnResizeIntList(null);

    protected JsonWriter() {
        super();
        stack.offerLast(STATE_START);
    }

    public void reset() {
        stack.clear();
        stack.offerLast(STATE_START);
    }

    public void openJsonp(@NotNull CharSequence name) {
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn("openJsonp({})", CharSequenceAppender.Json.ESSENTIAL.stringify(name, (StringBuilder) null));
        }
    }

    public void closeJsonp() {
        LOGGER.warn("closeJsonp()");
    }

    @Override
    public void simpleValue(@Nullable Object value) {
        LOGGER.error("simpleValue({})", AppenderToStringBuilder.Java.get().apply(value));
        throw new UnsupportedOperationException();
    }

    @Override
    public void skippedValue() {
        LOGGER.error("skippedValue()");
        throw new UnsupportedOperationException();
    }

    @Override
    public void skippedValue(@NotNull ByteBuf value) {
        try {
            (new JsonByteBufReader(value)).read(this);
        } finally {
            value.release();
        }
    }

    @CodeHistory(date = "2020/12/16", project = "webbiton", name = "JsonBlobBuilder.State")
    @CodeHistory(date = "2022/6/12", project = "Ultramarine", name = "WriterStates")
    @CodeHistory(date = "2026/4/1")
    private static class Debug {

        private static final String[] STATE_NAMES = {
                "start", "end", "array", "object", "key", "new-array", "new-object", "jsonp"
        };

        @NotNull
        protected static String stateToString(int state) {
            try {
                return STATE_NAMES[state];
            } catch (IndexOutOfBoundsException e) { // rare
                return "unknown(" + state + ")";
            }
        }

        @NotNull
        protected static String stateToString(@NotNull CopyOnResizeIntList stack) {
            final StringBuilder sb = new StringBuilder();
            boolean subsequent = false;
            sb.append("[");
            for (int state : stack) {
                if (subsequent) {
                    sb.append(", ");
                }
                sb.append(stateToString(state));
                subsequent = true;
            }
            return sb.append("]").toString();
        }

        private Debug() {
            super();
        }
    }

    protected void throwState() throws IllegalStateException {
        throw new IllegalStateException(Debug.stateToString(stack));
    }

    @CodeHistory(date = "2026/4/1")
    protected static class ByteArray {

        protected static final byte[] NULL = "null".getBytes(StandardCharsets.US_ASCII);

        protected static final byte[] TRUE = "true".getBytes(StandardCharsets.US_ASCII);

        protected static final byte[] FALSE = "false".getBytes(StandardCharsets.US_ASCII);

        private ByteArray() {
            super();
        }
    }
}
