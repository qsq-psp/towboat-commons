package mujica.io.misc;

import io.netty.channel.unix.DomainSocketAddress;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/1/11", project = "Ultramarine")
@CodeHistory(date = "2026/3/12")
public class ComparableDomainSocketAddress extends ComparableSocketAddress {

    private static final long serialVersionUID = 0xe6200d17089fcc90L;

    @NotNull
    final DomainSocketAddress address;

    public ComparableDomainSocketAddress(@NotNull DomainSocketAddress address) {
        super();
        this.address = address;
    }

    @NotNull
    @Override
    public DomainSocketAddress getSocketAddress() {
        return address;
    }

    @Override
    public int compareTo(@NotNull ComparableSocketAddress that) {
        return that.compareFromDomain(this);
    }

    @Override
    int compareFromLocal(@NotNull ComparableLocalSocketAddress that) {
        return -1;
    }

    @Override
    int compareFromDomain(@NotNull ComparableDomainSocketAddress that) {
        return that.address.path().compareTo(this.address.path());
    }

    @Override
    int compareFromInet(@NotNull ComparableInetSocketAddress that) {
        return 1;
    }
}
