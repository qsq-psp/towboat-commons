package mujica.grammar.regex;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2021/10/17", project = "va")
@CodeHistory(date = "2022/3/28", project = "infrastructure")
@CodeHistory(date = "2025/5/24")
public class AlphaTransition extends Transition {

    public AlphaTransition(int s0, int s1) {
        super(s0, s1);
    }
}
