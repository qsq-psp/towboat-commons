package mujica.json.provided.desktop;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyDescriptor;

/**
 * Created on 2026/8/23.
 */
public class PropertyDescriptorTransformer implements JsonContextTransformer<PropertyDescriptor> {

    static final FastString BOUND = new FastString("bound");

    static final FastString CONSTRAINED = new FastString("constrained");

    static final FastString EDITOR = new FastString("editor");

    @Override
    public void transform(PropertyDescriptor in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            BeanDescriptorTransformer.transformExposed(in, out);
            out.key(BOUND);
            out.booleanValue(in.isBound());
            out.key(CONSTRAINED);
            out.booleanValue(in.isConstrained());
        }
        {
            Class<?> clazz = in.getPropertyEditorClass();
            if (clazz != null) {
                out.key(EDITOR);
                out.stringValue(clazz.getSimpleName());
            }
        }
        out.closeObject();
    }
}
