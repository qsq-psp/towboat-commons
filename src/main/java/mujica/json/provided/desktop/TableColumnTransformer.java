package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import javax.swing.table.TableColumn;

@CodeHistory(date = "2026/10/3")
public class TableColumnTransformer implements JsonContextTransformer<TableColumn> {

    public static final TableColumnTransformer INSTANCE = new TableColumnTransformer();

    @Override
    public void transform(TableColumn in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.key("modelIndex");
            out.numberValue(in.getModelIndex());
            out.key("width");
            out.numberValue(in.getWidth());
            out.key("preferredWidth");
            out.numberValue(in.getPreferredWidth());
            out.key("minWidth");
            out.numberValue(in.getMinWidth());
            out.key("maxWidth");
            out.numberValue(in.getMaxWidth());
            out.key("resizable");
            out.booleanValue(in.getResizable());
        }
        out.closeObject();
    }
}
