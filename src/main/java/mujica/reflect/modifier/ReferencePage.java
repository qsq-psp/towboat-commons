package mujica.reflect.modifier;

import java.lang.annotation.*;

@CodeHistory(date = "2025/5/5")
@Stable(date = "2025/8/6")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
@Repeatable(ReferenceDocument.class)
public @interface ReferencePage {

    String title();

    String href();
}
