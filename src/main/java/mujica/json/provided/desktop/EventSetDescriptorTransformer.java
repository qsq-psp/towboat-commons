package mujica.json.provided.desktop;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.beans.EventSetDescriptor;

/**
 * Created on 2026/8/24.
 */
public class EventSetDescriptorTransformer implements JsonContextTransformer<EventSetDescriptor> {

    static final FastString UNICAST = new FastString("unicast");

    static final FastString IN_DEFAULT = new FastString("inDefault");

    static final FastString LISTENER_TYPE = new FastString("listenerType");

    @Override
    public void transform(EventSetDescriptor in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            BeanDescriptorTransformer.transformExposed(in, out);
            out.key(UNICAST);
            out.booleanValue(in.isUnicast());
            out.key(IN_DEFAULT);
            out.booleanValue(in.isInDefaultEventSet());
        }
        {
            Class<?> clazz = in.getListenerType();
            if (clazz != null) {
                out.key(LISTENER_TYPE);
                out.stringValue(clazz.getSimpleName());
            }
        }
        out.closeObject();
    }
}
