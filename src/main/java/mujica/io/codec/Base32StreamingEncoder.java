package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;

import java.io.IOException;

@CodeHistory(date = "2025/4/28")
public interface Base32StreamingEncoder extends Base32StreamingCodec, Base32Case {

    boolean isUpperCase();

    void setUpperCase(boolean upper);

    boolean stop() throws IOException;
}
