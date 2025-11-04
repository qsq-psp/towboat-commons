package mujica.grammar.regex;

import mujica.reflect.modifier.CodeHistory;

import java.util.ArrayList;

@CodeHistory(date = "2021/10/17", project = "va")
@CodeHistory(date = "2022/3/28", project = "infrastructure", name = "Matcher")
@CodeHistory(date = "2025/10/24")
public class DeterministicFiniteAutomata {

    protected final ArrayList<EpsilonTransition> epsilon = new ArrayList<>();

    protected final ArrayList<AlphaTransition> alpha = new ArrayList<>();
}
