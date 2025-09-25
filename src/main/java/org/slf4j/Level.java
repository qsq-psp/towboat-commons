package org.slf4j;

import mujica.reflect.modifier.CodeHistory;

/**
 * Created on 2025/8/10.
 */
@CodeHistory(date = "2025/8/10")
public enum Level {

    TRACE("trace", 0),
    DEBUG("debug", 10),
    INFO("info", 20),
    WARN("warn", 30),
    ERROR("error", 40);

    private static final long serialVersionUID = 0x14678b0c82002902L;

    final String name;

    final int code;

    Level(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
