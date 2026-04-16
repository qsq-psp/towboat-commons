package mujica.reflect.modifier;

import java.lang.annotation.*;

@CodeHistory(date = "2026/4/15")
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DirectSubclass {

    Class<?>[] value();
}
