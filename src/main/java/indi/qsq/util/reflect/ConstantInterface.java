package indi.qsq.util.reflect;

import java.lang.annotation.*;

/**
 * Created on 2024/7/4.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ConstantInterface {

    ConstantComposition composition() default ConstantComposition.NONE;
}
