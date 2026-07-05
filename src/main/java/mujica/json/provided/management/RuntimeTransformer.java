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
            out.stringKey(BufferPoolTransformer.NAME);
            out.stringValue(in.getName());
            out.stringKey("vmName");
            out.stringValue(in.getVmName());
            out.stringKey("vmVendor");
            out.stringValue(in.getVmVendor());
            out.stringKey("vmVersion");
            out.stringValue(in.getVmVersion());
            out.stringKey("specName");
            out.stringValue(in.getSpecName());
            out.stringKey("specVendor");
            out.stringValue(in.getSpecVendor());
            out.stringKey("specVersion");
            out.stringValue(in.getSpecVersion());
            out.stringKey("managementSpecVersion");
            out.stringValue(in.getManagementSpecVersion());
            out.stringKey("classPath");
            out.stringValue(in.getClassPath());
            out.stringKey("libraryPath");
            out.stringValue(in.getLibraryPath());
        }
        if (in.isBootClassPathSupported()) {
            out.stringKey("bootClassPath");
            out.stringValue(in.getBootClassPath());
        }
        {
            out.stringKey("inputArguments");
            out.openArray();
            for (String argument : in.getInputArguments()) {
                out.stringValue(argument);
            }
            out.closeArray();
        }
        {
            out.stringKey("upTime");
            out.numberValue(in.getUptime());
            out.stringKey("startTime");
            out.numberValue(in.getStartTime());
        }
        {
            out.stringKey("systemProperty");
            out.openObject();
            for (Map.Entry<String, String> entry : in.getSystemProperties().entrySet()) {
                out.stringKey(entry.getKey());
                out.stringValue(entry.getValue());
            }
            out.closeObject();
        }
        out.closeObject();
    }
}
