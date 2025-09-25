package mujica.reflect.modifier;

import java.lang.annotation.*;

@CodeHistory(date = "2025/7/23")
@Stable(date = "2025/7/23")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.MODULE, ElementType.PACKAGE, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface Stable {

    String date();
}
