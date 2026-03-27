package mujica.json.io;

import mujica.ds.of_int.list.CopyOnResizeIntList;
import mujica.json.entity.JsonHandler;
import mujica.json.entity.StructureChecked;
import mujica.reflect.modifier.CodeHistory;
import mujica.text.format.CharSequenceAppender;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CodeHistory(date = "2021/12/30", project = "infrastructure", name = "JsonWriter")
@CodeHistory(date = "2022/6/4", project = "Ultramarine", name = "Writer")
@CodeHistory(date = "2026/1/6")
public abstract class JsonWriter extends JsonHandler implements StructureChecked {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonWriter.class);

    protected static final int STATE_START = 0;
    protected static final int STATE_END = 1;
    protected static final int STATE_NEW_ARRAY = 2;
    protected static final int STATE_ARRAY = 3;
    protected static final int STATE_NEW_OBJECT = 4;
    protected static final int STATE_OBJECT = 5;
    protected static final int STATE_KEY = 6;
    protected static final int STATE_JSONP = 7;

    protected final CopyOnResizeIntList stack = new CopyOnResizeIntList(null);

    protected JsonWriter() {
        super();
        stack.offerLast(STATE_START);
    }

    public void reset() {
        stack.clear();
        stack.offerLast(STATE_START);
    }

    private static final String[] STATE_NAMES = {
            "start", "end", "new-array", "array", "new-object", "object", "key", "jsonp"
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
    protected String stateToString() {
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

    protected void throwState() throws IllegalStateException {
        throw new IllegalStateException(stateToString());
    }

    public void openJsonp(@NotNull CharSequence name) {
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn("openJsonp({})", CharSequenceAppender.Json.INSTANCE.stringify(name, (StringBuilder) null));
        }
    }

    public void closeJsonp() {
        LOGGER.warn("closeJsonp()");
    }
}
