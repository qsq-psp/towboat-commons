package mujica.json.provided.management;

import mujica.json.entity.FastString;
import mujica.json.entity.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;

import java.lang.management.OperatingSystemMXBean;

/**
 * Created on 2026/4/24.
 */
public class OperatingSystemTransformer implements JsonContextTransformer<OperatingSystemMXBean> {

    public static final OperatingSystemTransformer INSTANCE = new OperatingSystemTransformer();

    static final FastString NAME = new FastString("name");

    @Name(value = "architecture", language = "en")
    @Name(value = "架构", language = "zh")
    static final FastString ARCH = new FastString("arch");

    static final FastString VERSION = new FastString("version");

    static final FastString AVAILABLE_PROCESSORS = new FastString("availableProcessors");

    static final FastString SYSTEM_LOAD_AVERAGE = new FastString("systemLoadAverage");

    @Override
    public void transform(OperatingSystemMXBean in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(NAME);
            out.stringValue(in.getName());
            out.stringKey(ARCH);
            out.stringValue(in.getArch());
            out.stringKey(VERSION);
            out.stringValue(in.getVersion());
            out.stringKey(AVAILABLE_PROCESSORS);
            out.numberValue(in.getAvailableProcessors());
            out.stringKey(SYSTEM_LOAD_AVERAGE);
            out.numberValue(in.getSystemLoadAverage());
        }
        out.closeObject();
    }
}
