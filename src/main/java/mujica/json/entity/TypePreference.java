package mujica.json.entity;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2026/4/15")
public interface TypePreference {

    int FLAG_INTEGRAL_OVERFLOW_TO_FRACTIONAL    = 0x01;
    int FLAG_INTEGRAL_OVERFLOW_TO_RAW           = 0x02;
    int FLAG_FRACTIONAL_OVERFLOW_TO_RAW         = 0x04;
    int FLAG_INTEGRAL_FORCE_TO_FRACTIONAL       = 0x08;
    int FLAG_INTEGRAL_FORCE_TO_RAW              = 0x10;
    int FLAG_FRACTIONAL_FORCE_TO_RAW            = 0x20;
    int FLAG_SKIP_VALUE                         = 0x40;
    int FLAG_SKIP_TO_BYTE_BUF                   = 0x80;

    int typePreference();
}
