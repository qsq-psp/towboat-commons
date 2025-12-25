package mujica.text.regex;

import mujica.ds.of_int.list.IntSequence;
import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2025/12/15")
public interface CursorIntSequence extends IntSequence {

    @Override
    int intLength();

    int getCursorIndex();

    void setCursorIndex(int index);

    @Override
    int getInt(int index);
}
