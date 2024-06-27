package indi.um.json.entity;

import indi.um.json.api.JsonConsumer;
import indi.um.util.ds.HeapBooleanBuf;
import indi.um.util.reflect.ClassUtility;
import indi.um.util.value.EnumMapping;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2023/4/29.
 */
public class StructureChecker implements JsonConsumer {

    public static JsonConsumer checked(JsonConsumer jc) {
        if (jc.checksStructure()) {
            return jc;
        } else {
            return new StructureChecker(jc);
        }
    }

    final JsonConsumer jc;

    static final int STATE_ARRAY = 0;
    static final int STATE_OBJECT = 1;
    static final int STATE_KEY = 2;

    static final EnumMapping STATE = (new EnumMapping())
            .addDefault("array", STATE_ARRAY)
            .add("object", STATE_OBJECT)
            .add("key", STATE_KEY);

    int state = STATE_ARRAY;

    final HeapBooleanBuf stack = new HeapBooleanBuf();

    public StructureChecker(JsonConsumer jc) {
        super();
        this.jc = jc;
    }

    public void finish() {
        if (!(state == STATE_ARRAY && stack.writerIndex() == 0)) {
            throw new IllegalStateException();
        }
    }

    @Override
    public void key(@NotNull String key) {
        if (state != STATE_OBJECT) {
            throw new IllegalStateException();
        }
        state = STATE_KEY;
        jc.key(key);
    }

    private void anyValue() {
        if (state == STATE_OBJECT) {
            throw new IllegalStateException();
        }
        if (state == STATE_KEY) {
            state = STATE_OBJECT;
        }
    }

    @Override
    public void jsonValue(@NotNull CharSequence json) {
        anyValue();
        jc.jsonValue(json);
    }

    @Override
    public void nullValue() {
        anyValue();
        jc.nullValue();
    }

    @Override
    public void booleanValue(boolean value) {
        anyValue();
        jc.booleanValue(value);
    }

    @Override
    public void numberValue(long value) {
        anyValue();
        jc.numberValue(value);
    }

    @Override
    public void numberValue(double value) {
        anyValue();
        jc.numberValue(value);
    }

    @Override
    public void numberValue(@NotNull RawNumber value) {
        anyValue();
        jc.numberValue(value);
    }

    @Override
    public void stringValue(@NotNull String value) {
        anyValue();
        jc.stringValue(value);
    }

    @Override
    public void openArray() {
        anyValue();
        stack.writeInt(state);
        state = STATE_ARRAY;
        jc.openArray();
    }

    @Override
    public void closeArray() {
        if (state != STATE_ARRAY) {
            throw new IllegalStateException();
        }
        state = stack.popInt();
        jc.closeArray();
    }

    @Override
    public void openObject() {
        anyValue();
        stack.writeInt(state);
        state = STATE_OBJECT;
        jc.openObject();
    }

    @Override
    public void closeObject() {
        if (state != STATE_OBJECT) {
            throw new IllegalStateException();
        }
        state = stack.popInt();
        jc.closeObject();
    }

    @Override
    public void arrayValue() {
        anyValue();
        jc.arrayValue();
    }

    @Override
    public void objectValue() {
        anyValue();
        jc.objectValue();
    }

    @Override
    public boolean checksStructure() {
        return true;
    }

    public void stringify(StringBuilder sb) {
        sb.append("state = ").append(STATE.forKey(state)).append(", stack = ");
        stack.stringifyReadable(sb);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(ClassUtility.normal(this)).append('[');
        stringify(sb);
        return sb.append(']').toString();
    }
}
