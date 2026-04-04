package mujica.json.reflect;

import mujica.ds.generic.set.CollectionConstant;
import mujica.json.entity.FastString;
import mujica.json.entity.JsonHandler;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@CodeHistory(date = "2021/12/30", project = "infrastructure", name = "Mapping")
@CodeHistory(date = "2022/6/3", project = "Ultramarine", name = "Mapping")
@CodeHistory(date = "2026/4/3")
abstract class PlainObjectField extends JsonType implements Serializable {

    private static final long serialVersionUID = 0xf952640a6a05d352L;

    @NotNull
    abstract String getName();

    abstract void nameAsKey(@NotNull JsonHandler jh);

    @CodeHistory(date = "2026/4/3")
    static class StringNamed extends PlainObjectField {

        private static final long serialVersionUID = 0x96974058A092C2EDL;

        @NotNull
        final String stringName;

        StringNamed(@NotNull String name) {
            super();
            this.stringName = name;
        }

        @Override
        @NotNull
        public String getName() {
            return stringName;
        }

        @Override
        void nameAsKey(@NotNull JsonHandler jh) {
            jh.stringKey(stringName);
        }
    }

    @CodeHistory(date = "2026/4/3")
    static class FastNamed extends PlainObjectField {

        private static final long serialVersionUID = 0x9E5702A3CA0288E2L;

        @NotNull
        final FastString fastName;

        FastNamed(@NotNull FastString name) {
            super();
            this.fastName = name;
        }

        @Override
        @NotNull
        public String getName() {
            return fastName.toString();
        }

        @Override
        void nameAsKey(@NotNull JsonHandler jh) {
            jh.stringKey(fastName);
        }
    }

    JsonType staticType;

    Getter getter;

    Getter builder;

    Setter setter;

    @NotNull
    @Override
    public JsonParserFrame createFrame(@NotNull JsonParserFrame bottom) {
        final Object self = ((PlainObjectFrame) bottom).self;
        Object object;
        if ((flags & FLAG_ALWAYS_BUILD) == 0) {
            object = getter.invoke(self);
            if (object == null) {
                object = CollectionConstant.UNDEFINED;
            }
        } else {
            object = CollectionConstant.UNDEFINED;
        }
        if (object == CollectionConstant.UNDEFINED) {
            object = builder.invoke(self);
            if (object == CollectionConstant.UNDEFINED) {
                object = null;
            } else {
                bottom.key = this;
            }
        }
        if (object == null) {
            return new JsonParserFrame(bottom); // NOP frame
        }
        JsonType actualType;
        if ((flags & FLAG_DYNAMIC) != 0) {
            actualType = bottom.context.forClass(object.getClass());
        } else {
            actualType = staticType;
        }
        return actualType.createFrame(bottom); // todo
    }

    @Override
    public void serialize(Object in, @NotNull JsonHandler out, @NotNull JsonContext context) {
        final Object object = getter.invoke(in);
        if (object == CollectionConstant.UNDEFINED) {
            return;
        }
        nameAsKey(out);
        JsonType actualType;
        if ((flags & FLAG_DYNAMIC) != 0) {
            if (object != null) {
                actualType = context.forClass(object.getClass());
            } else {
                out.nullValue();
                return;
            }
        } else {
            actualType = staticType;
        }
        actualType.serialize(object, out, context);
    }
}
