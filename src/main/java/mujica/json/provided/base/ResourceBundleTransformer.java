package mujica.json.provided.base;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.modifier.JsonEmpty;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.json.reflect.ReflectConfig;
import org.jetbrains.annotations.NotNull;

import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * Created on 2026/6/23.
 */
public class ResourceBundleTransformer implements JsonContextTransformer<ResourceBundle> {

    static final FastString LOCALE = new FastString("locale");

    @Override
    public void transform(ResourceBundle bundle, @NotNull JsonHandler out, JsonContext context) {
        if (context == null) {
            context = new JsonContext();
        }
        if (context.addContainerObject(bundle)) {
            try {
                out.openObject();
                {
                    out.key(ClassLoaderTransformer.CLASS);
                    out.stringValue(bundle.getClass().getName());
                    out.key(ClassLoaderTransformer.NAME);
                    out.stringValue(bundle.getBaseBundleName());
                    out.key(LOCALE);
                    LocaleTransformer.INSTANCE.transform(bundle.getLocale(), out, context);
                    for (Enumeration<String> keyEnumeration = bundle.getKeys(); keyEnumeration.hasMoreElements(); ) {
                        String key = keyEnumeration.nextElement();
                        Object value = bundle.getObject(key);
                        out.key(key);
                        context.transform(value, out);
                    }
                }
                out.closeObject();
            } finally {
                context.removeContainerObject(bundle);
            }
        } else if (context.any(((long) JsonEmpty.FROM_LOOP_OBJECT) << ReflectConfig.UNDEFINED_SHIFT)) {
            context.getLogger().debug("resource bundle loop to undefined");
            out.skippedValue();
        } else if (context.any(((long) JsonEmpty.FROM_LOOP_OBJECT) << ReflectConfig.NULL_SHIFT)) {
            context.getLogger().debug("resource bundle loop to null");
            out.nullValue();
        } else {
            throw new RuntimeException("loop");
        }
    }
}
