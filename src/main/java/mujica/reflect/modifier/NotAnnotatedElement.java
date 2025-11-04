package mujica.reflect.modifier;

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

    private static final long serialVersionUID = 0xF3E0B8099FBF583BL;

    private static final Annotation[] EMPTY = new Annotation[0];

    @Override
    public boolean isAnnotationPresent(@NotNull Class<? extends Annotation> annotationClass) {
        return false;
    }

    @Override
    public <T extends Annotation> T getAnnotation(@NotNull Class<T> annotationClass) {
        return null;
    }

    @Override
    public Annotation[] getAnnotations() {
        return EMPTY;
    }

    @Override
    public Annotation[] getDeclaredAnnotations() {
        return EMPTY;
    }
}
