package mujica.json.entity;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ConstantComposition;
import mujica.reflect.modifier.ConstantInterface;

@CodeHistory(date = "2025/11/6")
@ConstantInterface(composition = ConstantComposition.NEVER)
public interface StructureChecked {

    int STATE_START = 0;
    int STATE_END = 1;
    int STATE_ARRAY = 2;
    int STATE_OBJECT = 3;
    int STATE_KEY = 4;
}
