package mujica.json.provided;

import mujica.json.entity.FastString;
import mujica.json.entity.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.lang.module.ModuleReference;

/**
 * Created on 2026/4/29.
 */
@CodeHistory(date = "2026/4/29")
public class ModuleReferenceTransformer implements JsonContextTransformer<ModuleReference> {

    public static final ModuleReferenceTransformer INSTANCE = new ModuleReferenceTransformer();

    static final FastString DESCRIPTOR = new FastString("descriptor");

    static final FastString LOCATION = new FastString("location");

    @Override
    public void transform(ModuleReference in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(DESCRIPTOR);
            ModuleDescriptorTransformer.INSTANCE.transform(in.descriptor(), out, context);
            in.location().ifPresent(uri -> {
                out.stringKey(LOCATION);
                out.stringValue(uri.toString());
            });
        }
        out.closeObject();
    }
}
