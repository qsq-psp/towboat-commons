package mujica.reflect.modifier;

import java.lang.annotation.*;

@CodeHistory(date = "2025/12/14")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface ReferenceCode {

    String groupId();

    String artifactId();

    String version() default "";

    String fullyQualifiedName();
}
