package mujica.grammar.regex;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2021/10/18", project = "va")
@CodeHistory(date = "2022/3/23", project = "infrastructure")
@CodeHistory(date = "2025/5/22")
enum MetaSymbolType {

    UNARY, BINARY, MODIFIER, BRACKET_LEFT, BRACKET_RIGHT;

    private static final long serialVersionUID = 0xECDF869550D9644CL;
}
