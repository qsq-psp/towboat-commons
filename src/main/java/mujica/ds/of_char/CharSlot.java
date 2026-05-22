package mujica.ds.of_char;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2026/3/24")
@Deprecated
public interface CharSlot {

    char getChar();

    void setChar(char newValue);
}
