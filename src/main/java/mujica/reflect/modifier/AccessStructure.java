package mujica.reflect.modifier;

import java.lang.annotation.*;

@CodeHistory(date = "2026/1/29")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
public @interface AccessStructure {

    boolean online();

    boolean local();
}
