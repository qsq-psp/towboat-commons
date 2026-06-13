package mujica.json.provided.desktop;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.awt.datatransfer.DataFlavor;

/**
 * Created on 2026/5/19.
 */
public class DataFlavorTransformer implements JsonContextTransformer<DataFlavor> {

    static final FastString MIME = new FastString("mime");

    static final FastString CLASS = new FastString("class");

    static final FastString NAME = new FastString("name");

    @Override
    public void transform(@NotNull DataFlavor in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            String mime = in.getMimeType();
            if (mime != null) {
                out.stringKey(MIME);
                out.stringValue(mime);
            }
        }
        {
            out.stringKey(CLASS);
            out.stringValue(in.getDefaultRepresentationClassAsString());
        }
        {
            String name = in.getHumanPresentableName();
            if (name != null) {
                out.stringKey(NAME);
                out.stringValue(name);
            }
        }
        out.closeObject();
    }
}
