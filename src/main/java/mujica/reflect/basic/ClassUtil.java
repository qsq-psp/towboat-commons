package mujica.reflect.basic;

import mujica.reflect.modifier.CodeHistory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Consumer;

@CodeHistory(date = "2022/9/22", project = "Ultramarine", name = "ClassUtility")
@CodeHistory(date = "2025/2/17", project = "Ultramarine", name = "ClassTravel")
@CodeHistory(date = "2025/3/6")
public final class ClassUtil {

    @NotNull
    public static String removeCommonPrefix(@NotNull Class<?> targetClass, @NotNull Class<?> referenceClass) {
        final String targetName = targetClass.getName();
        final String referenceName = referenceClass.getName();
        int start = 0;
        DOTS:
        while (true) {
            int targetIndex = targetName.indexOf('.', start);
            int referenceIndex = referenceName.indexOf('.', start);
            if (targetIndex == -1) {
                targetIndex = targetName.length();
            }
            if (referenceIndex == -1) {
                referenceIndex = referenceName.length();
            }
            if (targetIndex != referenceIndex) {
                break;
            }
            for (int index = start; index < targetIndex; index++) {
                if (targetName.charAt(index) != referenceName.charAt(index)) {
                    break DOTS;
                }
            }
            start = targetIndex + 1;
        }
        return targetName.substring(start);
    }

    public static void declaredFields(@NotNull Class<?> clazz, @NotNull Consumer<Field> consumer, boolean reverseArray) {
        final Field[] fields = clazz.getDeclaredFields();
        if (reverseArray) {
            for (int index = fields.length - 1; index >= 0; index--) {
                consumer.accept(fields[index]);
            }
        } else {
            for (Field field : fields) {
                consumer.accept(field);
            }
        }
    }

    public static void allFields(@Nullable Class<?> clazz, @NotNull Consumer<Field> consumer, boolean reverseArray, boolean reverseHierarchy) {
        if (reverseHierarchy) {
            if (clazz == null) {
                return;
            }
            allFields(clazz.getSuperclass(), consumer, reverseArray, true); // recursive
            declaredFields(clazz, consumer, reverseArray);
        } else {
            while (clazz != null) {
                declaredFields(clazz, consumer, reverseArray);
                clazz = clazz.getSuperclass(); // iterative
            }
        }
    }

    public static void declaredMethods(@NotNull Class<?> clazz, @NotNull Consumer<Method> consumer, boolean reverseArray) {
        final Method[] methods = clazz.getDeclaredMethods();
        if (reverseArray) {
            for (int index = methods.length - 1; index >= 0; index--) {
                consumer.accept(methods[index]);
            }
        } else {
            for (Method method : methods) {
                consumer.accept(method);
            }
        }
    }

    public static void allMethods(@Nullable Class<?> clazz, @NotNull Consumer<Method> consumer, boolean reverseArray, boolean reverseHierarchy) {
        if (reverseHierarchy) {
            if (clazz == null) {
                return;
            }
            allMethods(clazz.getSuperclass(), consumer, reverseArray, true); // recursive
            declaredMethods(clazz, consumer, reverseArray);
        } else {
            while (clazz != null) {
                declaredMethods(clazz, consumer, reverseArray);
                clazz = clazz.getSuperclass(); // iterative
            }
        }
    }

    public static void declaredNestedClasses(@NotNull Class<?> clazz, @NotNull Consumer<Class<?>> consumer, boolean reverseArray) {
        final Class<?>[] classes = clazz.getDeclaredClasses();
        if (reverseArray) {
            for (int index = classes.length - 1; index >= 0; index--) {
                consumer.accept(classes[index]);
            }
        } else {
            for (Class<?> nested : classes) {
                consumer.accept(nested);
            }
        }
    }

    public static void allNestedClasses(@Nullable Class<?> clazz, @NotNull Consumer<Class<?>> consumer, boolean reverseArray, boolean reverseHierarchy) {
        if (reverseHierarchy) {
            if (clazz == null) {
                return;
            }
            allNestedClasses(clazz.getSuperclass(), consumer, reverseArray, true); // recursive
            declaredNestedClasses(clazz, consumer, reverseArray);
        } else {
            while (clazz != null) {
                declaredNestedClasses(clazz, consumer, reverseArray);
                clazz = clazz.getSuperclass(); // iterative
            }
        }
    }

    public static boolean isOuterClassField(@NotNull Field field) {
        return "this$0".equals(field.getName());
    }

    public static boolean isUtility(@NotNull Class<?> clazz) {
        if (clazz.isArray()) {
            return false;
        }
        if (!clazz.isInterface()) {
            if ((clazz.getModifiers() & Modifier.ABSTRACT) != 0) {
                return false;
            }
            for (Field field : clazz.getDeclaredFields()) {
                if ((field.getModifiers() & Modifier.STATIC) == 0) {
                    return false;
                }
            }
        }
        for (Method method : clazz.getDeclaredMethods()) {
            if ((method.getModifiers() & Modifier.STATIC) == 0) {
                return false;
            }
        }
        {
            Class<?> superClass = clazz.getSuperclass();
            if (!(Object.class.equals(superClass) || isUtility(superClass))) {
                return false;
            }
        }
        for (Class<?> superClass : clazz.getInterfaces()) {
            if (!isUtility(superClass)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isStrictUtility(@NotNull Class<?> clazz) {
        if (clazz.isEnum() || clazz.isArray() || clazz.isAnnotation()) {
            return false;
        }
        if (clazz.isInterface()) {
            if ((clazz.getModifiers() & Modifier.PUBLIC) != 0) {
                return false;
            }
        } else {
            if ((clazz.getModifiers() & (Modifier.PUBLIC | Modifier.ABSTRACT | Modifier.FINAL)) != (Modifier.PUBLIC | Modifier.FINAL)) {
                return false;
            }
            for (Field field : clazz.getDeclaredFields()) {
                if ((field.getModifiers() & Modifier.STATIC) == 0) {
                    return false;
                }
                if (clazz.equals(field.getType())) {
                    return false;
                }
            }
            for (Constructor<?> constructor : clazz.getConstructors()) {
                if ((constructor.getModifiers() & Modifier.PRIVATE) == 0) {
                    return false;
                }
                if (constructor.getParameterCount() != 0) {
                    return false;
                }
            }
        }
        for (Method method : clazz.getDeclaredMethods()) {
            if ((method.getModifiers() & Modifier.STATIC) == 0) {
                return false;
            }
        }
        {
            Class<?> superClass = clazz.getSuperclass();
            if (!(Object.class.equals(superClass) || isStrictUtility(superClass))) {
                return false;
            }
        }
        for (Class<?> superClass : clazz.getInterfaces()) {
            if (!isStrictUtility(superClass)) {
                return false;
            }
        }
        return true;
    }

    private ClassUtil() {
        super();
    }
}
