package indi.qsq.json.reflect;

import indi.qsq.json.api.JsonConsumer;
import indi.qsq.json.api.JsonStructure;

/**
 * Created on 2022/7/26.
 */
class JsonStructureObjectType extends JsonObjectType {

    private static final long serialVersionUID = 0xA977D74134DF1013L;

    JsonStructureObjectType() {
        super();
    }

    @Override
    protected void serializeValue(String key, Object value, JsonConsumer jc, JsonSerializer js) {
        jc.optionalKey(key);
        ((JsonStructure) value).toJson(jc);
    }

    @Override
    public String typeName() {
        return "structure";
    }
}
