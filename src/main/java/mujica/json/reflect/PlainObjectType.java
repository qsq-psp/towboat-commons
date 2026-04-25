package mujica.json.reflect;

import mujica.ds.generic.set.CollectionConstant;
import mujica.json.entity.JsonHandler;
import mujica.json.modifier.JsonEmpty;
import mujica.json.modifier.JsonHint;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.FieldOrder;
import mujica.reflect.modifier.Name;
import mujica.reflect.modifier.NameCollection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;

@CodeHistory(date = "2021/12/24", project = "va", name = "JsonClass")
@CodeHistory(date = "2021/12/30", project = "infrastructure", name = "ReflectedClass")
@CodeHistory(date = "2022/6/4", project = "Ultramarine", name = "ReflectedClass")
@CodeHistory(date = "2022/7/13", project = "Ultramarine", name = "ReflectClass")
@CodeHistory(date = "2026/3/27")
class PlainObjectType extends BuiltType {

    private static final long serialVersionUID = 0x09c112151b161872L;

    protected transient Object value;

    PlainObjectType(long flags) {
        super(flags);
    }

    PlainObjectType(long flags, @NotNull Getter builder) {
        super(flags, builder);
    }

    @NotNull
    @Override
    public NopFrame createFrame(@NotNull NopFrame bottom) {
        if (bottom.shape != NopFrame.StructureShape.OBJECT) {
            bottom.context.getLogger().warn("container mismatch");
            return new NopFrame(bottom);
        }
        Object self = bottom.key;
        if (self == null) {
            try {
                self = builder.get(null);
            } catch (Throwable e) {
                bottom.context.getLogger().warn("build fail");
            }
        }
        if (self == null) {
            bottom.context.getLogger().warn("null plain object");
            return new NopFrame(bottom);
        }
        return new PlainObjectFrame(bottom, this, self);
    }

    @NotNull
    @Override
    JsonType from(@Nullable Object object) {
        value = object;
        state = CollectionConstant.PRESENT;
        return this;
    }

    @Override
    void to(@NotNull Setter setter, @Nullable Object self) throws Throwable {
        if (state == CollectionConstant.PRESENT) {
            Object object = value;
            state = CollectionConstant.UNDEFINED;
            value = null;
            setter.set(self, object);
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    void to(@NotNull JsonHandler out, @NotNull JsonContext context) {
        if (state == CollectionConstant.PRESENT) {
            Object self = value;
            state = CollectionConstant.UNDEFINED;
            if (self != null) {
                value = null;
                Collection<PlainObjectField> fields = fieldCollection();
                if (fields.isEmpty()) {
                    out.emptyObjectValue();
                } else {
                    out.openObject();
                    if ((flags & JsonHint.RANDOM_ORDER) != 0L) {
                        fields = context.randomContext.copyAndShuffleList(fields);
                    }
                    for (PlainObjectField field : fields) {
                        try {
                            field.transform(self, out, context);
                        } catch (Throwable e) {
                            context.getLogger().warn("", e);
                        }
                    }
                    out.closeObject();
                }
            } else if ((flags & (((long) JsonEmpty.FROM_NULL) << UNDEFINED_SHIFT)) != 0L) {
                context.getLogger().debug("null to undefined");
                out.skippedValue();
            } else {
                out.nullValue();
            }
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    Object toObject(@NotNull JsonContext context) {
        if (state == CollectionConstant.PRESENT) {
            Object object = value;
            state = CollectionConstant.UNDEFINED;
            value = null;
            if (object == null && (flags & (((long) JsonEmpty.FROM_NULL) << UNDEFINED_SHIFT)) != 0L) {
                context.getLogger().debug("null to undefined");
                return CollectionConstant.UNDEFINED;
            }
            return object;
        } else {
            throw new IllegalStateException();
        }
    }

    @Nullable
    PlainObjectField fieldForName(@NotNull String name) {
        return null;
    }

    @NotNull
    Collection<PlainObjectField> fieldCollection() {
        return Collections.emptyList();
    }

    @CodeHistory(date = "2026/4/3")
    static class ListBased extends PlainObjectType {

        private static final long serialVersionUID = 0x2078cb85bea19a3bL;

        @NotNull
        final ArrayList<PlainObjectField> list;

        ListBased(long flags, @NotNull Getter builder, @NotNull Collection<PlainObjectField> collection) {
            super(flags, builder);
            list = new ArrayList<>(collection);
        }

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

        @NotNull
        @Override
        JsonType collectType(@NotNull Class<?> clazz, @NotNull JsonContext context) {
            throw new UnsupportedOperationException();
        }
    }

    @CodeHistory(date = "2026/4/3")
    static class MapBased extends PlainObjectType {

        private static final long serialVersionUID = 0xf02a125f0b001a2bL;

        @NotNull
        final LinkedHashMap<String, PlainObjectField> map;

        MapBased(long flags) {
            super(flags);
            map = new LinkedHashMap<>();
        }

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

        @NotNull
        @Override
        JsonType collectType(@NotNull Class<?> clazz, @NotNull JsonContext context) {
            collectConfig(clazz, false);
            collectJsonFieldOrder(clazz);
            if (clazz.isAnnotation()) {
                collectAnnotationMethod(clazz, context);
            } else {
                collectConstructor(clazz, context);
                if ((flags & JsonHint.IGNORE_FIELDS) == 0) {
                    collectJavaField(clazz, context);
                }
                if ((flags | (JsonHint.IGNORE_SETTERS | JsonHint.IGNORE_GETTERS)) != flags) {
                    collectJavaMethod(clazz, context);
                }
                if ((flags & JsonHint.IGNORE_SUPER_CLASS) == 0) {
                    Class<?> superclass = clazz.getSuperclass();
                    while (superclass != null) {
                        mergeSuperType(context.forClass(superclass), context);
                        superclass = superclass.getSuperclass();
                    }
                }
                if ((flags & JsonHint.IGNORE_SUPER_INTERFACE) == 0) {
                    for (Class<?> superInterface : clazz.getInterfaces()) {
                        mergeSuperType(context.forClass(superInterface), context);
                    }
                }
            }
            if (map.isEmpty()) {
                return new PlainObjectType(flags, builder);
            }
            if (map.size() <= 10) {
                return new ListBased(flags, builder, map.values());
            }
            return this;
        }

        void collectJsonFieldOrder(@NotNull AnnotatedElement annotated) {
            final FieldOrder fieldOrder = annotated.getDeclaredAnnotation(FieldOrder.class);
            if (fieldOrder != null) {
                for (String name : fieldOrder.value()) {
                    map.put(name, PlainObjectField.forName(name));
                }
                flags |= JsonHint.SEALED;
            } else {
                flags &= ~JsonHint.SEALED;
            }
        }

        void collectJavaField(@NotNull Class<?> clazz, @NotNull JsonContext context) {
            for (Field javaField : clazz.getDeclaredFields()) {
                if (acceptJavaField(javaField.getModifiers())) {
                    String name = nameFor(javaField);
                    PlainObjectField jsonField;
                    if ((flags & JsonHint.SEALED) != 0) {
                        jsonField = map.get(name);
                    } else {
                        jsonField = PlainObjectField.forName(name);
                        map.put(name, jsonField);
                    }
                    if (jsonField != null) {
                        jsonField.collectField(javaField, context);
                    }
                }
            }
        }

        boolean acceptJavaField(int modifiers) {
            if ((flags & JsonHint.COLLECT_NON_PUBLIC) != 0) {
                return ((Modifier.STATIC | Modifier.TRANSIENT) & modifiers) == 0;
            } else {
                return ((Modifier.PUBLIC | Modifier.PRIVATE | Modifier.PROTECTED | Modifier.STATIC | Modifier.TRANSIENT) & modifiers) == Modifier.PUBLIC;
            }
        }

        @NotNull
        String nameFor(@NotNull Field field) {
            final NameCollection nameCollection = field.getDeclaredAnnotation(NameCollection.class);
            if (nameCollection != null) {
                for (Name nameItem : nameCollection.value()) {
                    if (nameItem.language().equals("json")) {
                        return nameItem.value();
                    }
                }
            }
            final Name name = field.getDeclaredAnnotation(Name.class);
            if (name != null && name.language().equals("json")) {
                return name.value();
            }
            return field.getName();
        }

        void collectJavaMethod(@NotNull Class<?> clazz, @NotNull JsonContext context) {
            for (Method javaMethod : clazz.getDeclaredMethods()) {
                if (acceptJavaMethod(javaMethod.getModifiers())) {
                    if (javaMethod.getParameterCount() == 0) {
                        if ((flags & JsonHint.IGNORE_GETTERS) == 0L && javaMethod.getReturnType() != void.class) { // getter
                            String name = nameForGetter(javaMethod);
                            PlainObjectField jsonField;
                            if (name != null) {
                                jsonField = map.get(name);
                                if (jsonField == null && (flags & JsonHint.SEALED) == 0L) {
                                    jsonField = PlainObjectField.forName(name);
                                    map.put(name, jsonField);
                                }
                            } else {
                                jsonField = null;
                            }
                            if (jsonField != null) {
                                jsonField.collectGetter(javaMethod, context);
                            }
                        }
                    } else if (javaMethod.getParameterCount() == 1) {
                        if ((flags & JsonHint.IGNORE_SETTERS) == 0L && javaMethod.getReturnType() == void.class) { // setter
                            String name = nameForSetter(javaMethod);
                            PlainObjectField jsonField;
                            if (name != null) {
                                jsonField = map.get(name);
                                if (jsonField == null && (flags & JsonHint.SEALED) == 0L) {
                                    jsonField = PlainObjectField.forName(name);
                                    map.put(name, jsonField);
                                }
                            } else {
                                jsonField = null;
                            }
                            if (jsonField != null) {
                                jsonField.collectSetter(javaMethod, context);
                            }
                        }
                    }
                }
            }
        }

        boolean acceptJavaMethod(int modifiers) {
            if ((flags & JsonHint.COLLECT_NON_PUBLIC) != 0) {
                return (Modifier.STATIC & modifiers) == 0;
            } else {
                return ((Modifier.PUBLIC | Modifier.PRIVATE | Modifier.PROTECTED | Modifier.STATIC) & modifiers) == Modifier.PUBLIC;
            }
        }

        @Nullable
        String nameForGetter(@NotNull Method method) {
            final NameCollection nameCollection = method.getDeclaredAnnotation(NameCollection.class);
            if (nameCollection != null) {
                for (Name nameItem : nameCollection.value()) {
                    if (nameItem.language().equals("json")) {
                        return nameItem.value();
                    }
                }
            }
            final Name name = method.getDeclaredAnnotation(Name.class);
            if (name != null && name.language().equals("json")) {
                return name.value();
            }
            String string = method.getName();
            if (string.startsWith("get")) {
                string = string.substring(3);
                if (string.isEmpty()) {
                    return null;
                }
                if (Character.isUpperCase(string.charAt(0))) {
                    string = Character.toLowerCase(string.charAt(0)) + string.substring(1);
                }
                if (string.equals("class") || string.startsWith("as")) { // getClass(), getAsInt(), getAsDouble(), etc
                    return null;
                }
                return string;
            } else if (string.startsWith("is") && method.getReturnType() == boolean.class) {
                string = string.substring(2);
                if (string.isEmpty()) {
                    return null;
                }
                if (Character.isUpperCase(string.charAt(0))) {
                    string = Character.toLowerCase(string.charAt(0)) + string.substring(1);
                }
                return string;
            } else {
                return null;
            }
        }

        @Nullable
        String nameForSetter(@NotNull Method method) {
            final NameCollection nameCollection = method.getDeclaredAnnotation(NameCollection.class);
            if (nameCollection != null) {
                for (Name nameItem : nameCollection.value()) {
                    if (nameItem.language().equals("json")) {
                        return nameItem.value();
                    }
                }
            }
            final Name name = method.getDeclaredAnnotation(Name.class);
            if (name != null && name.language().equals("json")) {
                return name.value();
            }
            String string = method.getName();
            if (string.startsWith("set")) {
                string = string.substring(3);
                if (string.isEmpty()) {
                    return null;
                }
                if (Character.isUpperCase(string.charAt(0))) {
                    string = Character.toLowerCase(string.charAt(0)) + string.substring(1);
                }
                return string;
            } else {
                return null;
            }
        }

        void collectAnnotationMethod(@NotNull Class<?> clazz, @NotNull JsonContext context) {
            if ((flags & JsonHint.IGNORE_GETTERS) != 0L) {
                return;
            }
            for (Method javaMethod : clazz.getDeclaredMethods()) {
                if (acceptJavaMethod(javaMethod.getModifiers()) && javaMethod.getParameterCount() == 0 && javaMethod.getReturnType() != void.class) {
                    String name = javaMethod.getName();
                    PlainObjectField jsonField = map.get(name);
                    if (jsonField == null && (flags & JsonHint.SEALED) == 0L) {
                        jsonField = PlainObjectField.forName(name);
                        map.put(name, jsonField);
                    }
                    if (jsonField != null) {
                        jsonField.collectGetter(javaMethod, context);
                    }
                }
            }
        }

        void mergeSuperType(@NotNull JsonType superType, @NotNull JsonContext context) {
            if (!(superType instanceof PlainObjectType)) {
                return;
            }
            final PlainObjectType that = (PlainObjectType) superType;
            for (PlainObjectField thatField : that.fieldCollection()) {
                String name = thatField.getName();
                PlainObjectField thisField = map.get(name);
                if (thisField == null) {
                    if ((flags & JsonHint.SEALED) == 0L) {
                        map.put(name, thatField);
                    }
                } else {
                    thisField.mergeSuperField(thatField, context);
                }
            }
        }
    }
}
