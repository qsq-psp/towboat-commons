package mujica.json.provided.base;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.module.ModuleDescriptor;
import java.util.Optional;

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
    public void transform(@NotNull ModuleDescriptor md, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(ClassLoaderTransformer.NAME);
            out.stringValue(md.name());
            out.key(OPEN);
            out.booleanValue(md.isOpen());
            out.key(AUTOMATIC);
            out.booleanValue(md.isAutomatic());
            out.key(REQUIRES);
            out.openArray();
            for (ModuleDescriptor.Requires requires : md.requires()) {
                out.key(ClassLoaderTransformer.NAME);
                out.stringValue(requires.name());
                optionalStringValue(VERSION, requires.rawCompiledVersion(), out);
            }
            out.closeArray();
            out.key(EXPORTS);
            out.openArray();
            for (ModuleDescriptor.Exports exports : md.exports()) {
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
            for (ModuleDescriptor.Opens opens : md.opens()) {
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
            for (String uses : md.uses()) {
                out.stringValue(uses);
            }
            out.closeArray();
            out.key(PROVIDES);
            out.openArray();
            for (ModuleDescriptor.Provides provides : md.provides()) {
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
            optionalStringValue(VERSION, md.rawVersion(), out);
            optionalStringValue(MAIN_CLASS, md.mainClass(), out);
            out.key(PACKAGES);
            out.openArray();
            for (String packageName : md.packages()) {
                out.stringValue(packageName);
            }
            out.closeArray();
        }
        out.closeObject();
    }


}
