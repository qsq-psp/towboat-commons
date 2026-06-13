package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;

/**
 * Created on 2026/5/27.
 */
public class DropTargetTransformer implements JsonContextTransformer<DropTarget> {

    @Override
    public void transform(@NotNull DropTarget in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            int actions = in.getDefaultActions();
            out.stringKey("actions");
            switch (actions) {
                case DnDConstants.ACTION_NONE:
                    out.stringValue("none");
                    break;
                case DnDConstants.ACTION_COPY:
                    out.stringValue("copy");
                    break;
                case DnDConstants.ACTION_MOVE:
                    out.stringValue("move");
                    break;
                case DnDConstants.ACTION_COPY_OR_MOVE:
                    out.stringValue("copyOrMove");
                    break;
                case DnDConstants.ACTION_LINK:
                    out.stringValue("link");
                    break;
                default:
                    out.numberValue(actions);
                    break;
            }
        }
        {
            out.stringKey("active");
            out.booleanValue(in.isActive());
        }
        out.closeObject();
    }
}
