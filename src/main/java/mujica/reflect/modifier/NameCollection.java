package mujica.reflect.modifier;

import java.lang.annotation.*;

@CodeHistory(date = "2025/5/5")
@Stable(date = "2025/8/1")
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface NameCollection {

    Name[] value();
}
