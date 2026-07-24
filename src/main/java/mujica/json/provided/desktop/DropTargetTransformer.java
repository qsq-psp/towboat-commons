package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;

@CodeHistory(date = "2026/5/27")
public class DropTargetTransformer implements JsonContextTransformer<DropTarget> {

    @Override
    public void transform(@NotNull DropTarget dropTarget, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            int actions = dropTarget.getDefaultActions();
            out.key("actions");
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
            out.key("active");
            out.booleanValue(dropTarget.isActive());
        }
        out.closeObject();
    }
}
