package mujica.reflect.modifier;

import java.lang.annotation.*;

@CodeHistory(date = "2025/5/5")
@Stable(date = "2025/7/23")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.PACKAGE, ElementType.MODULE})
@Repeatable(CodeHistoryTimeline.class)
public @interface CodeHistory {

    String date();

    String project() default "";

    String name() default "";
}
