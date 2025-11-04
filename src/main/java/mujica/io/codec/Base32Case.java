package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ConstantComposition;
import mujica.reflect.modifier.ConstantInterface;
import mujica.reflect.modifier.Stable;

@CodeHistory(date = "2025/4/22")
@Stable(date = "2025/10/9")
@ConstantInterface(composition = ConstantComposition.NEVER)
public interface Base32Case {

    int UPPER = 'A';
    int LOWER = 'a';
}
