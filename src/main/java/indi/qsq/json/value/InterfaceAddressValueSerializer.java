package indi.qsq.json.value;

import indi.qsq.json.api.JsonConsumer;
import indi.qsq.json.api.ValueSerializer;
import indi.qsq.json.reflect.ConversionConfig;
import indi.qsq.json.reflect.JsonSerializer;
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
