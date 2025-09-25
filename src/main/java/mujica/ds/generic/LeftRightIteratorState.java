package mujica.ds.generic;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2024/2/27", project = "Ultramarine", name = "ThreeStateIterator")
@CodeHistory(date = "2025/3/13")
public enum LeftRightIteratorState {

    BOTH_READY, LEFT_UNKNOWN, RIGHT_UNKNOWN, LEFT_FAIL, RIGHT_FAIL;

    private static final long serialVersionUID = 0xdc9ebeb393e96125L;
}
