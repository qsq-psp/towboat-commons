package mujica.ds.i32.list;

import mujica.ds.i8.view.DataView;
import mujica.reflect.modifier.CodeHistory;

/**
 * A simple data read interface like {@link CharSequence}, to be wrapped by {@link DataView}
 */
@CodeHistory(date = "2025/5/11")
public interface IntSequence {

    int intLength();

    int getInt(int index);
}
