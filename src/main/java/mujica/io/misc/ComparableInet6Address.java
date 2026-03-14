package mujica.io.misc;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.Inet6Address;

@CodeHistory(date = "2026/1/11", project = "Ultramarine")
@CodeHistory(date = "2026/3/11")
public class ComparableInet6Address extends ComparableInetAddress {

    private static final long serialVersionUID = 0xb70f1683f1d7d96aL;

    final Inet6Address address;

    final long high, low;

    private static long read64(byte[] data, int offset) {
        long value = 0L;
        int shift = Long.SIZE;
        while (shift > 0) {
            shift -= Byte.SIZE;
            value |= ((long) data[offset]) << shift;
            offset++;
        }
        return value;
    }

    public ComparableInet6Address(@NotNull Inet6Address address) {
        super();
        this.address = address;
        final byte[] data = address.getAddress();
        this.high = read64(data, 0);
        this.low = read64(data, Long.BYTES);
    }

    @Nullable
    @Override
    public Inet6Address getAddress() {
        return address;
    }

    @Override
    public int compareTo(@NotNull ComparableInetAddress that) {
        return that.compareFrom6(this);
    }

    @Override
    int compareFrom4(@NotNull ComparableInet4Address that) {
        return -1;
    }

    @Override
    int compareFrom6(@NotNull ComparableInet6Address that) {
        final int result = Long.compareUnsigned(this.high, that.high);
        if (result != 0) {
            return result;
        } else {
            return Long.compareUnsigned(this.low, that.low);
        }
    }
}
