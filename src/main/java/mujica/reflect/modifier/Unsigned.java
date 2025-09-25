package mujica.reflect.modifier;

import java.lang.annotation.*;

@CodeHistory(date = "2025/3/3")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
public @interface Unsigned {}
