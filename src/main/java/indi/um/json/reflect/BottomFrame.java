package indi.um.json.reflect;

import indi.um.json.api.FrameStructure;
import indi.um.json.entity.JsonArray;
import indi.um.json.entity.JsonConstant;
import indi.um.json.entity.JsonObject;

/**
 * Created in webbiton on 2020/12/23, named JsonObjectLayer.
 * Recreated in infrastructure on 2022/1/3.
 * Created on 2022/7/20.
 */
class BottomFrame extends MergeFrame {

    protected Object object;

    protected BottomFrame(Object object) {
        super();
        this.object = object;
    }

    @Override
    protected BottomFrame start(Object object) {
        this.object = object;
        return this;
    }

    @Override
    protected Object finish(JsonParser jp) {
        return object;
    }

    @Override
    protected ConverterFrame openValue(boolean isObject, JsonConverter jv) {
        if (object == null) {
            if (isObject) {
                object = new JsonObject();
            } else {
                object = new JsonArray();
            }
        }
        if (object instanceof FrameStructure) {
            return ((FrameStructure) object).frame(isObject, jv);
        } else {
            return ReflectClass.get(object.getClass()).frame(object, jv);
        }
    }

    @Override
    protected void anyValue(Object value, JsonConverter jv) {
        if (value == JsonConstant.UNDEFINED) {
            return;
        }
        object = value;
    }

    @Override
    public String toString() {
        return "BottomFrame[" + object + "]";
    }
}
