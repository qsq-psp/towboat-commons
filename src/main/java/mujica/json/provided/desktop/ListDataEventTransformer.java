package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import javax.swing.event.ListDataEvent;

@CodeHistory(date = "2026/9/3")
public class ListDataEventTransformer implements JsonContextTransformer<ListDataEvent> {

    public static final ListDataEventTransformer INSTANCE = new ListDataEventTransformer();

    @Override
    public void transform(ListDataEvent event, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            int type = event.getType();
            out.key(PathIteratorTransformer.TYPE);
            switch (type) {
                case ListDataEvent.CONTENTS_CHANGED:
                    out.stringValue("change");
                    break;
                case ListDataEvent.INTERVAL_ADDED:
                    out.stringValue("add");
                    break;
                case ListDataEvent.INTERVAL_REMOVED:
                    out.stringValue("remove");
                    break;
                default:
                    out.numberValue(type);
                    break;
            }
        }
        {
            out.key("index0");
            out.numberValue(event.getIndex0());
            out.key("index1");
            out.numberValue(event.getIndex1());
        }
        out.closeObject();
    }
}
