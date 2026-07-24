package mujica.json.provided.management;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.management.RuntimeMXBean;
import java.util.Map;

@CodeHistory(date = "2026/6/17")
public class RuntimeTransformer implements JsonContextTransformer<RuntimeMXBean> {

    public static final RuntimeTransformer INSTANCE = new RuntimeTransformer();

    @Override
    public void transform(@NotNull RuntimeMXBean bean, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(BufferPoolTransformer.NAME);
            out.stringValue(bean.getName());
            out.key("vmName");
            out.stringValue(bean.getVmName());
            out.key("vmVendor");
            out.stringValue(bean.getVmVendor());
            out.key("vmVersion");
            out.stringValue(bean.getVmVersion());
            out.key("specName");
            out.stringValue(bean.getSpecName());
            out.key("specVendor");
            out.stringValue(bean.getSpecVendor());
            out.key("specVersion");
            out.stringValue(bean.getSpecVersion());
            out.key("managementSpecVersion");
            out.stringValue(bean.getManagementSpecVersion());
            out.key("classPath");
            out.stringValue(bean.getClassPath());
            out.key("libraryPath");
            out.stringValue(bean.getLibraryPath());
        }
        if (bean.isBootClassPathSupported()) {
            out.key("bootClassPath");
            out.stringValue(bean.getBootClassPath());
        }
        {
            out.key("inputArguments");
            out.openArray();
            for (String argument : bean.getInputArguments()) {
                out.stringValue(argument);
            }
            out.closeArray();
        }
        {
            out.key("upTime");
            out.numberValue(bean.getUptime());
            out.key("startTime");
            out.numberValue(bean.getStartTime());
        }
        {
            out.key("systemProperty");
            out.openObject();
            for (Map.Entry<String, String> entry : bean.getSystemProperties().entrySet()) {
                out.key(entry.getKey());
                out.stringValue(entry.getValue());
            }
            out.closeObject();
        }
        out.closeObject();
    }
}
