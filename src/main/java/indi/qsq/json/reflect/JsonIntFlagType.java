package indi.qsq.json.reflect;

import indi.qsq.json.api.*;
import indi.qsq.json.entity.JsonArray;
import indi.qsq.json.entity.JsonConstant;
import indi.qsq.json.entity.JsonObject;
import indi.qsq.util.text.NumberText;
import indi.qsq.util.text.Quote;
import indi.qsq.util.text.SplitBy;
import indi.qsq.util.value.FlagName;
import io.netty.util.internal.InternalThreadLocalMap;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created in coo on 2019/11/12, named FlagInterpreter.
 * Recreated in webbiton on 2020/12/24, named RegistryStringToIntField.
 * Recreated in infrastructure on 2022/1/28.
 * Recreated on 2022/8/5.
 */
class JsonIntFlagType extends JsonIntegralType {

    private static final long serialVersionUID = 0x4EF233436883023EL;

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonIntFlagType.class);

    FlagName flagName;

    FlagStyle flagStyle;

    boolean copy;

    JsonIntFlagType() {
        super();
    }

    @Override
    protected void collectAnnotation(@NotNull AnnotatedElement element, int layer) {
        if (this.flagName != null) {
            return;
        }
        final IntFlagValue intFlagValue = element.getAnnotation(IntFlagValue.class);
        if (intFlagValue != null) {
            collectAnnotation(intFlagValue);
            return;
        }
        final AnyFlag anyFlag = element.getAnnotation(AnyFlag.class);
        if (anyFlag != null) {
            collectAnnotation(anyFlag);
        }
    }

    void collectAnnotation(IntFlagValue intFlagValue) {
        final FlagName flagName = new FlagName();
        final int[] integers = intFlagValue.integers();
        final String[] strings = intFlagValue.strings();
        final int length = Math.min(integers.length, strings.length);
        for (int index = 0; index < length; index++) {
            flagName.addFlag(integers[index], strings[index]);
        }
        this.flagName = flagName;
        this.flagStyle = intFlagValue.style();
    }

    void collectAnnotation(AnyFlag anyFlag) {
        final FlagName flagName = JsonParser.dereference(anyFlag.value(), FlagName.class);
        if (flagName != null) {
            if (anyFlag.copy()) {
                this.flagName = new FlagName(flagName);
                this.copy = true;
            } else {
                this.flagName = flagName;
                this.copy = false;
            }
        } else {
            LOGGER.warn("Fail to dereference {}", Quote.DEFAULT.apply(anyFlag.value()));
            this.flagName = new FlagName();
            this.copy = false;
        }
        this.flagStyle = anyFlag.style();
    }

    @Override
    protected void serializeValue(String key, Object value, JsonConsumer jc, JsonSerializer js) {
        final int flags = (int) value;
        if (flags == 0) {
            if (anySerializeConfig(SerializeFrom.ZERO_INTEGRAL, true)) {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize zero int flag to undefined in {}", js);
                }
                return;
            }
            if (anySerializeConfig(SerializeFrom.ZERO_INTEGRAL, false)) {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize zero int flag to null in {}", js);
                }
                serializeNull(key, jc);
                return;
            }
        }
        jc.optionalKey(key);
        switch (flagStyle) {
            case CONCAT_COMMA:
                jc.stringValue(flagName.stringify(flags, ","));
                break;
            case CONCAT_COMMA_SPACE:
                jc.stringValue(flagName.stringify(flags, ", "));
                break;
            case CONCAT_SPACE:
                jc.stringValue(flagName.stringify(flags, " "));
                break;
            case CONCAT_LINE_FEED:
                jc.stringValue(flagName.stringify(flags, "\n"));
                break;
            case CONCAT_PLUS:
                jc.stringValue(flagName.stringify(flags, "+"));
                break;
            case CONCAT_PLUS_SPACE:
                jc.stringValue(flagName.stringify(flags, " + "));
                break;
            case CONCAT_AMPERSAND:
                jc.stringValue(flagName.stringify(flags, "&"));
                break;
            case CONCAT_AMPERSAND_SPACE:
                jc.stringValue(flagName.stringify(flags, " & "));
                break;
            case CONCAT_VERTICAL_BAR:
                jc.stringValue(flagName.stringify(flags, "|"));
                break;
            case CONCAT_VERTICAL_BAR_SPACE:
                jc.stringValue(flagName.stringify(flags, " | "));
                break;
            default:
            case JSON_ARRAY:
                flagName.toJsonArray(jc, flags, false);
                break;
            case JSON_ARRAY_WITH_REMAIN:
                flagName.toJsonArray(jc, flags, true);
                break;
            case JSON_OBJECT:
                flagName.toJsonObject(jc, flags, false);
                break;
            case JSON_OBJECT_WITH_REMAIN:
                flagName.toJsonObject(jc, flags, true);
                break;
        }
    }

    @Override
    protected ConverterFrame frame(Object self, Getter read, boolean isObject, JsonConverter jv) {
        if (isObject) {
            return new FlagObjectFrame();
        } else {
            return new FlagArrayFrame();
        }
    }

    class FlagObjectFrame extends ConverterFrame {

        int flags;

        int shift;

        @Override
        protected Object finish(JsonParser jp) {
            return flags;
        }

        @Override
        protected void key(String key, JsonParser jp) {
            shift = flagName.getShift(key);
        }

        @Override
        protected void booleanValue(boolean value, JsonConverter jv) {
            if (shift == -1) {
                return;
            }
            if (value) {
                flags |= 1 << shift;
            } else {
                flags &= ~(1 << shift);
            }
            shift = -1;
        }

        @Override
        protected void numberValue(long value, JsonConverter jv) {
            if (shift == -1 || !anyParseConfig(ParseHint.ACCEPT_NUMBER)) {
                return;
            }
            flags |= value << shift;
            shift = -1;
        }
    }

    class FlagArrayFrame extends ConverterFrame {

        int flags;

        @Override
        protected Object finish(JsonParser jp) {
            return flags;
        }

        @Override
        protected void numberValue(long value, JsonConverter jv) {
            if (anyParseConfig(ParseHint.ACCEPT_NUMBER)) {
                flags |= value;
            }
        }

        @Override
        protected void stringValue(String value, JsonConverter jv) {
            final int flag = flagName.getFlag(value);
            if (flag != -1) {
                flags |= flag;
            }
        }
    }

    @Override
    protected Object parseNumber(long value, JsonConverter jv) {
        if (anyParseConfig(ParseHint.ACCEPT_NUMBER)) {
            if (anyParseConfig(ParseHint.APPLY_TRUNCATE) || Integer.MIN_VALUE <= value && value <= Integer.MAX_VALUE) {
                return (int) value;
            } else if (jv.logEnabled()) {
                LOGGER.debug("Discard outbound long value {} while parsing int-flag in {}", value, jv);
            }
        } else if (jv.logEnabled()) {
            LOGGER.debug("Discard long value {} while parsing int-flag in {}", value, jv);
        }
        return JsonConstant.UNDEFINED;
    }

    @Override
    protected Object parseNumber(double value, JsonConverter jv) {
        if (Double.isFinite(value)) {
            if (anyParseConfig(ParseHint.APPLY_ROUND_ZERO)) {
                return parseNumber((long) value, jv);
            }
            if (anyParseConfig(ParseHint.APPLY_ROUND_FLOOR)) {
                return parseNumber((long) Math.floor(value), jv);
            }
            if (anyParseConfig(ParseHint.APPLY_ROUND_CEIL)) {
                return parseNumber((long) Math.ceil(value), jv);
            }
            if (anyParseConfig(ParseHint.APPLY_ROUND_NEAR)) {
                return parseNumber(Math.round(value), jv);
            }
        }
        if (jv.logEnabled()) {
            LOGGER.debug("Fail to parse double value {} to int-flag in {}", value, jv);
        }
        return JsonConstant.UNDEFINED;
    }

    /**
     * Done in 2023/4/3.
     */
    @Override
    protected Object parseString(String value, JsonConverter jv) {
        Iterable<String> iterable = null;
        boolean trim = true;
        int flags = 0;
        switch (flagStyle) {
            case CONCAT_COMMA:
                trim = false;
                // no break here
            case CONCAT_COMMA_SPACE:
                iterable = new SplitBy.SeparatorChar(value, ',');
                break;
            case CONCAT_SPACE:
                iterable = new SplitBy.SeparatorChar(value, ' ');
                break;
            case CONCAT_LINE_FEED:
                trim = false;
                iterable = new SplitBy.CRLF(value);
                break;
            case CONCAT_PLUS:
                trim = false;
                // no break here
            case CONCAT_PLUS_SPACE:
                iterable = new SplitBy.SeparatorChar(value, '+');
                break;
            case CONCAT_AMPERSAND:
                trim = false;
                // no break here
            case CONCAT_AMPERSAND_SPACE:
                iterable = new SplitBy.SeparatorChar(value, '&');
                break;
            case CONCAT_VERTICAL_BAR:
                trim = false;
                // no break here
            case CONCAT_VERTICAL_BAR_SPACE:
                iterable = new SplitBy.SeparatorChar(value, '|');
                break;
            case JSON_ARRAY:
                trim = false;
                // this variable acts as remain flag temporarily; no break here
            case JSON_ARRAY_WITH_REMAIN: {
                try {
                    JsonArray jsonArray = (JsonArray) (new JsonParser()).parse(value);
                    ArrayList<String> flagList = InternalThreadLocalMap.get().arrayList();
                    for (Object item : jsonArray) {
                        if (item instanceof String) {
                            flagList.add((String) item);
                        } else if (trim && item instanceof Integer) {
                            flags |= (Integer) item;
                        } else {
                            LOGGER.debug(
                                    "Fail to recognize item {} in json array {} to int-flag in {}",
                                    item, Quote.DEFAULT.apply(value),
                                    jv
                            );
                        }
                    }
                    iterable = flagList;
                } catch (RuntimeException e) {
                    LOGGER.debug("Fail to parse json array {} to int-flag in {}", Quote.DEFAULT.apply(value), jv);
                }
                trim = false;
                break;
            }
            case JSON_OBJECT:
                trim = false;
                // this variable acts as remain flag temporarily; no break here
            case JSON_OBJECT_WITH_REMAIN: {
                try {
                    JsonObject jsonObject = (JsonObject) (new JsonParser()).parse(value);
                    ArrayList<String> flagList = InternalThreadLocalMap.get().arrayList();
                    for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                        if (Boolean.TRUE.equals(entry.getValue())) {
                            flagList.add(entry.getKey());
                        } else if (trim && "remain".equals(entry.getKey()) && entry.getValue() instanceof Integer) {
                            flags |= (Integer) entry.getValue();
                        } else {
                            LOGGER.debug(
                                    "Fail to recognize entry {}={} in json array {} to int-flag in {}",
                                    Quote.DEFAULT.apply(entry.getKey()), entry.getValue(),
                                    Quote.DEFAULT.apply(value),
                                    jv
                            );
                        }
                    }
                    iterable = flagList;
                } catch (RuntimeException e) {
                    LOGGER.debug("Fail to parse json object {} to int-flag in {}", Quote.DEFAULT.apply(value), jv);
                }
                trim = false;
                break;
            }
        }
        if (iterable != null) {
            int index = 0;
            for (String name : iterable) {
                if (trim) {
                    name = name.trim();
                }
                int flag = flagName.getFlag(name);
                if (flag == -1) {
                    if (jv.logEnabled()) {
                        LOGGER.debug(
                                "Fail to parse name[{}]={} in string value {} to int-flag in {}",
                                NumberText.INSTANCE.ordinalToString(index),
                                Quote.DEFAULT.apply(name),
                                Quote.DEFAULT.apply(value),
                                jv
                        );
                    }
                } else {
                    flags |= flag;
                }
                index++;
            }
        }
        return flags;
    }
}
