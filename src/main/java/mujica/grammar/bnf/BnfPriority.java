package mujica.grammar.bnf;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ConstantComposition;
import mujica.reflect.modifier.ConstantInterface;

@CodeHistory(date = "2025/10/24")
@ConstantInterface(composition = ConstantComposition.NEVER)
interface BnfPriority {

    int LEFT_BRACKET = 1;
    int RIGHT_BRACKET = 2;
}
