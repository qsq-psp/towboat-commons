package mujica.json.reflect;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandlerAdapter;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/12/20", name = "JsonParserStack")
@CodeHistory(date = "2026/4/27")
class ParserStack extends JsonHandlerAdapter<NopFrame> {

    public ParserStack(@NotNull NopFrame frame) {
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
    public void key(@NotNull String key) {
        h.setKey(key);
    }

    @Override
    public void key(@NotNull FastString key) {
        h.setKey(key);
    }

    public void close() {
        while (true) {
            h.close();
            if (h.parent == null) {
                break;
            }
            h = h.parent;
        }
    }
}
