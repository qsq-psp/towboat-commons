package mujica.reflect.modifier;

import java.lang.annotation.*;

@CodeHistory(date = "2024/7/4", project = "towboat-commons", name = "ActuallyChar")
@CodeHistory(date = "2025/3/4")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
@Deprecated
public @interface InterpretAsChar {

    boolean canBeZero() default true;

    boolean canBeEOF() default false;

    boolean canContainSurrogate() default true;
}
