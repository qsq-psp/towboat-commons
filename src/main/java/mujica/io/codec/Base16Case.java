package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ConstantComposition;
import mujica.reflect.modifier.ConstantInterface;
import mujica.reflect.modifier.Stable;

@CodeHistory(date = "2022/3/23", project = "Ultramarine", name = "HexCase")
@CodeHistory(date = "2025/3/2")
@Stable(date = "2025/10/9")
@ConstantInterface(composition = ConstantComposition.NEVER)
public interface Base16Case {

    int UPPER = 'A' - 0xA;
    int LOWER = 'a' - 0xa;
}
