package mujica.json.reflect;

import mujica.ds.generic.set.CollectionConstant;
import mujica.json.entity.FastNumber;
import mujica.json.entity.JsonHandler;
import mujica.json.modifier.JsonHint;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;

/**
 * Created on 2026/4/15.
 */
@CodeHistory(date = "2026/4/15")
class FastNumberType extends JsonType {

    transient FastNumber value;

    FastNumberType(long flags) {
        super(flags);
    }

    @Override
    public int typePreference() {
        return FLAG_INTEGRAL_OVERFLOW_TO_RAW | FLAG_FRACTIONAL_OVERFLOW_TO_RAW | FLAG_INTEGRAL_FORCE_TO_RAW | FLAG_FRACTIONAL_FORCE_TO_RAW;
    }

    @NotNull
    @Override
    JsonType from(@Nullable Object object) {
        value = (FastNumber) object;
        state = CollectionConstant.PRESENT;
        return this;
    }

    @NotNull
    @Override
    JsonType from() {
        value = null;
        state = CollectionConstant.PRESENT;
        return this;
    }

    @NotNull
    @Override
    JsonType from(boolean newValue) {
        if ((flags & JsonHint.CAST_FROM_BOOLEAN) != 0L) {
            value = new FastNumber(newValue ? "1" : "0");
            state = CollectionConstant.PRESENT;
        } else {
            throw new ClassCastException("boolean to FastNumber");
        }
        return this;
    }

    @NotNull
    @Override
    JsonType from(int newValue) {
        value = new FastNumber(String.valueOf(newValue));
        state = CollectionConstant.PRESENT;
        return this;
    }

    @NotNull
    @Override
    JsonType from(long newValue) {
        value = new FastNumber(String.valueOf(newValue));
        state = CollectionConstant.PRESENT;
        return this;
    }

    @NotNull
    @Override
    JsonType from(float newValue) {
        value = new FastNumber(String.valueOf(newValue));
        state = CollectionConstant.PRESENT;
        return this;
    }

    @NotNull
    @Override
    JsonType from(double newValue) {
        value = new FastNumber(String.valueOf(newValue));
        state = CollectionConstant.PRESENT;
        return this;
    }

    @NotNull
    @Override
    JsonType from(@NotNull BigInteger newValue) {
        value = new FastNumber(newValue.toString());
        state = CollectionConstant.PRESENT;
        return this;
    }

    @NotNull
    @Override
    JsonType from(@NotNull FastNumber newValue) {
        value = newValue;
        state = CollectionConstant.PRESENT;
        return this;
    }

    @Override
    void to(@NotNull Setter setter, @Nullable Object self) throws Throwable {
        if (state == CollectionConstant.PRESENT) {
            FastNumber fastNumber = value;
            state = CollectionConstant.UNDEFINED;
            value = null;
            setter.set(self, fastNumber);
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    void to(@NotNull JsonHandler out, @NotNull JsonContext context) {
        if (state == CollectionConstant.PRESENT) {
            FastNumber fastNumber = value;
            state = CollectionConstant.UNDEFINED;
            value = null;
            out.numberValue(fastNumber);
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    FastNumber toObject(@NotNull JsonContext context) {
        if (state == CollectionConstant.PRESENT) {
            FastNumber fastNumber = value;
            state = CollectionConstant.UNDEFINED;
            value = null;
            return fastNumber;
        } else {
            throw new IllegalStateException();
        }
    }
}
