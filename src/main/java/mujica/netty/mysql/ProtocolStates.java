package mujica.netty.mysql;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ConstantComposition;
import mujica.reflect.modifier.ConstantInterface;

@CodeHistory(date = "2024/6/16", project = "netty-mysql-connector")
@CodeHistory(date = "2026/5/26")
@ConstantInterface(composition = ConstantComposition.NEVER)
public interface ProtocolStates extends ServerCommands {

    int HANDSHAKE = -3;
    int AUTH = -2;
    int IDLE = -1;

    int QUERY_COLUMNS = 301;
    int QUERY_ROWS = 302;
    int STATEMENT_PREPARE_COLUMNS = 2201;
    int STATEMENT_EXECUTE_COLUMNS = 2301;
    int STATEMENT_EXECUTE_ROWS = 2302;

    int SYN = 10001;
    int SSL = 10002;
}
