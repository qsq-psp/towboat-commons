package mujica.json.entity;

import mujica.ds.of_int.list.CopyOnResizeIntList;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2023/4/29", project = "Ultramarine", name = "StructureChecker")
@CodeHistory(date = "2026/4/2")
public class StructureCheckAdapter<H extends JsonHandler> extends JsonHandlerAdapter<H> implements StructureChecked {

    protected final CopyOnResizeIntList stack = new CopyOnResizeIntList(null);

    @CodeHistory(date = "2026/4/13")
    private static class Debug {

        private static final String[] STATE_NAMES = {
                "start", "end", "array", "object", "key"
        };

        @NotNull
        protected static String stateToString(int state) {
            try {
                return STATE_NAMES[state];
            } catch (IndexOutOfBoundsException e) {
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

    public StructureCheckAdapter(@NotNull H h) {
        super(h);
        stack.offerLast(STATE_START);
    }

    public void reset() {
        stack.clear();
        stack.offerLast(STATE_START);
    }

    protected void beforeKey() {
        final int state = stack.removeLast();
        if (state == STATE_OBJECT) {
            stack.offerLast(STATE_KEY);
        } else {
            stack.offerLast(state);
            throwState();
        }
    }

    @Override
    protected void beforeValue() {
        final int state = stack.removeLast();
        switch (state) {
            case STATE_START:
                stack.offerLast(STATE_END);
                break;
            case STATE_ARRAY:
                stack.offerLast(STATE_ARRAY);
                break;
            case STATE_KEY:
                stack.offerLast(STATE_OBJECT);
                break;
            default:
                stack.offerLast(state);
                throwState();
                break; // never
        }
    }

    @Override
    public void openArray() {
        beforeValue();
        stack.offerLast(STATE_ARRAY);
        h.openArray();
    }

    @Override
    public void closeArray() {
        h.closeArray();
        final int state = stack.removeLast();
        if (state != STATE_ARRAY) {
            stack.offerLast(state);
            throwState();
        }
    }

    @Override
    public void openObject() {
        beforeValue();
        stack.offerLast(STATE_OBJECT);
        h.openObject();
    }

    @Override
    public void closeObject() {
        h.closeObject();
        final int state = stack.removeLast();
        if (state != STATE_OBJECT) {
            stack.offerLast(state);
            throwState();
        }
    }

    @Override
    public void stringKey(@NotNull String key) {
        beforeKey();
        h.stringKey(key);
    }

    @Override
    public void stringKey(@NotNull FastString key) {
        beforeKey();
        h.stringKey(key);
    }
}
