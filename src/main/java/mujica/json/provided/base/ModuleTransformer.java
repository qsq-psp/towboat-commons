package mujica.json.provided.base;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.handler.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2022/1/9", project = "infrastructure", name = "ModuleValue")
@CodeHistory(date = "2026/4/28")
public class ModuleTransformer implements JsonContextTransformer<Module>, JsonStructure {

    public static final ModuleTransformer INSTANCE = new ModuleTransformer();

    static final FastString PACKAGES = new FastString("packages");

    @Override
    public void transform(Module in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            String name = in.getName();
            if (name != null) {
                out.stringKey(ClassLoaderTransformer.NAME);
                out.stringValue(in.getName());
            }
            out.stringKey(PACKAGES);
            out.openArray();
            for (String packageName : in.getPackages()) {
                out.stringValue(packageName);
            }
            out.closeArray();
        }
        out.closeObject();
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        transform(getClass().getModule(), jh, null);
    }
}
