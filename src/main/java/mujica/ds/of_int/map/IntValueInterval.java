package mujica.ds.of_int.map;

import mujica.ds.of_int.set.Interval;
import mujica.ds.of_int.IntSlot;
import mujica.reflect.modifier.CodeHistory;

/**
 * Created on 2026/1/17.
 */
@CodeHistory(date = "2026/1/17")
public interface IntValueInterval extends Interval, IntSlot {

    @Override
    int getLeft();

    @Override
    int getRight();

    int getInt();

    int setInt(int newValue);
}
