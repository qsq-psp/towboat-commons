package mujica.io.misc;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.net.SocketAddress;

@CodeHistory(date = "2026/1/11", project = "Ultramarine")
@CodeHistory(date = "2026/3/12")
public abstract class ComparableSocketAddress implements Comparable<ComparableSocketAddress>, Serializable {

    private static final long serialVersionUID = 0x5946bf383873f842L;

    @Nullable
    public abstract SocketAddress getSocketAddress();

    @Override
    public abstract int compareTo(@NotNull ComparableSocketAddress that);

    abstract int compareFromLocal(@NotNull ComparableLocalSocketAddress that);

    abstract int compareFromDomain(@NotNull ComparableDomainSocketAddress that);

    abstract int compareFromInet(@NotNull ComparableInetSocketAddress that);
}
