package mujica.text.escape;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ConstantInterface;

@ConstantInterface
@CodeHistory(date = "2022/5/30", project = "Ultramarine")
@CodeHistory(date = "2025/3/4")
public interface EscapeValueStyle {

    int HEX_VALUE               = 0x01;

    int UPPER_CASE              = 0x02;

    int UNICODE_PREFIX          = 0x04;

    int CURLY_BRACKET           = 0x08;

    int APPLY_TO_SPACE          = 0x10;

    int APPLY_TO_ZH_CN          = 0x20;

    int APPLY_TO_RUNES          = 0x40;
}
