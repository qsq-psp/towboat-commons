package indi.um.json.reflect;

import indi.um.json.entity.JsonConstant;
import indi.um.json.io.RecursiveReader;

import java.util.function.Consumer;

/**
 * Created in webbiton on 2020/12/27, named JsonLayerHandler.
 * Created in webbiton on 2021/3/23, named JsonDummyLayer.
 * Recreated in va on 2021/12/28, named JsonParser.Frame.
 * Recreated in infrastructure on 2022/1/3, named ParserFrame.
 * Recreated on 2022/6/11.
 */
public class ConverterFrame implements Consumer<RecursiveReader> {

    public static final ConverterFrame INSTANCE = new ConverterFrame();

    protected ConverterFrame() {
        super();
    }

    protected ConverterFrame start(Object object) {
        return this;
    }

    protected Object finish(JsonParser jp) {
        return JsonConstant.UNDEFINED;
    }

    protected void key(String key, JsonParser jp) {
        // pass
    }

    protected ConverterFrame openValue(boolean isObject, JsonConverter jv) {
        return INSTANCE;
    }

    protected void closeValue(Object value, boolean isObject, JsonConverter jv) {
        // pass
    }

    protected void nullValue(JsonConverter jv) {
        // pass
    }

    protected void booleanValue(boolean value, JsonConverter jv) {
        // pass
    }

    protected void numberValue(long value, JsonConverter jv) {
        // pass
    }

    protected void numberValue(double value, JsonConverter jv) {
        // pass
    }

    protected void stringValue(String value, JsonConverter jv) {
        // pass
    }

    @Override
    public void accept(RecursiveReader reader) {
        // pass
    }

    @Override
    public String toString() {
        return "ConverterFrame[NOP]";
    }
}
