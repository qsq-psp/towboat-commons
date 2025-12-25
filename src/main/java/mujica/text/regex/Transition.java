package mujica.text.regex;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2021/10/17", project = "va")
@CodeHistory(date = "2022/3/28", project = "infrastructure")
@CodeHistory(date = "2025/5/22")
public class Transition {

    protected final int s0, s1;

    public Transition(int s0, int s1) {
        super();
        this.s0 = s0;
        this.s1 = s1;
    }
}
