package indi.qsq.json.reflect;

import indi.qsq.json.api.FrameStructure;
import indi.qsq.json.entity.JsonArray;
import indi.qsq.json.entity.JsonObject;

/**
 * Created on 2022/7/31.
 */
public abstract class MergeFrame extends ConverterFrame {

    @Override
    protected ConverterFrame openValue(boolean isObject, JsonConverter jv) {
        FrameStructure structure;
        if (isObject) {
            structure = new JsonObject();
        } else {
            structure = new JsonArray();
        }
        return structure.frame(isObject, jv);
    }

    @Override
    protected void closeValue(Object value, boolean isObject, JsonConverter jv) {
        anyValue(value, jv);
    }

    @Override
    protected void nullValue(JsonConverter jv) {
        anyValue(null, jv);
    }

    @Override
    protected void booleanValue(boolean value, JsonConverter jv) {
        anyValue(value, jv);
    }

    @Override
    protected void numberValue(long value, JsonConverter jv) {
        anyValue(value, jv);
    }

    @Override
    protected void numberValue(double value, JsonConverter jv) {
        anyValue(value, jv);
    }

    @Override
    protected void stringValue(String value, JsonConverter jv) {
        anyValue(value, jv);
    }

    protected abstract void anyValue(Object value, JsonConverter jv);

    @Override
    public String toString() {
        return "MergeFrame[NOP]";
    }
}
