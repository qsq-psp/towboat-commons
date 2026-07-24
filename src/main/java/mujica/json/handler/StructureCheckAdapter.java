package mujica.json.handler;

import mujica.ds.i32.I32;
import mujica.ds.i32.I32Slot;
import mujica.ds.i32.SlotArrayAllocatorI32;
import mujica.ds.slot.CopyOnResizeSlotList;
import mujica.json.container.FastString;
import mujica.json.io.JsonWriter;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2023/4/29", project = "Ultramarine", name = "StructureChecker")
@CodeHistory(date = "2026/4/2")
public class StructureCheckAdapter<H extends JsonHandler> extends JsonHandlerAdapter<H> {

    public static final int STATE_START = JsonWriter.STATE_START;
    public static final int STATE_END = JsonWriter.STATE_END;
    public static final int STATE_ARRAY = JsonWriter.STATE_ARRAY;
    public static final int STATE_OBJECT = JsonWriter.STATE_OBJECT;
    public static final int STATE_KEY = JsonWriter.STATE_KEY;

    protected final CopyOnResizeSlotList<I32Slot, int[]> stack = new CopyOnResizeSlotList<>(SlotArrayAllocatorI32.WithHashImpl.TWICE);

    protected final I32Slot slot = new I32();

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
        protected static String stateToString(@NotNull CopyOnResizeSlotList<I32Slot, int[]> stack, @NotNull I32Slot slot) {
            final StringBuilder sb = new StringBuilder();
            final int topState = slot.getI32();
            stack.forEach(tempSlot -> sb.append(stateToString(tempSlot.getI32())).append(", "), slot);
            slot.setI32(topState);
            return sb.append(stateToString(topState)).append("]").toString();
        }

        private Debug() {
            super();
        }
    }

    protected void throwState() throws IllegalStateException {
        throw new IllegalStateException(Debug.stateToString(stack, slot));
    }

    public StructureCheckAdapter(@NotNull H h) {
        super(h);
        slot.setI32(STATE_START);
    }

    public void reset() {
        stack.clear();
        slot.setI32(STATE_START);
    }

    @Override
    public boolean structureChecked() {
        return true;
    }

    protected void beforeKey() {
        if (slot.getI32() == STATE_OBJECT) {
            slot.setI32(STATE_KEY);
        } else {
            throwState();
        }
    }

    @Override
    protected void beforeValue() {
        switch (slot.getI32()) {
            case STATE_START:
                slot.setI32(STATE_END);
                break;
            case STATE_ARRAY:
                slot.setI32(STATE_ARRAY);
                break;
            case STATE_KEY:
                slot.setI32(STATE_OBJECT);
                break;
            default:
                throwState();
                break; // never
        }
    }

    @Override
    public void openArray() {
        beforeValue();
        stack.insertLastItem(slot);
        slot.setI32(STATE_ARRAY);
        h.openArray();
    }

    @Override
    public void closeArray() {
        h.closeArray();
        if (slot.getI32() != STATE_ARRAY) {
            throwState();
        }
        stack.removeLastItem(slot);
    }

    @Override
    public void openObject() {
        beforeValue();
        stack.insertLastItem(slot);
        slot.setI32(STATE_ARRAY);
        h.openObject();
    }

    @Override
    public void closeObject() {
        h.closeObject();
        if (slot.getI32() != STATE_ARRAY) {
            throwState();
        }
        stack.removeLastItem(slot);
    }

    @Override
    public void key(@NotNull String key) {
        beforeKey();
        h.key(key);
    }

    @Override
    public void key(@NotNull FastString key) {
        beforeKey();
        h.key(key);
    }
}
