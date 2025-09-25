package mujica.text.format;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@CodeHistory(date = "2025/4/16")
class FailureMessage implements Serializable {

    private static final long serialVersionUID = 0x68BEBE5540E556D9L;

    @NotNull
    final String en, zh;

    FailureMessage(@NotNull String en, @NotNull String zh) {
        super();
        this.en = en;
        this.zh = zh;
    }
}
