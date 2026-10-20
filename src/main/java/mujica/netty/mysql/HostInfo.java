package mujica.netty.mysql;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferenceCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2026/10/19")
@ReferenceCode(groupId = "mysql", artifactId = "mysql-connector-java", version = "8.0.21", fullyQualifiedName = "com.mysql.cj.conf.HostInfo")
public class HostInfo {

    @NotNull
    final String host;

    final int port;

    @NotNull
    final String user;

    @Nullable
    final String password;

    public HostInfo(@NotNull String host, int port, @NotNull String user, @Nullable String password) {
        super();
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }
}
