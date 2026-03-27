package mujica.io.misc;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.Inet4Address;

@CodeHistory(date = "2026/1/11", project = "Ultramarine")
@CodeHistory(date = "2026/3/11")
public class ComparableInet4Address extends ComparableInetAddress {

    private static final long serialVersionUID = 0x252e58179aabc2f7L;

    @Nullable
    final Inet4Address address;

    final int value;

    public ComparableInet4Address(@NotNull Inet4Address address) {
        super();
        this.address = address;
        final byte[] data = address.getAddress();
        int value = 0;
        int shift = Integer.SIZE;
        int index = 0;
        while (shift > 0) {
            shift -= Byte.SIZE;
            value |= data[index] << shift;
            index++;
        }
        this.value = value;
    }

    @Override
    @Nullable
    public Inet4Address getAddress() {
        return address;
    }

    @Override
    public int compareTo(@NotNull ComparableInetAddress that) {
        return that.compareFrom4(this);
    }

    @Override
    int compareFrom4(@NotNull ComparableInet4Address that) {
        return Integer.compareUnsigned(this.value, that.value);
    }

    @Override
    int compareFrom6(@NotNull ComparableInet6Address that) {
        return 1;
    }

    @Override
    @NotNull
    public String toString() {
        if (address != null) {
            return "ComparableInet4Address[address = " + address + ", value = " + value + "]";
        } else {
            return "ComparableInet4Address[value = " + value + "]";
        }
    }
}
