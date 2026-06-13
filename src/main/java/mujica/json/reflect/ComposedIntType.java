package mujica.json.reflect;

import mujica.ds.generic.set.CollectionConstant;
import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.modifier.JsonEmpty;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ConstantComposition;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@CodeHistory(date = "2020/12/24", project = "coo", name = "FlagInterpreter")
@CodeHistory(date = "2020/12/24", project = "webbiton", name = "RegistryStringToIntField")
@CodeHistory(date = "2021/12/31", project = "infrastructure", name = "JsonIntEnumType")
@CodeHistory(date = "2022/1/28", project = "Ultramarine", name = "JsonIntFlagType")
@CodeHistory(date = "2022/8/5", project = "Ultramarine", name = "JsonIntEnumType")
@CodeHistory(date = "2022/8/5", project = "Ultramarine", name = "JsonIntFlagType")
@CodeHistory(date = "2026/4/28", name = "ComposedIntType")
class ComposedIntType extends IntType {

    private static final long serialVersionUID = 0x773dde6d7b291dc4L;

    @NotNull
    final HashMap<Object, Object> map = new HashMap<>(); // (Integer -> String | FastString) | (String -> Integer)

    @NotNull
    ConstantComposition composition = ConstantComposition.NEVER;

    ComposedIntType(long flags) {
        super(flags);
    }

    @Override
    public void transform(Object in, @NotNull JsonHandler out, @NotNull JsonContext context) {
        from(in).to(out, context);
    }

    @Override
    void to(@NotNull JsonHandler out, @NotNull JsonContext context) {
        try {
            if (state == CollectionConstant.PRESENT) {
                switch (composition) {
                    case NEVER:
                        enumTo(out, context);
                        break;
                    case MULTIPLY:
                        factorTo(out, context);
                        break;
                    case AND:
                        reverseFlagTo(out, context);
                        break;
                    case OR:
                        flagTo(out, context);
                        break;
                    default:
                        context.getLogger().debug("composed int fail to resolve");
                        out.numberValue(value);
                        break;
                }
            } else if (state == CollectionConstant.EMPTY) {
                if ((flags & (((long) JsonEmpty.FROM_NULL) << UNDEFINED_SHIFT)) != 0L) {
                    context.getLogger().debug("null to undefined");
                    out.skippedValue();
                } else {
                    out.nullValue();
                }
            } else {
                throw new IllegalStateException();
            }
        } finally {
            state = CollectionConstant.UNDEFINED;
        }
    }

    void enumTo(@NotNull JsonHandler out, @NotNull JsonContext context) {
        final Object toString = map.get(value);
        if (toString instanceof FastString) {
            out.stringValue((FastString) toString);
        } else if (toString instanceof String) {
            out.stringValue((String) toString);
        } else {
            context.getLogger().debug("enum int fail to decompose");
            out.numberValue(value);
        }
    }

    void factorTo(@NotNull JsonHandler out, @NotNull JsonContext context) {
        if (value <= 0) {
            if (value == 0) {
                if ((flags & (((long) JsonEmpty.FROM_ZERO_INTEGRAL) << UNDEFINED_SHIFT)) != 0L) {
                    context.getLogger().debug("factor int to undefined");
                    out.skippedValue();
                    return;
                } else if ((flags & (((long) JsonEmpty.FROM_ZERO_INTEGRAL) << NULL_SHIFT)) != 0L) {
                    context.getLogger().debug("factor int to null");
                    out.nullValue();
                    return;
                }
            }
            out.numberValue(value);
            return;
        }
        boolean arrayNotOpened = true;
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            Object fromInteger = entry.getKey();
            if (!(fromInteger instanceof Integer)) {
                continue;
            }
            int fromInt = (int) fromInteger;
            if (fromInt <= 0) {
                continue;
            }
            Object toString = entry.getValue();
            if (!(toString instanceof FastString || toString instanceof String)) {
                continue;
            }
            if (fromInt != 1) {
                if (value % fromInt != 0) {
                    continue;
                }
                value /= fromInt;
            }
            if (arrayNotOpened) {
                out.openArray();
                arrayNotOpened = false;
            }
            if (toString instanceof FastString) {
                out.stringValue((FastString) toString);
            } else {
                out.stringValue(toString.toString());
            }
        }
        if (arrayNotOpened) {
            if ((flags & (((long) JsonEmpty.FROM_EMPTY_ARRAY) << UNDEFINED_SHIFT)) != 0L) {
                context.getLogger().debug("factor int to undefined");
                out.skippedValue();
            } else if ((flags & (((long) JsonEmpty.FROM_EMPTY_ARRAY) << NULL_SHIFT)) != 0L) {
                context.getLogger().debug("factor int to null");
                out.nullValue();
            } else {
                out.emptyArrayValue();
            }
            return;
        }
        out.closeArray();
    }

    void reverseFlagTo(@NotNull JsonHandler out, @NotNull JsonContext context) {
        if (value == -1) {
            if ((flags & (((long) JsonEmpty.FROM_EMPTY_ARRAY) << UNDEFINED_SHIFT)) != 0L) {
                context.getLogger().debug("reverse flag int to undefined");
                out.skippedValue();
            } else if ((flags & (((long) JsonEmpty.FROM_EMPTY_ARRAY) << NULL_SHIFT)) != 0L) {
                context.getLogger().debug("reverse flag int to null");
                out.nullValue();
            } else {
                out.emptyArrayValue();
            }
            return;
        }
        out.openArray();
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            Object fromInteger = entry.getKey();
            if (!(fromInteger instanceof Integer)) {
                continue;
            }
            int fromInt = (int) fromInteger;
            if (fromInt == -1) {
                continue;
            }
            if ((value | fromInt) != fromInt) {
                continue;
            }
            Object toString = entry.getValue();
            if (toString instanceof FastString) {
                value |= ~fromInt;
                out.stringValue((FastString) toString);
            } else if (toString instanceof String) {
                value |= ~fromInt;
                out.stringValue((String) toString);
            }
        }
        out.closeArray();
    }

    void flagTo(@NotNull JsonHandler out, @NotNull JsonContext context) {
        if (value == 0) {
            if ((flags & (((long) (JsonEmpty.FROM_ZERO_INTEGRAL | JsonEmpty.FROM_EMPTY_ARRAY)) << UNDEFINED_SHIFT)) != 0L) {
                context.getLogger().debug("zero flag int to undefined");
                out.skippedValue();
            } else if ((flags & (((long) (JsonEmpty.FROM_ZERO_INTEGRAL | JsonEmpty.FROM_EMPTY_ARRAY)) << NULL_SHIFT)) != 0L) {
                context.getLogger().debug("zero flag int to null");
                out.nullValue();
            } else {
                out.emptyArrayValue();
            }
            return;
        }
        out.openArray();
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            Object fromInteger = entry.getKey();
            if (!(fromInteger instanceof Integer)) {
                continue;
            }
            int fromInt = (int) fromInteger;
            if (fromInt == 0) {
                continue;
            }
            if ((value & fromInt) != fromInt) {
                continue;
            }
            Object toString = entry.getValue();
            if (toString instanceof FastString) {
                value &= ~fromInt;
                out.stringValue((FastString) toString);
            } else if (toString instanceof String) {
                value &= ~fromInt;
                out.stringValue((String) toString);
            }
        }
        if (value != 0) {
            out.numberValue(value);
        }
        out.closeArray();
    }
}
