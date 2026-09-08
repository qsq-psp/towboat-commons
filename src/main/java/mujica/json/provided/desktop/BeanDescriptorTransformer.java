package mujica.json.provided.desktop;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.beans.BeanDescriptor;
import java.beans.FeatureDescriptor;
import java.util.Objects;

@CodeHistory(date = "2026/8/14")
public class BeanDescriptorTransformer implements JsonContextTransformer<BeanDescriptor> {

    static final FastString DISPLAY_NAME = new FastString("displayName");

    static final FastString SHORT_DESCRIPTION = new FastString("shortDescription");

    static final FastString EXPERT = new FastString("expert");

    static final FastString HIDDEN = new FastString("hidden");

    static final FastString PREFERRED = new FastString("preferred");

    static void transformExposed(@NotNull FeatureDescriptor in, @NotNull JsonHandler out) {
        {
            String name = in.getName();
            if (name != null) {
                out.key(DataFlavorTransformer.NAME);
                out.stringValue(name);
            }
            String displayName = in.getName();
            if (!Objects.equals(name, displayName)) {
                out.key(DISPLAY_NAME);
                out.stringValue(displayName);
            }
            String shortDescription = in.getShortDescription();
            if (!Objects.equals(displayName, shortDescription)) {
                out.key(SHORT_DESCRIPTION);
                out.stringValue(shortDescription);
            }
        }
        out.key(EXPERT);
        out.booleanValue(in.isExpert());
        out.key(HIDDEN);
        out.booleanValue(in.isHidden());
        out.key(PREFERRED);
        out.booleanValue(in.isPreferred());
    }

    static final FastString BEAN_CLASS = new FastString("beanClass");

    static final FastString CUSTOMIZER_CLASS = new FastString("customizerClass");

    @Override
    public void transform(BeanDescriptor in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            transformExposed(in, out);
            Class<?> clazz = in.getBeanClass();
            if (clazz != null) {
                out.key(BEAN_CLASS);
                out.stringValue(clazz.getName());
            }
            clazz = in.getCustomizerClass();
            if (clazz != null) {
                out.key(CUSTOMIZER_CLASS);
                out.stringValue(clazz.getName());
            }
        }
        out.closeObject();
    }
}
