package mujica.grammar.bnf;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2022/4/21", project = "infrastructure", name = "Operator")
@CodeHistory(date = "2025/10/24")
enum BnfOperator implements BnfPriority {

    SEPARATOR(";", 5),
    DEFINE("= := ::= :", 4),
    DEFINE_ALTERNATIVE("=/ /= =| |=", 4),
    ALTERNATIVE("/ |", 3),
    REPEAT("*", 2),
    ROUND_LEFT("(", LEFT_BRACKET),
    ROUND_RIGHT(")", RIGHT_BRACKET),
    RECTANGLE_LEFT("[", LEFT_BRACKET),
    RECTANGLE_RIGHT("]", RIGHT_BRACKET);

    private static final long serialVersionUID = 0x880ac50bc38b5ad3L;

    final String signs; // multiple forms separated by space

    final int priority;

    BnfOperator(String signs, int priority) {
        this.signs = signs;
        this.priority = priority;
    }
}
