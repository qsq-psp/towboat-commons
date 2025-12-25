package mujica.reflect.modifier;

import java.lang.annotation.*;

@CodeHistory(date = "2025/3/3", name = "Unsigned")
@CodeHistory(date = "2025/11/24")
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
public @interface DataType {

    String value(); // u8, u16, ...
}
