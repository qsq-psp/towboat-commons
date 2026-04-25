package mujica.json.modifier;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ConstantComposition;
import mujica.reflect.modifier.ConstantInterface;
import org.intellij.lang.annotations.MagicConstant;

import java.lang.annotation.*;

@CodeHistory(date = "2021/12/31", project = "infrastructure", name = "SerializeFrom, SerializeToNull, SerializeToUndefined")
@CodeHistory(date = "2022/6/9", project = "Ultramarine", name = "SerializeFrom, SerializeToNull, SerializeToUndefined")
@CodeHistory(date = "2025/10/29", name = "Serialize")
@CodeHistory(date = "2026/4/7", name = "JsonTransform")
@CodeHistory(date = "2026/4/9")
@Documented
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ConstantInterface(composition = ConstantComposition.OR)
public @interface JsonEmpty {

    int FROM_NULL               = 0x0001;
    int FROM_FALSE              = 0x0002;
    int FROM_ZERO_INTEGRAL      = 0x0004;
    int FROM_ZERO_DECIMAL       = 0x0008;
    int FROM_INFINITE           = 0x0010;
    int FROM_NAN                = 0x0020;
    int FROM_EMPTY_STRING       = 0x0040;
    int FROM_BLANK_STRING       = 0x0080;
    int FROM_EMPTY_ARRAY        = 0x0200;
    int FROM_LOOP_OBJECT        = 0x0400;

    @MagicConstant(flagsFromClass = JsonEmpty.class)
    int toNull() default -1;

    @MagicConstant(flagsFromClass = JsonEmpty.class)
    int toUndefined() default -1;
}
