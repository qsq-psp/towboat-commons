package indi.qsq.json.reflect;

import indi.qsq.json.api.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedElement;

/**
 * Created on 2022/8/5.
 */
class JsonIntTypeBuilder extends JsonType {

    private static final long serialVersionUID = 0x0762CF238E4E01F7L;

    JsonType builtType;

    JsonIntTypeBuilder() {
        super();
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public JsonIntTypeBuilder clone() {
        final JsonIntTypeBuilder that = new JsonIntTypeBuilder();
        that.setJsonType(this);
        return that;
    }

    @Override
    protected void collectAnnotation(@NotNull AnnotatedElement element, int layer) {
        if (builtType instanceof JsonIntEnumType) {
            builtType.collectAnnotation(element, layer);
        } else {
            IntEnumValue intEnumValue = element.getAnnotation(IntEnumValue.class);
            if (intEnumValue != null) {
                JsonIntEnumType type = new JsonIntEnumType();
                type.setJsonType(this);
                type.collectAnnotation(intEnumValue);
                builtType = type;
                return;
            }
            AnyEnum anyEnum = element.getAnnotation(AnyEnum.class);
            if (anyEnum != null) {
                JsonIntEnumType type = new JsonIntEnumType();
                type.setJsonType(this);
                type.collectAnnotation(anyEnum);
                builtType = type;
                return;
            }
            if (builtType instanceof JsonIntFlagType) {
                builtType.collectAnnotation(element, layer);
            } else {
                IntFlagValue intFlagValue = element.getAnnotation(IntFlagValue.class);
                if (intFlagValue != null) {
                    JsonIntFlagType type = new JsonIntFlagType();
                    type.setJsonType(this);
                    type.collectAnnotation(intFlagValue);
                    builtType = type;
                    return;
                }
                AnyFlag anyFlag = element.getAnnotation(AnyFlag.class);
                if (anyFlag != null) {
                    JsonIntFlagType type = new JsonIntFlagType();
                    type.setJsonType(this);
                    type.collectAnnotation(anyFlag);
                    builtType = type;
                    return;
                }
                if (builtType instanceof JsonIntType) {
                    builtType.collectAnnotation(element, layer);
                } else {
                    IntValue intValue = element.getAnnotation(IntValue.class);
                    if (intValue != null) {
                        JsonIntType type = new JsonIntType();
                        type.setJsonType(this);
                        type.collectAnnotation(intValue);
                        builtType = type;
                    }
                }
            }
        }
    }

    @Override
    protected JsonType finishCollection() {
        if (builtType == null) {
            builtType = new JsonIntType();
            builtType.setJsonType(this);
        }
        return builtType;
    }

    @Override
    public String typeName() {
        return "int-builder";
    }
}
