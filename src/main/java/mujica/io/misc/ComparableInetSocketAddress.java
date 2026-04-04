package mujica.io.misc;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;

@CodeHistory(date = "2026/1/11", project = "Ultramarine")
@CodeHistory(date = "2026/3/12")
public class ComparableInetSocketAddress extends ComparableSocketAddress {

    private static final long serialVersionUID = 0x18ae2c0de49d1707L;

    @Nullable
    final InetSocketAddress socketAddress;

    @NotNull
    final ComparableInetAddress inetAddress;

    final int port;

    public ComparableInetSocketAddress(@NotNull InetSocketAddress socketAddress) {
        super();
        this.socketAddress = socketAddress;
        this.inetAddress = ComparableInetAddress.of(socketAddress.getAddress());
        this.port = socketAddress.getPort();
    }

    public ComparableInetSocketAddress(@NotNull ComparableInetAddress inetAddress, int port) {
        super();
        this.socketAddress = null;
        this.inetAddress = inetAddress;
        this.port = port;
    }

    @Nullable
    @Override
    public InetSocketAddress getSocketAddress() {
        return socketAddress;
    }

    @Override
    public int compareTo(@NotNull ComparableSocketAddress that) {
        return that.compareFromInet(this);
    }

    @Override
    int compareFromLocal(@NotNull ComparableLocalSocketAddress that) {
        return -1;
    }

    @Override
    int compareFromDomain(@NotNull ComparableDomainSocketAddress that) {
        return -1;
    }

    @Override
    int compareFromInet(@NotNull ComparableInetSocketAddress that) {
        final int result = this.inetAddress.compareTo(that.inetAddress);
        if (result != 0) {
            return result;
        } else {
            return Integer.compare(this.port, that.port);
        }
    }
}
