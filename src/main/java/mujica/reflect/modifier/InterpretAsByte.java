package mujica.reflect.modifier;

import java.lang.annotation.*;

@CodeHistory(date = "2024/7/4", project = "towboat-commons", name = "ActuallyByte")
@CodeHistory(date = "2025/3/4")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
@Deprecated
public @interface InterpretAsByte {

    boolean unsigned() default false;

    boolean canBeEOF() default false;
}
