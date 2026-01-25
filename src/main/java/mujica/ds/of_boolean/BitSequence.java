package mujica.ds.of_boolean;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2025/4/15")
public interface BitSequence {

    int bitLength();

    boolean getBit(int index);
}
