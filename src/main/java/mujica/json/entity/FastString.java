package mujica.json.entity;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@CodeHistory(date = "2022/6/1", project = "Ultramarine", name = "TypedString")
@CodeHistory(date = "2025/10/11")
public class FastString implements Serializable {

    private static final long serialVersionUID = 0xe6acf0939adaa8c6L;

    @NotNull
    public final String string;

    public FastString(@NotNull String string) {
        super();
        this.string = string;
    }

    @Override
    public int hashCode() {
        return string.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof FastString && this.string.equals(((FastString) obj).string));
    }

    @Override
    public String toString() {
        return string;
    }
}
