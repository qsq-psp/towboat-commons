package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2025/5/1")
public interface Base16StreamingEncoder extends Base16StreamingCodec, Base16Case {

    boolean isUpperCase();

    void setUpperCase(boolean upper);
}
