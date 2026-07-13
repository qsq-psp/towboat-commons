package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import javax.swing.undo.UndoableEdit;

@CodeHistory(date = "2026/6/30")
public class UndoableEditTransformer implements JsonContextTransformer<UndoableEdit> {

    public static final UndoableEditTransformer INSTANCE = new UndoableEditTransformer();

    @Override
    public void transform(UndoableEdit edit, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.key("canUndo");
            out.booleanValue(edit.canUndo());
            out.key("canRedo");
            out.booleanValue(edit.canRedo());
            out.key("significant");
            out.booleanValue(edit.isSignificant());
            out.key("name");
            out.stringValue(edit.getPresentationName());
            out.key("undoName");
            out.stringValue(edit.getUndoPresentationName());
            out.key("redoName");
            out.stringValue(edit.getRedoPresentationName());
        }
        out.closeObject();
    }
}
