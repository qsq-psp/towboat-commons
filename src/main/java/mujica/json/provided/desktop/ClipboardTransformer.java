package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;

@CodeHistory(date = "2026/9/27")
public class ClipboardTransformer implements JsonContextTransformer<Clipboard> {

    public static final ClipboardTransformer INSTANCE = new ClipboardTransformer();

    @Override
    public void transform(Clipboard in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            String name = in.getName();
            if (name != null) {
                out.key(DataFlavorTransformer.NAME);
                out.stringValue(name);
            }
        }
        {
            DataFlavor[] dataFlavors = in.getAvailableDataFlavors();
            if (dataFlavors != null && dataFlavors.length != 0) {
                out.key("dataFlavors");
                out.openArray();
                for (DataFlavor dataFlavor : dataFlavors) {
                    DataFlavorTransformer.INSTANCE.transform(dataFlavor, out, context);
                }
                out.closeArray();
            }
        }
        out.closeObject();
    }
}
