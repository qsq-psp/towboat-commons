package mujica.json.provided.desktop;


import mujica.json.handler.JsonHandler;
import mujica.json.handler.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

@CodeHistory(date = "2026/10/9")
public class LookAndFeelInfoTransformer implements JsonContextTransformer<UIManager.LookAndFeelInfo>, JsonStructure {

    public static final LookAndFeelInfoTransformer INSTANCE = new LookAndFeelInfoTransformer();

    @Override
    public void transform(UIManager.LookAndFeelInfo info, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.key(DataFlavorTransformer.NAME);
            out.stringValue(info.getName());
            out.key(DataFlavorTransformer.CLASS);
            out.stringValue(info.getClassName());
        }
        out.closeObject();
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        final UIManager.LookAndFeelInfo[] array = UIManager.getInstalledLookAndFeels();
        jh.openArray();
        for (UIManager.LookAndFeelInfo info : array) {
            transform(info, jh, null);
        }
        jh.closeArray();
    }
}
