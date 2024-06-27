package indi.qsq.json.reflect;

import indi.qsq.json.api.*;
import indi.qsq.json.entity.JsonConstant;
import indi.qsq.util.text.Quote;
import indi.qsq.util.value.EnumMapping;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.AnnotatedElement;
import java.util.Objects;

/**
 * Created on 2022/10/7.
 */
class JsonBooleanEnumType extends JsonBooleanType {

    private static final long serialVersionUID = 0xA7197DBF71589F63L;

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonBooleanEnumType.class);

    EnumMapping mapping;

    boolean copy;

    JsonBooleanEnumType() {
        super();
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public JsonBooleanEnumType clone() {
        final JsonBooleanEnumType that = new JsonBooleanEnumType();
        that.setJsonType(this);
        if (copy) {
            if (this.mapping != null) {
                that.mapping = new EnumMapping(this.mapping);
            }
        } else {
            that.mapping = this.mapping;
        }
        that.copy = this.copy;
        return that;
    }

    @Override
    protected void collectAnnotation(@NotNull AnnotatedElement element, int layer) {
        if (this.mapping != null) {
            return;
        }
        final BooleanEnumValue booleanEnumValue = element.getAnnotation(BooleanEnumValue.class);
        if (booleanEnumValue != null) {
            collectAnnotation(booleanEnumValue);
        }
        final AnyEnum anyEnum = element.getAnnotation(AnyEnum.class);
        if (anyEnum != null) {
            collectAnnotation(anyEnum);
        }
    }

    void collectAnnotation(BooleanEnumValue booleanEnumValue) {
        final EnumMapping mapping = new EnumMapping(booleanEnumValue.caseInsensitive());
        mapping.add(booleanEnumValue.falseString(), 0);
        mapping.add(booleanEnumValue.trueString(), 1);
        this.mapping = mapping;
    }

    void collectAnnotation(AnyEnum anyEnum) {
        final EnumMapping mapping = JsonParser.dereference(anyEnum.value(), EnumMapping.class);
        if (mapping != null) {
            if (anyEnum.copy()) {
                this.mapping = new EnumMapping(mapping);
                this.copy = true;
            } else {
                this.mapping = mapping;
                this.copy = false;
            }
        } else {
            LOGGER.warn("Fail to dereference {}", Quote.DEFAULT.apply(anyEnum.value()));
            this.mapping = new EnumMapping();
            this.copy = false;
        }
    }

    @Override
    protected void serializeValue(String key, Object value, JsonConsumer jc, JsonSerializer js) {
        final boolean booleanValue = (Boolean) value;
        if (!booleanValue && anySerializeConfig((SerializeFrom.FALSE << UNDEFINED_SHIFT) | SerializeFrom.FALSE)) {
            if (anySerializeConfig(SerializeFrom.FALSE, true)) {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize false boolean to undefined in {}", js);
                }
            } else if (anySerializeConfig(SerializeFrom.FALSE, false)) {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize false boolean to null in {}", js);
                }
                jc.optionalKey(key);
                jc.nullValue();
            }
            return;
        }
        final int intValue = booleanValue ? 1 : 0;
        if (anySerializeConfig((SerializeFrom.DEFAULT_ENUM << UNDEFINED_SHIFT) | SerializeFrom.DEFAULT_ENUM) && Objects.equals(mapping.getDefaultValue(), intValue)) {
            if (anySerializeConfig(SerializeFrom.DEFAULT_ENUM, true)) {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize default enum {} to undefined in {}", value, js);
                }
            } else {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize default enum {} to undefined in {}", value, js);
                }
                serializeNull(key, jc);
            }
            return;
        }
        Object string = mapping.get(intValue);
        if (string instanceof String) {
            jc.optionalKey(key);
            jc.stringValue((String) string);
        } else if (anySerializeConfig((SerializeFrom.OUT_ENUM << UNDEFINED_SHIFT) | SerializeFrom.OUT_ENUM)) {
            if (anySerializeConfig(SerializeFrom.OUT_ENUM, true)) {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize out enum {} to undefined in {}", value, js);
                }
            } else {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize out enum {} to null in {}", value, js);
                }
                serializeNull(key, jc);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    protected Object parseString(String value, JsonConverter jv) {
        Object parsed = mapping.get(value);
        if (parsed instanceof Integer) {
            return ((Integer) parsed) != 0;
        }
        parsed = mapping.getDefaultValue();
        if (parsed instanceof Integer) {
            return ((Integer) parsed) != 0;
        }
        if (jv.logEnabled()) {
            LOGGER.debug("Fail to parse string value {} to boolean-enum in {}", Quote.DEFAULT.apply(value), jv);
        }
        return JsonConstant.UNDEFINED;
    }

    @Override
    public void toJsonEntries(@NotNull JsonConsumer jc) {
        super.toJsonEntries(jc);
        final EnumMapping map = this.mapping;
        if (map != null) {
            jc.key("map");
            map.toJson(jc);
        }
    }

    @Override
    public String typeName() {
        return "boolean-enum";
    }
}
