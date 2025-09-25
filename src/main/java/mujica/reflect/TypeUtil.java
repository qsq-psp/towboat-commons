package mujica.reflect;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Stable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

@CodeHistory(date = "2022/7/22", project = "infrastructure")
@CodeHistory(date = "2022/7/23", project = "Ultramarine", name = "GenericSearch")
@CodeHistory(date = "2024/2/17", project = "Ultramarine", name = "GenericTravel")
@CodeHistory(date = "2025/3/6")
@Stable(date = "2025/7/25")
public final class TypeUtil {

    @Nullable
    public static Class<?> castToClass(@Nullable Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            type = ((ParameterizedType) type).getRawType();
            if (type instanceof Class) {
                return (Class<?>) type;
            }
        }
        return null;
    }

    @Nullable
    public static Class<?> searchForClass(@Nullable Type from, @NotNull Class<?> to, @NotNull String name) {
        return castToClass(search(from, to, name));
    }

    @Nullable
    public static Type search(@Nullable Type from, @NotNull Class<?> to, @NotNull String name) {
        if (from instanceof ParameterizedType) {
            Type raw = ((ParameterizedType) from).getRawType();
            if (raw instanceof Class) {
                Type type = searchFromClass((Class<?>) raw, to, name);
                if (type instanceof TypeVariable) {
                    type = getActualTypeArgument(from, (Class<?>) raw, ((TypeVariable<?>) type).getName());
                }
                return type;
            }
        } else if (from instanceof Class) {
            return searchFromClass((Class<?>) from, to, name);
        }
        return null;
    }

    @Nullable
    private static Type searchFromClass(@NotNull Class<?> from, @NotNull Class<?> to, @NotNull String name) {
        if (to.isInterface()) {
            if (from.isInterface()) {
                return searchInterfaces(from, to, name);
            } else {
                return searchBoth(from, to, name);
            }
        } else {
            if (from.isInterface()) {
                return null; // not found
            } else {
                return searchClasses(from, to, name);
            }
        }
    }

    @Nullable
    private static Type searchBoth(@NotNull Class<?> from, @NotNull Class<?> to, @NotNull String name) {
        final Class<?> superCLass = from.getSuperclass();
        if (superCLass == to) {
            return getActualTypeArgument(from.getGenericSuperclass(), superCLass, name);
        } else if (superCLass != null) {
            Type type = searchBoth(superCLass, to, name);
            if (type != null) {
                if (type instanceof TypeVariable) {
                    type = getActualTypeArgument(from.getGenericSuperclass(), superCLass, ((TypeVariable<?>) type).getName());
                }
            }
            if (type != null) {
                return type;
            }
        }
        return searchInterfaces(from, to, name);
    }

    @Nullable
    private static Type searchClasses(@NotNull Class<?> from, @NotNull Class<?> to, @NotNull String name) {
        final Class<?> superCLass = from.getSuperclass();
        Type type;
        if (superCLass == to) {
            type = getActualTypeArgument(from.getGenericSuperclass(), superCLass, name);
        } else if (superCLass != null) {
            type = searchClasses(superCLass, to, name);
            if (type instanceof TypeVariable) {
                type = getActualTypeArgument(from.getGenericSuperclass(), superCLass, ((TypeVariable<?>) type).getName());
            }
        } else {
            type = null;
        }
        return type;
    }

    @Nullable
    private static Type searchInterfaces(@NotNull Class<?> from, @NotNull Class<?> to, @NotNull String name) {
        for (Class<?> superInterface : from.getInterfaces()) {
            if (superInterface == to) {
                return getActualTypeArgument(getGenericInterface(from, superInterface), superInterface, name);
            } else {
                Type type = searchInterfaces(superInterface, to, name);
                if (type instanceof TypeVariable) {
                    type = getActualTypeArgument(getGenericInterface(from, superInterface), superInterface, ((TypeVariable<?>) type).getName());
                }
                if (type != null) {
                    return type;
                }
            }
        }
        return null;
    }

    @Nullable
    private static Type getGenericInterface(@NotNull Class<?> from, @NotNull Class<?> to) {
        for (Type type : from.getGenericInterfaces()) {
            if (type instanceof ParameterizedType && ((ParameterizedType) type).getRawType() == to) {
                return type;
            }
        }
        return null;
    }

    @Nullable
    private static Type getActualTypeArgument(@Nullable Type parameterized, @NotNull Class<?> raw, @NotNull String name) {
        if (!(parameterized instanceof ParameterizedType)) {
            return null;
        }
        final TypeVariable<?>[] typeVariables = raw.getTypeParameters();
        final int length = typeVariables.length;
        for (int index = 0; index < length; index++) {
            if (name.equals(typeVariables[index].getName())) {
                return ((ParameterizedType) parameterized).getActualTypeArguments()[index];
            }
        }
        return null;
    }
}
