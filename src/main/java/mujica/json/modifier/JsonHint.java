package mujica.json.modifier;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ConstantComposition;
import mujica.reflect.modifier.ConstantInterface;
import org.intellij.lang.annotations.MagicConstant;

import java.lang.annotation.*;

@CodeHistory(date = "2021/12/28", project = "va", name = "ParseCast")
@CodeHistory(date = "2021/12/26", project = "Ultramarine", name = "ParseHint")
@CodeHistory(date = "2026/4/7", name = "ParseFlags")
@CodeHistory(date = "2026/4/9")
@Documented
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ConstantInterface(composition = ConstantComposition.OR)
public @interface JsonHint {

    int NULLABLE                    = 0x0000_0001;
    int UNSIGNED                    = 0x0000_0002;
    int CAST_FROM_BOOLEAN           = 0x0000_0004;
    int CAST_FROM_INTEGRAL          = 0x0000_0008;
    int CAST_FROM_DECIMAL           = 0x0000_0010;
    int CAST_FROM_STRING            = 0x0000_0020;
    int CAST_FROM_ARRAY             = 0x0000_0040;
    int CAST_TO_UNIT_ARRAY          = 0x0000_0080;
    int INTEGRAL_TRUNCATE           = 0x0000_0100;
    int INTEGRAL_CLAMP              = 0x0000_0200;
    int APPEND_TO_ARRAY             = 0x0000_0400;
    int CLEAR_COLLECTION            = 0x0000_0800;
    int DYNAMIC_TYPE                = 0x0000_1000;
    int ALWAYS_BUILD                = 0x0000_2000;
    int RANDOM_ORDER                = 0x0000_4000;
    int USE_METHOD_HANDLE           = 0x0000_8000;
    int IGNORE_FIELDS               = 0x0001_0000;
    int IGNORE_SETTERS              = 0x0002_0000;
    int IGNORE_GETTERS              = 0x0004_0000;
    int IGNORE_SUPER_CLASS          = 0x0008_0000;
    int IGNORE_SUPER_INTERFACE      = 0x0010_0000;
    int COLLECT_NON_PUBLIC          = 0x0020_0000;
    int SEALED                      = 0x0040_0000;
    int DERIVED                     = 0x0080_0000;

    @MagicConstant(flagsFromClass = JsonHint.class)
    int value();
}
