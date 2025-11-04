package mujica.json.entity;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2022/6/4", project = "Ultramarine", name = "RawDecimal")
@CodeHistory(date = "2023/5/5", project = "Ultramarine", name = "RawNumber")
@CodeHistory(date = "2025/10/11")
public class FastNumber extends Number {

    private static final long serialVersionUID = 0x6e78b6e4e0a6eef9L;

    @NotNull
    public final String value;

    public FastNumber(@NotNull String value) {
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
        return this == obj || (obj instanceof FastNumber && this.value.equals(((FastNumber) obj).value));
    }

    @Override
    public String toString() {
        return value;
    }
}
