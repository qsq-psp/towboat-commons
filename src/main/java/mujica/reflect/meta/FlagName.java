package mujica.reflect.meta;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Stable;
import mujica.text.escape.Quote;
import mujica.text.number.HexEncoder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

@CodeHistory(date = "2019/11/12", project = "coo", name = "FlagInterpreter")
@CodeHistory(date = "2022/6/12", project = "Ultramarine")
@CodeHistory(date = "2025/3/12")
@Stable(date = "2025/7/25")
@SuppressWarnings("UnusedReturnValue")
public class FlagName implements Cloneable, Serializable {

    private static final long serialVersionUID = 0xd8005bb2bdc23ed1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(FlagName.class);

    @NotNull
    protected String[] array;

    public FlagName() {
        super();
        this.array = new String[8];
    }

    public FlagName(@NotNull FlagName that) {
        super();
        this.array = Arrays.copyOf(that.array, that.array.length);
    }

    @NotNull
    @SuppressWarnings({"CloneDoesntDeclareCloneNotSupportedException", "MethodDoesntCallSuperMethod"})
    @Override
    protected FlagName clone() {
        return new FlagName(this);
    }

    public static int shift8(int flag) {
        switch (flag) {
            default:
                return -1;
            case 1:
                return 0;
            case 1 << 1:
                return 1;
            case 1 << 2:
                return 2;
            case 1 << 3:
                return 3;
            case 1 << 4:
                return 4;
            case 1 << 5:
                return 5;
            case 1 << 6:
                return 6;
            case 1 << 7:
                return 7;
        }
    }

    public static int shift32(int flag) {
        if ((flag & (flag - 1)) == 0 && flag != 0) {
            if ((flag & 0x0000ffff) != 0) {
                if ((flag & 0x000000ff) != 0) {
                    return shift8(flag);
                } else {
                    return 8 + shift8(flag >>> 8);
                }
            } else {
                if ((flag & 0x00ff0000) != 0) {
                    return 16 + shift8(flag >>> 16);
                } else {
                    return 24 + shift8(flag >>> 24);
                }
            }
        }
        return -1;
    }

    public static int shift64(long flag) {
        final int low = shift32((int) flag);
        final int high = shift32((int) (flag >>> Integer.SIZE));
        if (high == -1) {
            return low;
        } else if (low == -1) {
            return Integer.SIZE + high;
        } else {
            return -1;
        }
    }

    private static final int MAX_SHIFT = 0xff; // 255

    @NotNull
    public FlagName addShift(int shift, String name) {
        if (0 <= shift && shift <= MAX_SHIFT) {
            int length = array.length;
            if (shift >= length) {
                do {
                    length <<= 1;
                } while (shift >= length);
                array = Arrays.copyOf(array, length);
            }
            if (array[shift] != null) {
                LOGGER.warn("Flag name {} override name {} shift {}",
                        Quote.DEFAULT.apply(name), Quote.DEFAULT.apply(array[shift]), shift);
            }
            array[shift] = name;
        } else {
            LOGGER.warn("Shift value {} out of range", shift);
        }
        return this;
    }

    @NotNull
    public FlagName addFlag(int flag, String name) {
        int shift = shift32(flag);
        if (shift != -1) {
            addShift(shift, name);
        }
        return this;
    }

    @NotNull
    public FlagName addFlag(long flag, String name) {
        int shift = shift64(flag);
        if (shift != -1) {
            addShift(shift, name);
        }
        return this;
    }

    private static final int INTERESTED_MODIFIER = Modifier.STATIC | Modifier.FINAL;

    private boolean testType(Class<?> clazz) {
        return int.class == clazz || long.class == clazz || Integer.class == clazz || Long.class == clazz;
    }

    @NotNull
    public FlagName addFromClass(@NotNull Class<?> clazz, @Nullable Predicate<String> nameFilter, @Nullable UnaryOperator<String> nameTransformer, boolean publicOnly) {
        int interestedModifier = INTERESTED_MODIFIER;
        if (publicOnly) {
            interestedModifier |= Modifier.PUBLIC;
        }
        for (Field field : clazz.getDeclaredFields()) {
            if ((field.getModifiers() & interestedModifier) == interestedModifier && testType(field.getType())) {
                int shift = -1;
                try {
                    field.setAccessible(true);
                    Object value = field.get(null);
                    if (value instanceof Integer) {
                        shift = shift32((Integer) value);
                    } else if (value instanceof Long) {
                        shift = shift64((Long) value);
                    } else {
                        LOGGER.warn("Unexpected type {} out of {}", value, field);
                    }
                } catch (ReflectiveOperationException e) {
                    LOGGER.warn("Fail to reflect on field {}", field, e);
                }
                if (shift == -1) {
                    continue;
                }
                String name = field.getName();
                if (nameFilter == null || nameFilter.test(name)) {
                    if (nameTransformer != null) {
                        name = nameTransformer.apply(name);
                    }
                    addShift(shift, name);
                }
            }
        }
        return this;
    }

    @NotNull
    public FlagName addFromClass(@NotNull Class<?> clazz, @Nullable Predicate<String> nameFilter, @Nullable UnaryOperator<String> nameTransformer) {
        return addFromClass(clazz, nameFilter, nameTransformer, true);
    }

    @NotNull
    public Iterable<Integer> shifts() {
        return new ShiftIterator();
    }

    private class ShiftIterator implements Iterable<Integer>, PrimitiveIterator.OfInt {

        private int index;

        @Override
        @NotNull
        public Iterator<Integer> iterator() {
            for (index = 0; index < array.length; index++) {
                if (array[index] != null) {
                    break;
                }
            }
            return this;
        }

        @Override
        public boolean hasNext() {
            return index < array.length;
        }

        @Override
        public int nextInt() {
            int value = index;
            for (index++; index < array.length; index++) {
                if (array[index] != null) {
                    break;
                }
            }
            return value;
        }
    }

    @NotNull
    public Iterable<Integer> flags() {
        return new FlagIterator();
    }

    private class FlagIterator extends ShiftIterator {

        @Override
        public int nextInt() {
            return 1 << super.nextInt();
        }
    }

    public String nameByShift(int shift) {
        if (0 <= shift && shift < array.length) {
            return array[shift];
        } else {
            return null;
        }
    }

    public int getShift(String name) {
        final int length = array.length;
        for (int index = 0; index < length; index++) {
            if (name.equals(array[index])) {
                return index;
            }
        }
        return -1;
    }

    public int getFlag(String name) {
        final int shift = getShift(name);
        if (shift != -1) {
            return 1 << shift;
        } else {
            return -1;
        }
    }

    public int stringify(@NotNull StringBuilder sb, int value, @NotNull String separator) {
        final int length = array.length;
        boolean subsequent = false;
        for (int index = 0; index < length; index++) {
            if (array[index] != null && (value & (1 << index)) != 0) {
                value &= ~(1 << index);
                if (subsequent) {
                    sb.append(separator);
                }
                sb.append(array[index]);
                subsequent = true;
            }
        }
        return value;
    }

    @NotNull
    public String stringify(int value, @NotNull String separator) {
        final StringBuilder sb = new StringBuilder();
        stringify(sb, value, separator);
        return sb.toString();
    }

    public void stringifyRound(int value, @NotNull StringBuilder out) {
        out.append('(');
        int remain = stringify(out, value, ", ");
        if (remain != 0) {
            out.append("; remain = 0x");
            HexEncoder.LOWER_ENCODER.hexMax32(out, value);
        }
        out.append(')');
    }
}
