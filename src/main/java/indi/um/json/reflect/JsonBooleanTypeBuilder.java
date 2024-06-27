package indi.um.json.reflect;

import indi.um.json.api.AnyEnum;
import indi.um.json.api.BooleanEnumValue;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedElement;

/**
 * Created on 2022/10/7.
 */
class JsonBooleanTypeBuilder extends JsonType {

    private static final long serialVersionUID = 0xED638114B060FEC4L;

    JsonType builtType;

    JsonBooleanTypeBuilder() {
        super();
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public JsonBooleanTypeBuilder clone() {
        final JsonBooleanTypeBuilder that = new JsonBooleanTypeBuilder();
        that.setJsonType(this);
        return that;
    }

    @Override
    protected void collectAnnotation(@NotNull AnnotatedElement element, int layer) {
        if (builtType instanceof JsonBooleanEnumType) {
            builtType.collectAnnotation(element, layer);
        } else {
            BooleanEnumValue booleanEnumValue = element.getAnnotation(BooleanEnumValue.class);
            if (booleanEnumValue != null) {
                JsonBooleanEnumType type = new JsonBooleanEnumType();
                type.setJsonType(this);
                type.collectAnnotation(booleanEnumValue);
                builtType = type;
            }
            AnyEnum anyEnum = element.getAnnotation(AnyEnum.class);
            if (anyEnum != null) {
                JsonBooleanEnumType type = new JsonBooleanEnumType();
                type.setJsonType(this);
                type.collectAnnotation(anyEnum);
                builtType = type;
            }
        }
    }

    @Override
    protected JsonType finishCollection() {
        if (builtType == null) {
            builtType = new JsonBooleanType();
            builtType.setJsonType(this);
        }
        return builtType;
    }

    @Override
    public String typeName() {
        return "boolean-builder";
    }
}
