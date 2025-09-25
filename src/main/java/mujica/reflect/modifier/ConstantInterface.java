package mujica.reflect.modifier;

import java.lang.annotation.*;

@CodeHistory(date = "2025/3/3")
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ConstantInterface {

    ConstantComposition composition() default ConstantComposition.OR;
}
