package mujica.reflect.function;

/**
 * Created on 2025/3/18.
 */
public enum PredicateMistake {

    NONE,
    FALSE_TO_TRUE, // the actual answer is false but the predicate returns true
    TRUE_TO_FALSE, // the actual answer is true but the predicate returns false
    BOTH;

    private static final long serialVersionUID = 0xce15580297d325f4L;
}
