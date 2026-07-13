package mujica.json.provided.desktop;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.datatransfer.DataFlavor;

/**
 * Created on 2026/5/19.
 */
@CodeHistory(date = "2026/5/19")
public class DataFlavorTransformer implements JsonContextTransformer<DataFlavor> {

    public static final DataFlavorTransformer INSTANCE = new DataFlavorTransformer();

    static final FastString MIME = new FastString("mime");

    static final FastString CLASS = new FastString("class");

    static final FastString NAME = new FastString("name");

    @Override
    public void transform(@NotNull DataFlavor dataFlavor, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            String mime = dataFlavor.getMimeType();
            if (mime != null) {
                out.key(MIME);
                out.stringValue(mime);
            }
        }
        {
            out.key(CLASS);
            out.stringValue(dataFlavor.getDefaultRepresentationClassAsString());
        }
        {
            String name = dataFlavor.getHumanPresentableName();
            if (name != null) {
                out.key(NAME);
                out.stringValue(name);
            }
        }
        out.closeObject();
    }
}
