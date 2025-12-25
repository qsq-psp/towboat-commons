package mujica.ds.proto;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2025/8/31")
public enum ProtoOperationResult {

    DERIVE, REDUCE, MODIFIED, UNKNOWN, REMAIN;

    private static final long serialVersionUID = 0x58edcb7be0492cf9L;
}
