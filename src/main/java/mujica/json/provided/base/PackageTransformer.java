package mujica.json.provided.base;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/9/19.
 */
public class PackageTransformer implements JsonContextTransformer<Package> {

    public static final PackageTransformer INSTANCE = new PackageTransformer();

    static final FastString SEALED = new FastString("sealed");

    @Override
    public void transform(Package in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.key(ClassLoaderTransformer.NAME);
            out.stringValue(in.getName());
        }
        {
            String string = in.getSpecificationTitle();
            if (string != null) {
                out.key("specificationTitle");
                out.stringValue(string);
            }
            string = in.getSpecificationVersion();
            if (string != null) {
                out.key("specificationVersion");
                out.stringValue(string);
            }
            string = in.getSpecificationVendor();
            if (string != null) {
                out.key("specificationVendor");
                out.stringValue(string);
            }
            string = in.getImplementationTitle();
            if (string != null) {
                out.key("implementationTitle");
                out.stringValue(string);
            }
            string = in.getImplementationVersion();
            if (string != null) {
                out.key("implementationVersion");
                out.stringValue(string);
            }
            string = in.getImplementationVendor();
            if (string != null) {
                out.key("implementationVendor");
                out.stringValue(string);
            }
        }
        {
            out.key(SEALED);
            out.booleanValue(in.isSealed());
        }
        out.closeObject();
    }
}
