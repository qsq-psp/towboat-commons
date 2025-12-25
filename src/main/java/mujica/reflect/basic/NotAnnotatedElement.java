package mujica.reflect.basic;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Stable;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

@CodeHistory(date = "2022/8/18", project = "LeetCode", name = "NeverAnnotatedElement")
@CodeHistory(date = "2022/11/14", project = "nettyon", name = "NeverAnnotatedElement")
@CodeHistory(date = "2025/10/31")
@Stable(date = "2025/11/3")
public class NotAnnotatedElement implements AnnotatedElement, Serializable {

    public static final NotAnnotatedElement INSTANCE = new NotAnnotatedElement();

    private static final Annotation[] EMPTY = new Annotation[0];

    private static final long serialVersionUID = 0xf3e0b8099fbf583bL;

    @Override
    public boolean isAnnotationPresent(@NotNull Class<? extends Annotation> annotationClass) {
        return false;
    }

    @Override
    public <T extends Annotation> T getAnnotation(@NotNull Class<T> annotationClass) {
        return null;
    }

    @Override
    @NotNull
    public Annotation[] getAnnotations() {
        return EMPTY;
    }

    @NotNull
    @Override
    public Annotation[] getDeclaredAnnotations() {
        return EMPTY;
    }
}
