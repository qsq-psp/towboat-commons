package mujica.json.entity;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * Created on 2026/4/14.
 */
@CodeHistory(date = "2026/4/14")
public class CacheAdapter<H extends JsonHandler> extends JsonHandlerAdapter<H> {

    @NotNull
    HashMap<Object, Object> map;

    public CacheAdapter(@NotNull H h, @NotNull HashMap<Object, Object> map) {
        super(h);
        this.map = map;
    }

    public CacheAdapter(@NotNull H h) {
        this(h, new HashMap<>());
    }

    @NotNull
    public HashMap<Object, Object> getMap() {
        return map;
    }

    public void setMap(@NotNull HashMap<Object, Object> map) {
        this.map = map;
    }

    @Override
    public void stringKey(@NotNull String key) {
        if (testTypePreference(FLAG_DO_NOT_CACHE_STRING_KEY)) {
            h.stringKey(key);
            return;
        }
        final Object object = map.get(key);
        if (object instanceof String) {
            h.stringKey((String) object);
        } else if (object instanceof FastString) {
            h.stringKey((FastString) object);
        } else {
            map.put(key, key);
            h.stringKey(key);
        }
    }

    @Override
    public void stringKey(@NotNull FastString key) {
        if (testTypePreference(FLAG_DO_NOT_CACHE_FAST_STRING_KEY)) {
            h.stringKey(key);
            return;
        }
        final Object object = map.get(key.toString());
        if (object instanceof FastString) {
            h.stringKey((FastString) object);
        } else {
            map.put(key.toString(), key);
            h.stringKey(key);
        }
    }

    @Override
    public void stringValue(@NotNull CharSequence value) {
        beforeValue();
        if (testTypePreference(FLAG_DO_NOT_CACHE_STRING_VALUE) || !(value instanceof String)) {
            h.stringValue(value);
        } else {
            Object object = map.get(value);
            if (object instanceof String) {
                h.stringValue((String) object);
            } else if (object instanceof FastString) {
                h.stringValue((FastString) object);
            } else {
                map.put(value, value);
                h.stringValue(value);
            }
        }
        afterValue();
    }

    @Override
    public void stringValue(@NotNull FastString value) {
        beforeValue();
        if (testTypePreference(FLAG_DO_NOT_CACHE_FAST_STRING_VALUE)) {
            h.stringValue(value);
        } else {
            Object object = map.get(value.toString());
            if (object instanceof FastString) {
                h.stringValue((FastString) object);
            } else {
                map.put(value.toString(), value);
                h.stringValue(value);
            }
        }
        afterValue();
    }
}
