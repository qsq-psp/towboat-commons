package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;

@CodeHistory(date = "2026/9/25")
public class IIOMetadataTransformer implements JsonContextTransformer<IIOMetadata> {

    public static final IIOMetadataTransformer INSTANCE = new IIOMetadataTransformer();

    @Override
    public void transform(IIOMetadata metadata, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.key("standardSupported");
            out.booleanValue(metadata.isStandardMetadataFormatSupported());
            out.key("readOnly");
            out.booleanValue(metadata.isReadOnly());
            String nativeKey = metadata.getNativeMetadataFormatName();
            if (nativeKey != null) {
                out.key("nativeKey");
                out.stringValue(nativeKey);
            }
        }
        {
            String[] keys = metadata.getMetadataFormatNames();
            if (keys != null) {
                out.key("nodes");
                out.openObject();
                for (String key : keys) {
                    Object node = metadata.getAsTree(key);
                    if (node instanceof IIOMetadataNode) {
                        out.key(key);
                        IIOMetadataNodeTransformer.INSTANCE.transform((IIOMetadataNode) node, out, context);
                    }
                }
                out.closeObject();
            }
        }
        {
            out.key("hasController");
            out.booleanValue(metadata.hasController());
        }
        out.closeObject();
    }
}
