package mujica.json.provided.base;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.module.ModuleReference;

@CodeHistory(date = "2026/4/29")
public class ModuleReferenceTransformer implements JsonContextTransformer<ModuleReference> {

    public static final ModuleReferenceTransformer INSTANCE = new ModuleReferenceTransformer();

    static final FastString DESCRIPTOR = new FastString("descriptor");

    static final FastString LOCATION = new FastString("location");

    @Override
    public void transform(@NotNull ModuleReference mr, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(DESCRIPTOR);
            ModuleDescriptorTransformer.INSTANCE.transform(mr.descriptor(), out, context);
            mr.location().ifPresent(uri -> {
                out.key(LOCATION);
                out.stringValue(uri.toString());
            });
        }
        out.closeObject();
    }
}
