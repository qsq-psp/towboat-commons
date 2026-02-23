package mujica.algebra;

import mujica.reflect.modifier.CodeHistory;

@Deprecated
@CodeHistory(date = "2025/5/19")
public class CpuTimeoutException extends RuntimeException {

    private static final long serialVersionUID = 0xfffbeabf68ef693dL;

    public CpuTimeoutException() {
        super();
    }
}
