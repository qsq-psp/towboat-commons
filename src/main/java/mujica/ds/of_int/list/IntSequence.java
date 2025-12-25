package mujica.ds.of_int.list;

import mujica.io.hash.DataView;
import mujica.reflect.modifier.CodeHistory;

/**
 * A simple data read interface like {@link CharSequence}, to be wrapped by {@link DataView}
 */
@CodeHistory(date = "2025/5/11")
public interface IntSequence {

    int intLength();

    int getInt(int index);
}
