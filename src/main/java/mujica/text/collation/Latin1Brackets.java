package mujica.text.collation;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2026/3/6")
public interface Latin1Brackets {

    Bracket ROUND = new Bracket("(", ")");

    Bracket SHARP = new Bracket("<", ">");

    Bracket SQUARE = new Bracket("[", "]");

    Bracket CURLY = new Bracket("{", "}");

    Bracket QUOTATION_MARK = new Bracket("\"");

    Bracket APOSTROPHE = new Bracket("'");
}
