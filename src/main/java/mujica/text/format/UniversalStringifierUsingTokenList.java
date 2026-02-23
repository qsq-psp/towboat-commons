package mujica.text.format;

import mujica.reflect.modifier.AccessStructure;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@CodeHistory(date = "2026/2/2")
public class UniversalStringifierUsingTokenList implements UniversalStringifier {

    @AccessStructure(online = false, local = true)
    @NotNull
    final ArrayList<Object> tokens = new ArrayList<>();

    @NotNull
    final UniversalAppender appender;

    public UniversalStringifierUsingTokenList(@NotNull UniversalAppender appender) {
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
    public String apply(@NotNull Object object) {
        try {
            appender.addTokens(object, tokens);
            return mergeTokens().toString();
        } finally {
            tokens.clear();
        }
    }
}
