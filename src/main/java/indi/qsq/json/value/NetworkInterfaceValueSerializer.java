package indi.um.json.value;

import indi.um.json.api.JsonConsumer;
import indi.um.json.api.JsonStructure;
import indi.um.json.api.ValueSerializer;
import indi.um.json.reflect.ConversionConfig;
import indi.um.json.reflect.JsonSerializer;
import indi.um.util.text.HexCase;
import indi.um.util.text.StringUtility;
import io.netty.util.internal.InternalThreadLocalMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Created in infrastructure on 2022/1/10, named NetworkInterfaceValue.
 * Recreated on 2022/7/16.
 */
@SuppressWarnings("unused")
public class NetworkInterfaceValueSerializer implements ValueSerializer<NetworkInterface>, JsonStructure {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkInterfaceValueSerializer.class);

    public static final NetworkInterfaceValueSerializer INSTANCE = new NetworkInterfaceValueSerializer();

    @Override
    public void serialize(String key, NetworkInterface value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        jc.optionalKey(key);
        jc.openObject();
        jc.key("index");
        jc.numberValue(value.getIndex());
        jc.key("name");
        jc.stringValue(value.getName());
        jc.key("displayName");
        jc.stringValue(value.getDisplayName());
        if (value.getParent() != null) {
            jc.key("parent");
            jc.stringValue(value.getParent().getName());
        }
        if (value.isVirtual()) {
            jc.key("virtual");
            jc.booleanValue(true);
        }
        jc.key("inetAddress");
        jc.openArray();
        for (Enumeration<InetAddress> enumeration = value.getInetAddresses(); enumeration.hasMoreElements();) {
            jc.objectValue(enumeration.nextElement());
        }
        jc.closeArray();
        jc.key("interfaceAddress");
        jc.openArray();
        for (InterfaceAddress address : value.getInterfaceAddresses()) {
            InterfaceAddressValueSerializer.INSTANCE.serialize(null, address, jc, cc, js);
        }
        jc.closeArray();
        try {
            if (value.isLoopback()) {
                jc.key("loopback");
                jc.booleanValue(true);
            }
            int mtu = value.getMTU();
            jc.key("MTU");
            jc.numberValue(mtu);
            jc.optionalStringEntry("hardwareAddress", hardwareAddressToString(value.getHardwareAddress()));
        } catch (SocketException e) {
            LOGGER.warn("{}", value, e);
        }
        jc.closeObject();
    }
    
    @Override
    public void toJson(@NotNull JsonConsumer jc) {
        final ConversionConfig cc = new ConversionConfig();
        final JsonSerializer js = new JsonSerializer();
        jc.openArray();
        try {
            for (Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces(); enumeration.hasMoreElements();) {
                serialize(null, enumeration.nextElement(), jc, cc, js);
            }
        } catch (SocketException ignored) {}
        jc.closeArray();
    }

    public static String hardwareAddressToString(@Nullable byte[] address) {
        if (address == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        final int length = address.length;
        for (int i = 0; i < length; i++) {
            if (i != 0) {
                sb.append('-');
            }
            int octet = address[i];
            hexDigit(sb, octet >> 4);
            hexDigit(sb, octet);
        }
        return sb.toString();
    }

    private static void hexDigit(StringBuilder sb, int nibble) {
        nibble &= 0xf;
        if (nibble < 0xa) {
            nibble += '0';
        } else {
            nibble += HexCase.LOWER;
        }
        sb.append((char) nibble);
    }

    /**
     * @return IP address to access the Internet, or null if not accessible
     */
    public static String ipInternet() {
        return testConnect(null);
    }

    /**
     * @return IP address to access the Internet, or null if not accessible
     */
    public static String ipv4Internet() {
        return testConnect(StandardProtocolFamily.INET);
    }

    /**
     * @return IP address to access the Internet, or null if not accessible
     */
    public static String ipv6Internet() {
        return testConnect(StandardProtocolFamily.INET6);
    }

    private static String testConnect(@Nullable ProtocolFamily family) {
        /*
        try (DatagramSocket datagramSocket = new DatagramSocket()) {
            DatagramPacket packet = new DatagramPacket(new byte[1], 1, new InetSocketAddress("8.8.8.8", 1));
            datagramSocket.send(packet);
            return datagramSocket.getLocalAddress().toString();
        } catch (IOException ignored) {
            return null;
        }
         */
        try (DatagramChannel datagramChannel = openDatagramChannel(family)) {
            datagramChannel.connect(new InetSocketAddress("8.8.8.8", 1));
            SocketAddress address = datagramChannel.getLocalAddress();
            if (address instanceof InetSocketAddress) {
                InetAddress inetAddress = ((InetSocketAddress) address).getAddress();
                if (family == null
                        || family == StandardProtocolFamily.INET && inetAddress instanceof Inet4Address
                        || family == StandardProtocolFamily.INET6 && inetAddress instanceof Inet6Address) {
                    return inetAddress.toString();
                }
            }
        } catch (IOException ignored) {}
        return null;
    }

    private static DatagramChannel openDatagramChannel(@Nullable ProtocolFamily family) throws IOException {
        if (family != null) {
            return DatagramChannel.open(family); // this method do not accept null
        } else {
            return DatagramChannel.open(); // auto select
        }
    }

    private static final Pattern BAD_DISPLAY_NAMES = Pattern.compile("virtual|vmnet|tunnel|miniport|npcap|filter|scheduler|debug", Pattern.CASE_INSENSITIVE);

    /**
     * @return The possible IP in a router device local area network (LAN), or null if none
     */
    public static String ipLan() {
        try {
            for (Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces(); networkInterfaceEnumeration.hasMoreElements();) {
                NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement();
                try {
                    if (
                            networkInterface.isVirtual()
                            || BAD_DISPLAY_NAMES.matcher(networkInterface.getDisplayName()).find()
                            || networkInterface.isLoopback()
                            || !networkInterface.isUp()
                            || networkInterface.getMTU() <= 0
                    ) {
                        continue;
                    }
                    for (Enumeration<InetAddress> addressEnumeration = networkInterface.getInetAddresses(); addressEnumeration.hasMoreElements();) {
                        InetAddress inetAddress = addressEnumeration.nextElement();
                        if (inetAddress.isSiteLocalAddress()) {
                            return inetAddress.toString();
                        }
                    }
                } catch (SocketException ignored) {}
            }
        } catch (SocketException ignored) {}
        return null;
    }

    @NotNull
    public static String[] ip() {
        return address(address -> true);
    }

    @NotNull
    public static String[] ipv4() {
        return address(address -> address instanceof Inet4Address);
    }

    @NotNull
    public static String[] ipv6() {
        return address(address -> address instanceof Inet6Address);
    }

    @NotNull
    private static String[] address(Predicate<InetAddress> predicate) {
        final ArrayList<String> list = InternalThreadLocalMap.get().arrayList();
        try {
            for (Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces(); networkInterfaceEnumeration.hasMoreElements();) {
                NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement();
                try {
                    if (networkInterface.isLoopback() || networkInterface.isVirtual() || !networkInterface.isUp()) {
                        continue;
                    }
                    for (Enumeration<InetAddress> addressEnumeration = networkInterface.getInetAddresses(); addressEnumeration.hasMoreElements();) {
                        InetAddress inetAddress = addressEnumeration.nextElement();
                        if (predicate.test(inetAddress)) {
                            list.add(inetAddress.toString());
                        }
                    }
                } catch (SocketException e) {
                    LOGGER.warn("{}", networkInterface, e);
                }
            }
        } catch (SocketException ignored) {}
        return StringUtility.toArray(list);
    }
}
