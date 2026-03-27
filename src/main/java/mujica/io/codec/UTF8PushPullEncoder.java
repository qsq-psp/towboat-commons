package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CodeHistory(date = "2026/3/26")
public class UTF8PushPullEncoder extends PushPullEncoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(UTF8PushPullEncoder.class);

    private int buffer;

    public UTF8PushPullEncoder() {
        super();
    }

    @Override
    public void reset() {
        buffer = 0;
    }
}
