package mujica.reflect.modifier;

@CodeHistory(date = "2024/7/4", project = "towboat-commons")
@CodeHistory(date = "2025/3/4")
public enum ConstantComposition {

    NEVER, // indicate the constant is an enum, not a flag
    CONCAT, ADD, MULTIPLY, POWER, // arithmetic composition
    AND, OR, XOR, // bitwise composition
    OTHER; // complex situation

    private static final long serialVersionUID = 0x03ed4db583225278L;
}
