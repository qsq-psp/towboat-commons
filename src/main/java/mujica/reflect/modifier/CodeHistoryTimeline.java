package mujica.reflect.modifier;

import java.lang.annotation.*;

@CodeHistory(date = "2025/5/5")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.PACKAGE, ElementType.MODULE})
public @interface CodeHistoryTimeline {

    CodeHistory[] value();
}
