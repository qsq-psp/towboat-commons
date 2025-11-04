package mujica.grammar.regex;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2021/10/6", project = "?")
@CodeHistory(date = "2021/10/18", project = "va")
@CodeHistory(date = "2022/3/23", project = "infrastructure")
@CodeHistory(date = "2025/5/24")
enum MetaSymbol {

    STAR(MetaSymbolType.UNARY, "*"),
    PLUS(MetaSymbolType.UNARY, "+"),
    QUESTION(MetaSymbolType.UNARY, "?"),
    ALTERNATION(MetaSymbolType.BINARY, "|"),
    FROM_TO(MetaSymbolType.BINARY, "-"),
    COMMA(MetaSymbolType.BINARY, ","),
    COMPLEMENT(MetaSymbolType.MODIFIER, "^"),
    PARENTHESIS_LEFT(MetaSymbolType.BRACKET_LEFT, "("),
    PARENTHESIS_RIGHT(MetaSymbolType.BRACKET_RIGHT, ")"),
    SQUARE_LEFT(MetaSymbolType.BRACKET_LEFT, "["),
    SQUARE_RIGHT(MetaSymbolType.BRACKET_RIGHT, "]"),
    CURLY_LEFT(MetaSymbolType.BRACKET_LEFT, "{"),
    CURLY_RIGHT(MetaSymbolType.BRACKET_RIGHT, "}");

    private static final long serialVersionUID = 0x7fe79906d0ea4d6bL;

    public final MetaSymbolType type;

    public final String sign;

    MetaSymbol(MetaSymbolType type, String sign) {
        this.type = type;
        this.sign = sign;
    }

    @Override
    public String toString() {
        return '<' + sign + '>';
    }
}
