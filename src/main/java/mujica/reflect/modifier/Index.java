package mujica.reflect.modifier;

import java.lang.annotation.*;

/**
 * Created in Ultramarine on 2024/5/1.
 * Moved here on 2025/3/3.
 */
@CodeHistory(date = "2024/5/1", project = "Ultramarine")
@CodeHistory(date = "2025/3/3")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
public @interface Index {

    String of() default "";

    int start() default 0;

    boolean inclusive() default true;

    boolean supportsNegative() default false;
}
