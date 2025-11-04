package mujica.reflect.basic;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Stable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Consumer;

@CodeHistory(date = "2022/9/22", project = "Ultramarine", name = "ClassUtility")
@CodeHistory(date = "2025/2/17", project = "Ultramarine", name = "ClassTravel")
@CodeHistory(date = "2025/3/6")
@Stable(date = "2025/8/6")
public final class ClassUtil {

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
}
