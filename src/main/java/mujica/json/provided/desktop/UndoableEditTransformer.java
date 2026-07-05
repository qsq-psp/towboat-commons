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
            out.stringKey("canUndo");
            out.booleanValue(edit.canUndo());
            out.stringKey("canRedo");
            out.booleanValue(edit.canRedo());
            out.stringKey("significant");
            out.booleanValue(edit.isSignificant());
            out.stringKey("name");
            out.stringValue(edit.getPresentationName());
            out.stringKey("undoName");
            out.stringValue(edit.getUndoPresentationName());
            out.stringKey("redoName");
            out.stringValue(edit.getRedoPresentationName());
        }
        out.closeObject();
    }
}
