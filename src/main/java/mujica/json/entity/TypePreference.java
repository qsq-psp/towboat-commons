package mujica.json.entity;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2026/4/15")
public interface TypePreference {

    int FLAG_INTEGRAL_OVERFLOW_TO_FRACTIONAL    = 0x001;
    int FLAG_INTEGRAL_OVERFLOW_TO_RAW           = 0x002;
    int FLAG_FRACTIONAL_OVERFLOW_TO_RAW         = 0x004;
    int FLAG_INTEGRAL_FORCE_TO_FRACTIONAL       = 0x008;
    int FLAG_INTEGRAL_FORCE_TO_RAW              = 0x010;
    int FLAG_FRACTIONAL_FORCE_TO_RAW            = 0x020;
    int FLAG_SKIP_VALUE                         = 0x040;
    int FLAG_SKIP_TO_BYTE_BUF                   = 0x080;
    int FLAG_DO_NOT_CACHE_STRING_KEY            = 0x100;
    int FLAG_DO_NOT_CACHE_FAST_STRING_KEY       = 0x200;
    int FLAG_DO_NOT_CACHE_STRING_VALUE          = 0x400;
    int FLAG_DO_NOT_CACHE_FAST_STRING_VALUE     = 0x800;

    int typePreference();
}
