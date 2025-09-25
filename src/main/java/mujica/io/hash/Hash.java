package mujica.io.hash;

import mujica.io.view.DataView;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

@CodeHistory(date = "2025/5/12")
public interface Hash extends Function<DataView, DataView> {

    @NotNull
    @Override
    DataView apply(@NotNull DataView input);
}
