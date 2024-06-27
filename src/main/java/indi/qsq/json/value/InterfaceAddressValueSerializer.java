package indi.um.json.value;

import indi.um.json.api.JsonConsumer;
import indi.um.json.api.ValueSerializer;
import indi.um.json.reflect.ConversionConfig;
import indi.um.json.reflect.JsonSerializer;
import org.jetbrains.annotations.NotNull;

import java.net.InterfaceAddress;

/**
 * Created on 2022/7/16.
 */
public class InterfaceAddressValueSerializer implements ValueSerializer<InterfaceAddress> {

    public static final InterfaceAddressValueSerializer INSTANCE = new InterfaceAddressValueSerializer();

    @Override
    public void serialize(String key, InterfaceAddress value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        jc.openObject();
        jc.optionalObjectEntry("address", value.getAddress());
        jc.optionalObjectEntry("broadcast", value.getBroadcast());
        jc.key("prefixLength");
        jc.numberValue(value.getNetworkPrefixLength());
        jc.closeObject();
    }
}
