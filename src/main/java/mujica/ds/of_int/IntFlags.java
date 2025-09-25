package mujica.ds.of_int;

import mujica.ds.of_boolean.Flags;
import mujica.reflect.modifier.CodeHistory;

import java.util.function.IntSupplier;

@CodeHistory(date = "2018/11/12", project = "coo", name = "ReadFlagPole")
@CodeHistory(date = "2025/5/27")
public interface IntFlags extends IntSupplier, Flags {

    @Override
    int getAsInt();

    @Override
    boolean getBit(int index);

    boolean containsFlag(int flag);

    boolean containsAllFlags(int flags);
}
