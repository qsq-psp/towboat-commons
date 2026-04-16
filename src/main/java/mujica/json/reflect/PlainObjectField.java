package mujica.json.reflect;

import mujica.json.entity.FastString;
import mujica.json.entity.JsonHandler;
import mujica.json.modifier.JsonHint;
import mujica.reflect.modifier.CodeHistory;
import mujica.text.sanitizer.CharSequenceAppender;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

@CodeHistory(date = "2021/12/30", project = "infrastructure", name = "Mapping")
@CodeHistory(date = "2022/6/3", project = "Ultramarine", name = "Mapping")
@CodeHistory(date = "2026/4/3")
abstract class PlainObjectField implements Serializable {

    private static final long serialVersionUID = 0xf952640a6a05d352L;

    @NotNull
    JsonType declaredType = JsonType.NOP;

    @NotNull
    Getter getter = Getter.NOP;

    @NotNull
    Setter setter = Setter.NOP;

    PlainObjectField() {
        super();
    }

    @NotNull
    static PlainObjectField forName(@NotNull String name) {
        if (CharSequenceAppender.Json.ESSENTIAL_CORE.isIdentity(name)) {
            return new FastNamed(new FastString(name));
        } else {
            return new StringNamed(name);
        }
    }

    @NotNull
    public NopFrame createFrame(@NotNull PlainObjectFrame frame) throws Throwable {
        final Object object = getter.get(frame.self); // check always build
        JsonType actualType;
        if (object != null && (declaredType.flags & JsonHint.DYNAMIC_TYPE) != 0L) {
            actualType = frame.context.forClass(object.getClass());
        } else {
            actualType = declaredType;
        }
        frame.setKey(object);
        return actualType.createFrame(frame);
    }

    void transform(Object self, @NotNull JsonHandler out, @NotNull JsonContext context) throws Throwable {
        JsonType actualType;
        if ((declaredType.flags & JsonHint.DYNAMIC_TYPE) != 0L) {
            Object object = getter.get(self);
            if (object != null) {
                actualType = context.forClass(object.getClass()).from(object);
            } else {
                actualType = declaredType.from();
            }
        } else {
            actualType = declaredType.from(getter, self);
        }
        nameAsKey(out);
        actualType.to(out, context);
    }

    @NotNull
    protected abstract String getName();

    protected abstract void nameAsKey(@NotNull JsonHandler jh);

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
        protected String getName() {
            return stringName;
        }

        @Override
        protected void nameAsKey(@NotNull JsonHandler jh) {
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
        protected String getName() {
            return fastName.toString();
        }

        @Override
        protected void nameAsKey(@NotNull JsonHandler jh) {
            jh.stringKey(fastName);
        }
    }

    void collectField(@NotNull Field field, @NotNull JsonContext context) {
        if (collectDeclaredType(field.getType(), context)) {
            collectDeclaredGenericType(field.getGenericType(), context);
        }
        declaredType = (JsonType) declaredType.collectConfig(field, true);
        getter = new ClassicalFieldGetter(field);
        if ((Modifier.FINAL & field.getModifiers()) == 0) {
            setter = new ClassicalFieldSetter(field);
        }
    }

    void collectGetter(@NotNull Method method, @NotNull JsonContext context) {
        if (collectDeclaredType(method.getReturnType(), context)) {
            collectDeclaredGenericType(method.getGenericReturnType(), context);
        }
        declaredType = (JsonType) declaredType.collectConfig(method, true);
        getter = new ClassicalMethodGetter(method);
    }

    void collectSetter(@NotNull Method method, @NotNull JsonContext context) {
        if (collectDeclaredType(method.getParameterTypes()[0], context)) {
            collectDeclaredGenericType(method.getGenericParameterTypes()[0], context);
        }
        declaredType = (JsonType) declaredType.collectConfig(method, true);
        setter = new ClassicalMethodSetter(method);
    }

    boolean collectDeclaredType(@NotNull Class<?> clazz, @NotNull JsonContext context) {
        if (declaredType == JsonType.NOP) {
            declaredType = context.forClass(clazz);
        }
        return declaredType.needGenericInfo();
    }

    void collectDeclaredGenericType(@NotNull Type genericType, @NotNull JsonContext context) {
        declaredType = declaredType.collectGeneric(genericType, context);
    }

    void mergeSuperField(@NotNull PlainObjectField that, @NotNull JsonContext context) {
        if (getter == Getter.NOP) {
            getter = that.getter;
            context.getLogger().debug("merge super getter");
        }
        if (setter == Setter.NOP) {
            setter = that.setter;
            context.getLogger().debug("merge super setter");
        }
    }
}
