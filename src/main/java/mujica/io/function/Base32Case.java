package mujica.io.function;

import mujica.reflect.modifier.ConstantComposition;
import mujica.reflect.modifier.ConstantInterface;

/**
 * Created on 2025/4/22.
 */
@ConstantInterface(composition = ConstantComposition.NEVER)
public interface Base32Case {

    int UPPER = 'A';
    int LOWER = 'a';
}
