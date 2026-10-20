package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.modifier.JsonEmpty;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.json.reflect.ReflectConfig;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * Created on 2026/9/4.
 */
@CodeHistory(date = "2026/9/4")
public class MenuElementTransformer implements JsonContextTransformer<MenuElement> {

    public static final MenuElementTransformer INSTANCE = new MenuElementTransformer();

    @Override
    public void transform(MenuElement in, @NotNull JsonHandler out, JsonContext context) {
        if (context == null) {
            context = new JsonContext();
        }
        if (context.addContainerObject(in)) {
            try {
                out.openObject();
                if (in instanceof Component) {
                    String name = ((Component) in).getName();
                    if (name != null && !name.isEmpty()) {
                        out.key(DataFlavorTransformer.NAME);
                        out.stringValue(name);
                    }
                    if (in instanceof JComponent) {
                        if (in instanceof AbstractButton) {
                            String text = ((AbstractButton) in).getText();
                            if (text != null && !text.isEmpty()) {
                                out.key("text");
                                out.stringValue(text);
                            }
                            if (in instanceof JCheckBoxMenuItem) {
                                out.key("selected");
                                out.booleanValue(((JCheckBoxMenuItem) in).isSelected());
                            }
                        }
                        String toolTip = ((JComponent) in).getToolTipText();
                        if (toolTip != null && !toolTip.isEmpty()) {
                            out.key("toolTip");
                            out.stringValue(toolTip);
                        }
                    }
                }
                {
                    MenuElement[] subElements = in.getSubElements();
                    if (subElements != null && subElements.length != 0) {
                        out.key("subElements");
                        out.openArray();
                        for (MenuElement subElement : subElements) {
                            if (subElement == null) {
                                out.nullValue();
                            } else {
                                transform(subElement, out, context);
                            }
                        }
                        out.closeArray();
                    }
                }
                out.closeObject();
            } finally {
                context.removeContainerObject(in);
            }
        } else if (context.any(((long) JsonEmpty.FROM_LOOP_OBJECT) << ReflectConfig.UNDEFINED_SHIFT)) {
            context.getLogger().debug("menu loop to undefined");
            out.skippedValue();
        } else if (context.any(((long) JsonEmpty.FROM_LOOP_OBJECT) << ReflectConfig.NULL_SHIFT)) {
            context.getLogger().debug("menu loop to null");
            out.nullValue();
        } else {
            throw new RuntimeException("loop");
        }
    }
}
