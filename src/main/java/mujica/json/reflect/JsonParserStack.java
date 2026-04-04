package mujica.json.reflect;

import mujica.json.entity.FastString;
import mujica.json.entity.JsonHandlerAdapter;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/12/20")
class JsonParserStack extends JsonHandlerAdapter<JsonParserFrame> {

    public JsonParserStack(JsonParserFrame h) {
        super(h);
    }

    @Override
    protected void afterValue() {
        h.topType = null;
        h.key = null;
    }

    @Override
    public void openArray() {
        h.topType = JsonParserFrame.StructureType.ARRAY;
        h = h.open();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void closeArray() {
        h.bottom.structureValue(h.close());
        h = h.bottom;
        afterValue();
    }

    @Override
    public void openObject() {
        h.topType = JsonParserFrame.StructureType.OBJECT;
        h = h.open();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void closeObject() {
        h.bottom.structureValue(h.close());
        h = h.bottom;
        afterValue();
    }

    @Override
    public void stringKey(@NotNull String key) {
        h.key = key;
    }

    @Override
    public void stringKey(@NotNull FastString key) {
        h.key = key;
    }
}
