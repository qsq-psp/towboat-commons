package mujica.ds.i32.map;

import mujica.ds.base.DataStructure;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import mujica.reflect.modifier.Name;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Mapping from int to int, initially, all 2**32 values map to zero, designed as a counter
 */
@CodeHistory(date = "2023/2/27", project = "Ultramarine")
@CodeHistory(date = "2025/3/25", name = "IntMap")
@CodeHistory(date = "2026/7/8")
@DirectSubclass({IterableI32Map.class})
public abstract class I32Map implements DataStructure {

    @CodeHistory(date = "2025/3/27")
    public interface Entry {

        int getI32Key();

        int getI32Value();
    }

    protected I32Map() {
        super();
    }

    @NotNull
    @Override
    public abstract I32Map duplicate();

    @Override
    public abstract void checkHealth(@NotNull Consumer<RuntimeException> consumer);

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public abstract int getI32(int key);

    public int putI32(int key, int newValue) {
        throw new UnsupportedOperationException();
    }

    public int addI32(int key, int delta) {
        return putI32(key, getI32(key) + delta);
    }

    public int addS32(int key, int delta) {
        return putI32(key, Math.addExact(key, delta));
    }

    public void increaseI32(int key) {
        addI32(key, 1);
    }

    public void increaseS32(int key) {
        addS32(key, 1);
    }

    public void decreaseI32(int key) {
        addI32(key, -1);
    }

    public void decrementS32(int key) {
        addS32(key, -1);
    }

    public void addAll(@NotNull IterableI32Map that) {
        for (Entry entry : that) {
            addI32(entry.getI32Key(), entry.getI32Value());
        }
    }

    public void addAll(@NotNull int[] array) {
        for (int num : array) {
            increaseI32(num);
        }
    }

    public void addAll(@NotNull char[] array) {
        for (int num : array) {
            increaseI32(num);
        }
    }

    public void addChars(@NotNull String string) {
        string.chars().forEach(this::increaseI32);
    }

    public void addCodePoints(@NotNull String string) {
        string.codePoints().forEach(this::increaseI32);
    }

    static final int TYPE_SHIFT = 30;

    static final int TYPE_MASK = 0x3 << TYPE_SHIFT;

    @SuppressWarnings("PointlessBitwiseExpression")
    static final int TYPE_TRIE = 0x0 << TYPE_SHIFT;

    static final int TYPE_ACCEPT = 0x1 << TYPE_SHIFT;

    @Name(value = "build dictionary tree", language = "en")
    @ReferencePage(title = "trie", href = "https://oi-wiki.org/string/trie/")
    public void buildTrie(String[] patterns) {
        int nextState = 1;
        for (String pattern : patterns) {
            int length = pattern.length();
            int state = 0;
            int index = 0;
            while (index < length) {
                int key = TYPE_TRIE | (state << Character.SIZE) | pattern.charAt(index++);
                int value = getI32(key);
                if (value != 0) {
                    state = value;
                } else {
                    state = nextState++;
                    putI32(key, state);
                }
            }
            putI32(TYPE_ACCEPT | state, 1);
        }
        if ((nextState - 1) >> TYPE_SHIFT != 0) {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * @return index in input string array, or -1 when not found
     */
    public int runTrie(@NotNull String string) {
        final int length = string.length();
        int state = 0;
        for (int index = 0; index < length; index++) {
            state = getI32(TYPE_TRIE | (state << Character.SIZE) | string.charAt(index));
            if (state == 0) {
                return -1;
            } else if (getI32(TYPE_ACCEPT | state) != 0) {
                return index;
            }
        }
        return -1;
    }

    static final int TYPE_CHAR = 0x2 << TYPE_SHIFT;

    static final int TYPE_FAIL = 0x3 << TYPE_SHIFT;

    @Name(value = "build Aho-Corasick automaton", language = "en")
    @ReferencePage(title = "AC", href = "https://oi-wiki.org/string/ac-automaton/")
    public void buildAC(@NotNull String[] patterns) {
        int charCount = 0;
        int nextState = 1;
        for (String pattern : patterns) {
            int length = pattern.length();
            int state = 0;
            int index = 0;
            while (index < length) {
                int ch = pattern.charAt(index++);
                int key = TYPE_CHAR | ch;
                if (getI32(key) != 0) {
                    putI32(key, 1);
                    putI32(TYPE_CHAR | (++charCount << Character.SIZE), ch);
                }
                key = TYPE_TRIE | (state << Character.SIZE) | ch;
                int value = getI32(key);
                if (value != 0) {
                    state = value;
                } else {
                    state = nextState++;
                    putI32(key, state);
                }
            }
            putI32(TYPE_ACCEPT | state, 1);
        }
        if (nextState >> (TYPE_SHIFT - Character.SIZE) != 0) { // states are always more than chars, so only check states
            throw new IndexOutOfBoundsException();
        }
        final char[] chars = new char[charCount];
        final char[] states = new char[nextState - 1];
        nextState = 0;
        for (int charIndex = 0; charIndex < charCount; charIndex++) {
            int ch = getI32(TYPE_CHAR | ((charIndex + 1) << Character.SIZE));
            chars[charIndex] = (char) ch;
            int state = getI32(TYPE_TRIE | ch);
            if (state != 0) {
                states[nextState++] = (char) state;
            }
        }
        for (int stateIndex = 0; stateIndex < nextState; stateIndex++) {
            int state = states[stateIndex];
            int fail = getI32(TYPE_FAIL | state);
            if (fail != 0 && getI32(TYPE_ACCEPT | fail) != 0) {
                putI32(TYPE_ACCEPT | state, 1);
            }
            for (int charIndex = 0; charIndex < charCount; charIndex++) {
                int ch = chars[charIndex];
                int key1 = TYPE_TRIE | (state << Character.SIZE) | ch;
                int state1 = getI32(key1);
                int state2 = getI32(TYPE_TRIE | (fail << Character.SIZE) | ch);
                if (state1 != 0) {
                    putI32(TYPE_FAIL | state1, state2);
                    states[nextState++] = (char) state1;
                } else {
                    putI32(key1, state2);
                }
            }
        }
        assert nextState == states.length;
    }

    @NotNull
    public byte[] runAC(@NotNull String string) {
        final int length = string.length();
        final byte[] result = new byte[length];
        int state = 0;
        for (int index = 0; index < length; index++) {
            state = getI32(TYPE_TRIE | (state << Character.SIZE) | string.charAt(index));
            result[index] = (byte) getI32(TYPE_ACCEPT | state);
        }
        return result;
    }

    @Name(value = "build suffix automaton", language = "en")
    @ReferencePage(title = "SAM", href = "https://oi-wiki.org/string/sam/")
    public void buildSAM(String pattern) {
        //
    }
}
