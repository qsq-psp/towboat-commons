package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

@CodeHistory(date = "2026/9/5")
public class TrayIconTransformer implements JsonContextTransformer<TrayIcon> {

    public static final TrayIconTransformer INSTANCE = new TrayIconTransformer();

    @Override
    public void transform(TrayIcon trayIcon, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            String toolTip = trayIcon.getToolTip();
            if (toolTip != null && !toolTip.isEmpty()) {
                out.key("toolTip");
                out.stringValue(toolTip);
            }
        }
        {
            out.key("imageAutoSize");
            out.booleanValue(trayIcon.isImageAutoSize());
        }
        {
            String actionCommand = trayIcon.getActionCommand();
            if (actionCommand != null && !actionCommand.isEmpty()) {
                out.key("actionCommand");
                out.stringValue(actionCommand);
            }
        }
        {
            Dimension dimension = trayIcon.getSize();
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
