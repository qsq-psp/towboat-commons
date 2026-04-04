package mujica.json.reflect;

import mujica.json.entity.FastString;
import mujica.json.entity.JsonHandler;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

@CodeHistory(date = "2021/12/24", project = "va", name = "JsonClass")
@CodeHistory(date = "2021/12/30", project = "infrastructure", name = "ReflectedClass")
@CodeHistory(date = "2022/6/4", project = "Ultramarine", name = "ReflectedClass")
@CodeHistory(date = "2022/7/13", project = "Ultramarine", name = "ReflectClass")
@CodeHistory(date = "2026/3/27")
abstract class PlainObjectType extends JsonType implements Serializable {

    private static final long serialVersionUID = 0x09c112151b161872L;

    @Nullable
    abstract PlainObjectField fieldForName(@NotNull String name);

    @NotNull
    abstract Collection<PlainObjectField> fieldCollection();

    @CodeHistory(date = "2026/4/3")
    static class ListBased extends PlainObjectType {

        private static final long serialVersionUID = 0x2078cb85bea19a3bL;

        final ArrayList<PlainObjectField> list = new ArrayList<>();

        @Override
        PlainObjectField fieldForName(@NotNull String name) {
            for (PlainObjectField field : list) {
                if (name.equals(field.getName())) {
                    return field;
                }
            }
            return null;
        }

        @NotNull
        @Override
        Collection<PlainObjectField> fieldCollection() {
            return list;
        }
    }

    @CodeHistory(date = "2026/4/3")
    static class MapBased extends PlainObjectType {

        private static final long serialVersionUID = 0xf02a125f0b001a2bL;

        final LinkedHashMap<String, PlainObjectField> map = new LinkedHashMap<>();

        @Nullable
        @Override
        PlainObjectField fieldForName(@NotNull String name) {
            return map.get(name);
        }

        @NotNull
        @Override
        Collection<PlainObjectField> fieldCollection() {
            return map.values();
        }
    }

    // RandomOrder ...

    @NotNull
    @Override
    public JsonParserFrame createFrame(@NotNull JsonParserFrame bottom) {
        String name;
        if (bottom.key instanceof String) {
            name = (String) bottom.key;
        } else if (bottom.key instanceof FastString) {
            name = bottom.key.toString();
        } else {
            return new JsonParserFrame(bottom); // NOP frame
        }
        final PlainObjectField field = fieldForName(name);
        if (field == null) {
            return new JsonParserFrame(bottom); // NOP frame
        }
        bottom.key = null;
        return field.createFrame(bottom);
    }

    @Override
    public void serialize(Object in, @NotNull JsonHandler out, @NotNull JsonContext context) {
        for (PlainObjectField field : fieldCollection()) {
            field.serialize(in, out, context);
        }
    }
}
