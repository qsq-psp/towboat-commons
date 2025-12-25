package mujica.text.regex;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@CodeHistory(date = "2021/10/17", project = "va")
@CodeHistory(date = "2022/3/28", project = "infrastructure", name = "Matcher")
@CodeHistory(date = "2025/10/24")
@Name(value = "nondeterministic finite automata", language = "en")
@Name(value = "非确定性有限状态自动机", language = "zh")
public class NFA {

    public static final int STATE_SRC = 0;
    public static final int STATE_DST = 1;

    public static final int COMPONENT_START = 0;
    public static final int COMPONENT_END = 1;

    protected static final int NONE = Integer.MAX_VALUE;

    protected final ArrayList<EpsilonTransition> epsilon = new ArrayList<>();

    protected final ArrayList<AlphaTransition> alpha = new ArrayList<>();

    protected int stateCount = 2;

    public NFA() {
        super();
    }

    public void add(@NotNull EpsilonTransition t) {
        epsilon.add(t);
    }

    public void add(@NotNull AlphaTransition t) {
        alpha.add(t);
    }

    public int newState() {
        return stateCount++;
    }

    private void reset(int[] state) {
        for (int i = stateCount - 1; i >= 0; i--) {
            state[i] = NONE;
        }
    }

    private int epsilon(int state, @NotNull CursorIntSequence input) {
        boolean loop;
        do {
            loop = false;
            for (EpsilonTransition t : epsilon) {
                if ((state & (1 << t.s0)) != 0 && (state & (1 << t.s1)) == 0 && t.test(input)) {
                    state |= 1 << t.s1;
                    loop = true;
                }
            }
        } while (loop);
        return state;
    }

    private int alpha(int src, int value) {
        int dst = 0;
        for (AlphaTransition t : alpha) {
            if ((src & (1 << t.s0)) != 0 && t.test(value)) {
                dst |= 1 << t.s1;
            }
        }
        return dst;
    }

    private void epsilon(int[] state, @NotNull CursorIntSequence input) {
        boolean loop;
        do {
            loop = false;
            for (EpsilonTransition t : epsilon) {
                int s0 = state[t.s0];
                if (s0 != NONE && state[t.s1] > s0 && t.test(input)) {
                    state[t.s1] = s0;
                    loop = true;
                }
            }
        } while (loop);
    }

    private void alpha(int[] src, int[] dst, int value) {
        for (AlphaTransition t : alpha) {
            int s0 = src[t.s0];
            if (s0 != NONE && dst[t.s1] > s0 && t.test(value)) {
                dst[t.s1] = s0;
            }
        }
    }

    private boolean extinct(int[] state, int gate) {
        for (int i = 2; i < stateCount; i++) {
            if (state[i] <= gate) {
                return false;
            }
        }
        return true;
    }

    public boolean matchB(@NotNull CursorIntSequence input) {
        int state = epsilon(1 << STATE_SRC, input);
        while (input.getCursorIndex() < input.intLength()) {
            state = alpha(state, input.getInt(input.getCursorIndex()));
            input.setCursorIndex(input.getCursorIndex() + 1);
            state = epsilon(state, input);
        }
        return (state & (1 << STATE_DST)) != 0;
    }

    public boolean matchI(@NotNull CursorIntSequence input) {
        int[] a = new int[stateCount];
        int[] b = new int[stateCount];
        reset(a);
        a[STATE_SRC] = 0;
        epsilon(a, input);
        while (input.getCursorIndex() < input.intLength()) {
            reset(b);
            alpha(a, b, input.getInt(input.getCursorIndex()));
            input.setCursorIndex(input.getCursorIndex() + 1);
            int[] c = a;
            a = b;
            b = c;
            epsilon(a, input);
        }
        return a[STATE_DST] != NONE;
    }

    public boolean match(@NotNull CursorIntSequence input) {
        if (stateCount <= Integer.SIZE) {
            return matchB(input);
        } else {
            return matchI(input);
        }
    }
}
