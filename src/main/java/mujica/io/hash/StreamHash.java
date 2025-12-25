package mujica.io.hash;

import mujica.io.function.AsyncProcessor;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/5/13.
 */
public interface StreamHash extends AsyncProcessor<DataView>, Hash {

    @Override
    void clear();

    @Override
    void start();

    @Override
    @NotNull
    DataView finish();

    @NotNull
    @Override
    DataView apply(@NotNull DataView input);
}
