package mujica.netty.mysql;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferenceCode;
import mujica.reflect.modifier.ReferencePage;

@CodeHistory(date = "2024/5/21", project = "netty-mysql-connector")
@CodeHistory(date = "2026/5/25")
@ReferencePage(title = "", href = "https://dev.mysql.com/doc/dev/mysql-server/latest/my__command_8h.html")
@ReferencePage(title = "", href = "https://dev.mysql.com/doc/dev/mysql-server/latest/my__command_8h_source.html")
@ReferenceCode(groupId = "mysql", artifactId = "mysql-connector-java", version = "8.0.21", fullyQualifiedName = "com.mysql.cj.protocol.a.NativeConstants")
interface ServerCommands {

    int SLEEP = 0;
    int QUIT = 1;
    int INIT_DB = 2;
    int QUERY = 3;
    int FIELD_LIST = 4; // not used; deprecated in MySQL 5.7.11 and MySQL 8.0.0.
    int CREATE_DB = 5; // not used; deprecated?
    int DROP_DB = 6; // not used; deprecated?
    int REFRESH = 7; // not used; deprecated in MySQL 5.7.11 and MySQL 8.0.0.
    int SHUTDOWN = 8; // Deprecated in MySQL 5.7.9 and MySQL 8.0.0.
    int STATISTICS = 9;
    int PROCESS_INFO = 10; // not used; deprecated in MySQL 5.7.11 and MySQL 8.0.0.
    int CONNECT = 11;
    int PROCESS_KILL = 12; // not used; deprecated in MySQL 5.7.11 and MySQL 8.0.0.
    int DEBUG = 13;
    int PING = 14;
    int TIME = 15;
    int DELAYED_INSERT = 16;
    int CHANGE_USER = 17;
    int BINLOG_DUMP = 18;
    int TABLE_DUMP = 19;
    int CONNECT_OUT = 20;
    int REGISTER_SLAVE = 21;
    int STATEMENT_PREPARE = 22;
    int STATEMENT_EXECUTE = 23;
    int STATEMENT_SEND_LONG_DATA = 24; // No response
    int STATEMENT_CLOSE = 25; // No response
    int STATEMENT_RESET = 26;
    int SET_OPTION = 27;
    int STMT_FETCH = 28;
    int DAEMON = 29;
    int BINLOG_DUMP_GTID = 30;
    int RESET_CONNECTION = 31;
}
