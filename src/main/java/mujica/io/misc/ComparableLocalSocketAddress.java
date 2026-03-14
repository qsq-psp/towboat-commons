package mujica.io.misc;

import io.netty.channel.local.LocalAddress;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/1/11", project = "Ultramarine")
@CodeHistory(date = "2026/3/12")
public class ComparableLocalSocketAddress extends ComparableSocketAddress {

    private static final long serialVersionUID = 0x5bf87a39a5b82231L;

    @NotNull
    final LocalAddress address;

    public ComparableLocalSocketAddress(@NotNull LocalAddress address) {
        super();
        this.address = address;
    }

    @Override
    public LocalAddress getSocketAddress() {
        return address;
    }

    @Override
    public int compareTo(@NotNull ComparableSocketAddress that) {
        return that.compareFromLocal(this);
    }

    @Override
    int compareFromLocal(@NotNull ComparableLocalSocketAddress that) {
        return that.address.id().compareTo(this.address.id());
    }

    @Override
    int compareFromDomain(@NotNull ComparableDomainSocketAddress that) {
        return 1;
    }

    @Override
    int compareFromInet(@NotNull ComparableInetSocketAddress that) {
        return 1;
    }
}
