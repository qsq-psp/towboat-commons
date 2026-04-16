package mujica.json.reflect;

import mujica.json.entity.FastString;
import mujica.json.entity.JsonHandlerAdapter;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/12/20")
class JsonParserStack extends JsonHandlerAdapter<NopFrame> {

    public JsonParserStack(@NotNull NopFrame frame) {
        super(frame);
    }

    @Override
    protected void afterValue() {
        h.shape = null;
        h.setKey(null);
    }

    @Override
    public void openArray() {
        h.shape = NopFrame.StructureShape.ARRAY;
        h = h.open();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void closeArray() {
        h.parent.structureValue(h.close());
        h = h.parent;
        afterValue();
    }

    @Override
    public void openObject() {
        h.shape = NopFrame.StructureShape.OBJECT;
        h = h.open();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void closeObject() {
        h.parent.structureValue(h.close());
        h = h.parent;
        afterValue();
    }

    @Override
    public void stringKey(@NotNull String key) {
        h.setKey(key);
    }

    @Override
    public void stringKey(@NotNull FastString key) {
        h.setKey(key);
    }
}
