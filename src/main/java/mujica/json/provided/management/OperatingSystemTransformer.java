package mujica.json.provided.management;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.management.OperatingSystemMXBean;

@CodeHistory(date = "2026/4/24")
public class OperatingSystemTransformer implements JsonContextTransformer<OperatingSystemMXBean> {

    public static final OperatingSystemTransformer INSTANCE = new OperatingSystemTransformer();

    @Name(value = "architecture", language = "en")
    @Name(value = "架构", language = "zh")
    static final FastString ARCH = new FastString("arch");

    static final FastString VERSION = new FastString("version");

    static final FastString AVAILABLE_PROCESSORS = new FastString("availableProcessors");

    static final FastString SYSTEM_LOAD_AVERAGE = new FastString("systemLoadAverage");

    @Override
    public void transform(@NotNull OperatingSystemMXBean in, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(BufferPoolTransformer.NAME);
            out.stringValue(in.getName());
            out.key(ARCH);
            out.stringValue(in.getArch());
            out.key(VERSION);
            out.stringValue(in.getVersion());
            out.key(AVAILABLE_PROCESSORS);
            out.numberValue(in.getAvailableProcessors());
            out.key(SYSTEM_LOAD_AVERAGE);
            out.numberValue(in.getSystemLoadAverage());
        }
        out.closeObject();
    }
}
