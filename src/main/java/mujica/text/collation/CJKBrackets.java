package mujica.text.collation;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2026/3/14")
public interface CJKBrackets {

    Bracket ZH_SINGLE_QUOTE = new Bracket("\u2018", "\u2019");

    Bracket ZH_DOUBLE_QUOTE = new Bracket("\u201c", "\u201d");

    Bracket ANGLE_QUOTATION_MARK = new Bracket("\u2039", "\u203a");

    Bracket SQUARE_WITH_QUILL = new Bracket("\u2045", "\u2046");

    Bracket ZH_SINGLE_SHARP = new Bracket("\u3008", "\u3009");

    Bracket ZH_DOUBLE_SHARP = new Bracket("\u300a", "\u300b");

    Bracket ZH_CORNER = new Bracket("\u300c", "\u300d");

    Bracket ZH_WHITE_CORNER = new Bracket("\u300e", "\u300f");

    Bracket BLACK_LENTICULAR = new Bracket("\u3010", "\u3011");

    Bracket TORTOISE_SHELL = new Bracket("\u3014", "\u3015");

    Bracket WHITE_LENTICULAR = new Bracket("\u3016", "\u3017");

    Bracket WHITE_TORTOISE_SHELL = new Bracket("\u3018", "\u3019");

    Bracket WHITE_SQUARE = new Bracket("\u301a", "\u301b");

    Bracket ROUND = new Bracket("\uff08", "\uff09");

    Bracket SHARP = new Bracket("\uff1c", "\uff1e");

    Bracket SQUARE = new Bracket("\uff3b", "\uff3d");

    Bracket CURLY = new Bracket("\uff5b", "\uff5d");

    Bracket DOUBLE_ROUND = new Bracket("\uff5f", "\uff60");

    Bracket CORNER = new Bracket("\uff62", "\uff63");

    Bracket QUOTATION_MARK = new Bracket("\uff02");

    Bracket APOSTROPHE = new Bracket("\uff07");
}
