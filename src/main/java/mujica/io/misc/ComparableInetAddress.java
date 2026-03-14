package mujica.io.misc;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;

@CodeHistory(date = "2026/1/11", project = "Ultramarine")
@CodeHistory(date = "2026/3/11")
public abstract class ComparableInetAddress implements Comparable<ComparableInetAddress>, Serializable {

    private static final long serialVersionUID = 0x47cf78c3e0c381d1L;

    @NotNull
    public static ComparableInetAddress of(InetAddress address) {
        if (address instanceof Inet4Address) {
            return new ComparableInet4Address((Inet4Address) address);
        }
        if (address instanceof Inet6Address) {
            return new ComparableInet6Address((Inet6Address) address);
        }
        throw new IllegalArgumentException();
    }

    @Nullable
    public abstract InetAddress getAddress();

    @Override
    public abstract int compareTo(@NotNull ComparableInetAddress that);

    abstract int compareFrom4(@NotNull ComparableInet4Address that);

    abstract int compareFrom6(@NotNull ComparableInet6Address that);
}
