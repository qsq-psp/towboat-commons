package mujica.text.format;

import mujica.reflect.modifier.AccessStructure;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@CodeHistory(date = "2026/1/29")
public class CharSequenceOperatorUsingTokenList implements CharSequenceOperator {

    @AccessStructure(online = false, local = true)
    @NotNull
    final ArrayList<Object> tokens = new ArrayList<>();

    @NotNull
    final CharSequenceAppender appender;

    public CharSequenceOperatorUsingTokenList(@NotNull CharSequenceAppender appender) {
        super();
        this.appender = appender;
    }

    private Object mergeTokens() {
        final int size = tokens.size();
        int previousStride = 1;
        int currentStride = 2;
        while (previousStride < size) {
            for (int index = previousStride; index < size; index += currentStride) {
                int parent = index - previousStride;
                tokens.set(parent, tokens.get(parent).toString().concat(tokens.get(index).toString()));
            }
            previousStride = currentStride;
            currentStride <<= 1;
        }
        return tokens.get(0);
    }

    @NotNull
    @Override
    public String apply(@NotNull CharSequence string) {
        try {
            appender.addTokens(string, tokens);
            return mergeTokens().toString();
        } finally {
            tokens.clear();
        }
    }

    @NotNull
    @Override
    public String apply(@NotNull CharSequence string, int startIndex, int endIndex) {
        try {
            appender.addTokens(string, startIndex, endIndex, tokens);
            return mergeTokens().toString();
        } finally {
            tokens.clear();
        }
    }
}
