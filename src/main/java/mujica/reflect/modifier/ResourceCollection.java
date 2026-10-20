package mujica.reflect.modifier;

import java.lang.annotation.*;

@CodeHistory(date = "2026/10/4")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.PACKAGE, ElementType.MODULE})
public @interface ResourceCollection {

    Resource[] value();
}
