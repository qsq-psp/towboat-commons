package mujica.io.misc;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2026/2/18")
public enum FileStoreAttributeKey {

    TOTAL_SPACE,
    USABLE_SPACE,
    BLOCK_SIZE,
    UNALLOCATED_SPACE,
    USED_SPACE;

    private static final long serialVersionUID = 0xC3740CAFFAF067FEL;
}
