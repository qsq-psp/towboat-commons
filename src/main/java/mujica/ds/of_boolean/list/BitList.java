package mujica.ds.of_boolean.list;

import mujica.ds.DataStructure;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/2/7.
 */
@CodeHistory(date = "2026/2/7")
public interface BitList extends DataStructure, BitSequence {

    @Override
    @NotNull
    BitList duplicate();

    @Override
    int bitLength();

    @Override
    boolean getBit(int index);

    @NotNull
    @Override
    String summaryToString();

    @NotNull
    @Override
    String detailToString();
}
