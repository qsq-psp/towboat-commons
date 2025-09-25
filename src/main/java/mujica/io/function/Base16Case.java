package mujica.io.function;

import mujica.reflect.modifier.ConstantComposition;
import mujica.reflect.modifier.ConstantInterface;

/**
 * Created in Ultramarine on 2022/3/23, Named HexCase.
 * Recreated on 2025/3/2.
 */
@ConstantInterface(composition = ConstantComposition.NEVER)
public interface Base16Case {

    int UPPER = 'A' - 0xA;
    int LOWER = 'a' - 0xa;
}
