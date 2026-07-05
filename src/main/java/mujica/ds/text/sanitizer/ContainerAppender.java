package mujica.ds.text.sanitizer;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/6/1")
@DirectSubclass({SelectiveAppender.class, ConcatAppender.class}) // add ChainAppender
public abstract class ContainerAppender extends CharSequenceAppender {

    @NotNull
    protected final CharSequenceAppender[] appenderArray;

    protected ContainerAppender(@NotNull CharSequenceAppender[] appenderArray) {
        super();
        this.appenderArray = appenderArray;
    }
}
