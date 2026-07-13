package mujica.json.provided.base;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.lang.module.ModuleDescriptor;
import java.util.Optional;

/**
 * Created on 2026/4/29.
 */
@CodeHistory(date = "2026/4/29")
public class ModuleDescriptorTransformer implements JsonContextTransformer<ModuleDescriptor> {

    public static final ModuleDescriptorTransformer INSTANCE = new ModuleDescriptorTransformer();

    static final FastString VERSION = new FastString("version");

    static final FastString OPEN = new FastString("open");

    static final FastString AUTOMATIC = new FastString("automatic");

    static final FastString MAIN_CLASS = new FastString("mainClass");

    static final FastString SOURCE = new FastString("source");

    static final FastString TARGET = new FastString("target");

    static final FastString SERVICE = new FastString("service");

    static final FastString PROVIDER = new FastString("provider");

    static final FastString REQUIRES = new FastString("requires");

    static final FastString EXPORTS = new FastString("exports");

    static final FastString OPENS = new FastString("opens");

    static final FastString USES = new FastString("uses");

    static final FastString PROVIDES = new FastString("provides");

    static final FastString PACKAGES = new FastString("packages");

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private void optionalStringValue(@NotNull FastString key, @NotNull Optional<String> value, @NotNull JsonHandler out) {
        if (value.isPresent()) {
            out.key(key);
            out.stringValue(value.get());
        }
    }

    @Override
    public void transform(ModuleDescriptor in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.key(ClassLoaderTransformer.NAME);
            out.stringValue(in.name());
            out.key(OPEN);
            out.booleanValue(in.isOpen());
            out.key(AUTOMATIC);
            out.booleanValue(in.isAutomatic());
            out.key(REQUIRES);
            out.openArray();
            for (ModuleDescriptor.Requires requires : in.requires()) {
                out.key(ClassLoaderTransformer.NAME);
                out.stringValue(requires.name());
                optionalStringValue(VERSION, requires.rawCompiledVersion(), out);
            }
            out.closeArray();
            out.key(EXPORTS);
            out.openArray();
            for (ModuleDescriptor.Exports exports : in.exports()) {
                out.key(SOURCE);
                out.stringValue(exports.source());
                out.key(TARGET);
                out.openArray();
                for (String target : exports.targets()) {
                    out.stringValue(target);
                }
                out.closeArray();
            }
            out.closeArray();
            out.key(OPENS);
            out.openArray();
            for (ModuleDescriptor.Opens opens : in.opens()) {
                out.key(SOURCE);
                out.stringValue(opens.source());
                out.key(TARGET);
                out.openArray();
                for (String target : opens.targets()) {
                    out.stringValue(target);
                }
                out.closeArray();
            }
            out.closeArray();
            out.key(USES);
            out.openArray();
            for (String uses : in.uses()) {
                out.stringValue(uses);
            }
            out.closeArray();
            out.key(PROVIDES);
            out.openArray();
            for (ModuleDescriptor.Provides provides : in.provides()) {
                out.key(SERVICE);
                out.stringValue(provides.service());
                out.key(PROVIDER);
                out.openArray();
                for (String provider : provides.providers()) {
                    out.stringValue(provider);
                }
                out.closeArray();
            }
            out.closeArray();
            optionalStringValue(VERSION, in.rawVersion(), out);
            optionalStringValue(MAIN_CLASS, in.mainClass(), out);
            out.key(PACKAGES);
            out.openArray();
            for (String packageName : in.packages()) {
                out.stringValue(packageName);
            }
            out.closeArray();
        }
        out.closeObject();
    }


}
