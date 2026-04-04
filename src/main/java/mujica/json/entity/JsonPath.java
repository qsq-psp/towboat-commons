package mujica.json.entity;

import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/4/3.
 */
public interface JsonPath {

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
