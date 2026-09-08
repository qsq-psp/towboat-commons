package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Created on 2026/9/5.
 */
public class TrayIconTransformer implements JsonContextTransformer<TrayIcon> {

    public static final TrayIconTransformer INSTANCE = new TrayIconTransformer();

    @Override
    public void transform(TrayIcon in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            String toolTip = in.getToolTip();
            if (toolTip != null && !toolTip.isEmpty()) {
                out.key("toolTip");
                out.stringValue(toolTip);
            }
        }
        {
            out.key("imageAutoSize");
            out.booleanValue(in.isImageAutoSize());
        }
        {
            String actionCommand = in.getActionCommand();
            if (actionCommand != null && !actionCommand.isEmpty()) {
                out.key("actionCommand");
                out.stringValue(actionCommand);
            }
        }
        {
            Dimension dimension = in.getSize();
            if (dimension != null) {
                out.key("width");
                out.numberValue(dimension.width);
                out.key("height");
                out.numberValue(dimension.height);
            }
        }
        out.closeObject();
    }
}
