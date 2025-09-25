package mujica.io.stream;

import mujica.io.function.Base16Case;
import mujica.reflect.modifier.CodeHistory;

/**
 * Created on 2025/5/1.
 */
@CodeHistory(date = "2025/5/1")
public interface Base16StreamingEncoder extends Base16StreamingCodec, Base16Case {

    boolean isUpperCase();

    void setUpperCase(boolean upper);
}
