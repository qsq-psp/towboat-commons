package mujica.json.provided.desktop;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.modifier.JsonEmpty;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.json.reflect.ReflectConfig;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.imageio.metadata.IIOMetadataNode;

@CodeHistory(date = "2026/9/24")
public class IIOMetadataNodeTransformer implements JsonContextTransformer<IIOMetadataNode> {

    public static final IIOMetadataNodeTransformer INSTANCE = new IIOMetadataNodeTransformer();

    static final FastString VALUE = new FastString("value");

    static final FastString CHILDREN = new FastString("children");

    @Override
    public void transform(@NotNull IIOMetadataNode node, @NotNull JsonHandler out, @Nullable JsonContext context) {
        if (context == null) {
            context = new JsonContext();
        }
        if (context.addContainerObject(node)) {
            try {
                out.openObject();
                {
                    String name = node.getNodeName();
                    if (name != null) {
                        out.key(DataFlavorTransformer.NAME);
                        out.stringValue(name);
                    }
                }
                {
                    String value = node.getNodeValue();
                    if (value != null) {
                        out.key(VALUE);
                        out.stringValue(value);
                    }
                }
                {
                    boolean hasChild = false;
                    Object object = node.getFirstChild();
                    while (object instanceof IIOMetadataNode) {
                        if (!hasChild) {
                            out.key(CHILDREN);
                            out.openArray();
                            hasChild = true;
                        }
                        IIOMetadataNode child = (IIOMetadataNode) object;
                        transform(child, out, context);
                        object = child.getNextSibling();
                    }
                    if (hasChild) {
                        out.closeArray();
                    }
                }
                out.closeObject();
            } finally {
                context.removeContainerObject(node);
            }
        } else if (context.any(((long) JsonEmpty.FROM_LOOP_OBJECT) << ReflectConfig.UNDEFINED_SHIFT)) {
            context.getLogger().debug("image IO metadata node loop to undefined");
            out.skippedValue();
        } else if (context.any(((long) JsonEmpty.FROM_LOOP_OBJECT) << ReflectConfig.NULL_SHIFT)) {
            context.getLogger().debug("image IO metadata node loop to null");
            out.nullValue();
        } else {
            throw new RuntimeException("image IO metadata node loop");
        }
    }
}
