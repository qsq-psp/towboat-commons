package mujica.io.hash;

import mujica.reflect.modifier.CodeHistory;

/**
 * Created on 2025/5/14.
 */
@CodeHistory(date = "2025/1/27", project = "OSHI", name = "DigestState")
@CodeHistory(date = "2025/5/14")
public enum HashState {

    CLEARED, STARTED, FINISHED;

    private static final long serialVersionUID = 0x6f5e9813cf460c2aL;
}
