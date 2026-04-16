package mujica.json.entity;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@CodeHistory(date = "2025/10/2", name = "AbstractKeyList")
@CodeHistory(date = "2026/4/3")
public interface JsonPath extends Serializable {

    @NotNull
    static JsonPathSegment of(int value) {
        return new JsonPathIndexSegment(value);
    }

    @NotNull
    static JsonPathSegment of(String value) {
        return new JsonPathNameSegment(value);
    }

    int length();

    @NotNull
    JsonPathSegment get(int index);
}
