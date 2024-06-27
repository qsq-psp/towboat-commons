package indi.qsq.json.reflect;

import indi.qsq.json.api.JsonConsumer;
import indi.qsq.json.api.ParseHint;
import indi.qsq.json.entity.JsonConstant;
import indi.qsq.json.value.BooleanValueSerializer;

/**
 * Created in infrastructure on 2021/12/31.
 * Created on 2022/7/16.
 */
class JsonBooleanType extends JsonType {

    JsonBooleanType() {
        super();
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public JsonBooleanType clone() {
        final JsonBooleanType that = new JsonBooleanType();
        that.setJsonType(this);
        return that;
    }

    @Override
    protected void serializeValue(String key, Object value, JsonConsumer jc, JsonSerializer js) {
        BooleanValueSerializer.INSTANCE.serialize(key, (Boolean) value, jc, this, js);
    }

    @Override
    protected Object parseNumber(long value, JsonConverter jv) {
        if (anyParseConfig(ParseHint.ACCEPT_NUMBER)) {
            return value != 0L;
        } else {
            return JsonConstant.UNDEFINED;
        }
    }

    @Override
    protected Object parseNumber(double value, JsonConverter jv) {
        if (anyParseConfig(ParseHint.ACCEPT_NUMBER)) {
            return value != 0.0;
        } else {
            return JsonConstant.UNDEFINED;
        }
    }

    @Override
    protected Object parseString(String value, JsonConverter jv) {
        if (anyParseConfig(ParseHint.ACCEPT_STRING)) {
            return !value.isEmpty();
        } else {
            return JsonConstant.UNDEFINED;
        }
    }

    @Override
    public String typeName() {
        return "boolean";
    }
}
