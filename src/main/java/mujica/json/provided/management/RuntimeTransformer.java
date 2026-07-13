package mujica.json.provided.management;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.management.RuntimeMXBean;
import java.util.Map;

/**
 * Created on 2026/6/17.
 */
public class RuntimeTransformer implements JsonContextTransformer<RuntimeMXBean> {

    public static final RuntimeTransformer INSTANCE = new RuntimeTransformer();

    @Override
    public void transform(@NotNull RuntimeMXBean in, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(BufferPoolTransformer.NAME);
            out.stringValue(in.getName());
            out.key("vmName");
            out.stringValue(in.getVmName());
            out.key("vmVendor");
            out.stringValue(in.getVmVendor());
            out.key("vmVersion");
            out.stringValue(in.getVmVersion());
            out.key("specName");
            out.stringValue(in.getSpecName());
            out.key("specVendor");
            out.stringValue(in.getSpecVendor());
            out.key("specVersion");
            out.stringValue(in.getSpecVersion());
            out.key("managementSpecVersion");
            out.stringValue(in.getManagementSpecVersion());
            out.key("classPath");
            out.stringValue(in.getClassPath());
            out.key("libraryPath");
            out.stringValue(in.getLibraryPath());
        }
        if (in.isBootClassPathSupported()) {
            out.key("bootClassPath");
            out.stringValue(in.getBootClassPath());
        }
        {
            out.key("inputArguments");
            out.openArray();
            for (String argument : in.getInputArguments()) {
                out.stringValue(argument);
            }
            out.closeArray();
        }
        {
            out.key("upTime");
            out.numberValue(in.getUptime());
            out.key("startTime");
            out.numberValue(in.getStartTime());
        }
        {
            out.key("systemProperty");
            out.openObject();
            for (Map.Entry<String, String> entry : in.getSystemProperties().entrySet()) {
                out.key(entry.getKey());
                out.stringValue(entry.getValue());
            }
            out.closeObject();
        }
        out.closeObject();
    }
}
