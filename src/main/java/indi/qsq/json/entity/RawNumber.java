package indi.um.json.entity;

import org.jetbrains.annotations.NotNull;

/**
 * Created on 2022/6/4, named RawDecimal.
 * Renamed on 2023/5/5.
 */
public class RawNumber extends Number {

    private static final long serialVersionUID = 0x6E78B6E4E0A6EEF9L;

    @NotNull
    public final String value;

    public RawNumber(@NotNull String value) {
        super();
        this.value = value;
    }

    @Override
    public int intValue() {
        return Integer.parseInt(value);
    }

    @Override
    public long longValue() {
        return Long.parseLong(value);
    }

    @Override
    public float floatValue() {
        return Float.parseFloat(value);
    }

    @Override
    public double doubleValue() {
        return Double.parseDouble(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof RawNumber && this.value.equals(((RawNumber) obj).value));
    }

    @Override
    public String toString() {
        return value;
    }
}
