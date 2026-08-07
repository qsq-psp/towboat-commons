package mujica.text.collation;

import mujica.reflect.modifier.CodeHistory;

/**
 * Created on 2026/3/14.
 */
@CodeHistory(date = "2026/3/14")
public interface CJKBrackets {

    Bracket ZH_SINGLE_QUOTE = new Bracket("\u2018", "\u2019");

    Bracket ZH_DOUBLE_QUOTE = new Bracket("\u201c", "\u201d");

    Bracket ZH_SINGLE_SHARP = new Bracket("\u3008", "\u3009");

    Bracket ZH_DOUBLE_SHARP = new Bracket("\u300a", "\u300b");

    Bracket ZH_CORNER = new Bracket("\u300c", "\u300d");

    Bracket ROUND = new Bracket("\uff08", "\uff09");

    Bracket SHARP = new Bracket("\uff1c", "\uff1e");

    Bracket SQUARE = new Bracket("\uff3b", "\uff3d");

    Bracket CURLY = new Bracket("\uff5b", "\uff5d");

    Bracket DOUBLE_ROUND = new Bracket("\uff5f", "\uff60");

    Bracket CORNER = new Bracket("\uff62", "\uff63");

    Bracket QUOTATION_MARK = new Bracket("\uff02");

    Bracket APOSTROPHE = new Bracket("\uff07");
}
