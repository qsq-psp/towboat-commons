package mujica.text.collation;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2026/9/8")
public interface MathBrackets {

    Bracket CEILING = new Bracket("\u2308", "\u2309");

    Bracket FLOOR = new Bracket("\u230a", "\u230b");

    @Deprecated
    Bracket ANGLE = new Bracket("\u2329", "\u232a");

    Bracket HORIZONTAL_SQUARE = new Bracket("\u23b4", "\u23b5");

    Bracket HORIZONTAL_ROUND = new Bracket("\u23dc", "\u23dd");

    Bracket HORIZONTAL_CURLY = new Bracket("\u23de", "\u23df");

    Bracket HORIZONTAL_TORTOISE_SHELL = new Bracket("\u23e0", "\u23e1");
}
