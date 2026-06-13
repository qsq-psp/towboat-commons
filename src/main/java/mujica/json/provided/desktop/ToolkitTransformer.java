package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.handler.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.ColorModel;

/**
 * Created on 2026/6/2.
 */
public class ToolkitTransformer implements JsonContextTransformer<Toolkit>, JsonStructure {

    public static final ToolkitTransformer INSTANCE = new ToolkitTransformer();

    @Override
    public void transform(@NotNull Toolkit in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        try {
            Dimension screenSize = in.getScreenSize();
            out.stringKey("screenWidth");
            out.numberValue(screenSize.width);
            out.stringKey("screenHeight");
            out.numberValue(screenSize.height);
        } catch (HeadlessException ignore) {}
        try {
            int screenResolution = in.getScreenResolution();
            out.stringKey("screenResolution");
            out.numberValue(screenResolution);
        } catch (HeadlessException ignore) {}
        try {
            ColorModel colorModel = in.getColorModel();
            out.stringKey("colorModel");
            ColorModelTransformer.INSTANCE.transform(colorModel, out, context);
        } catch (HeadlessException ignore) {}
        try {
            boolean lockState = in.getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
            out.stringKey("capsLock");
            out.booleanValue(lockState);
        } catch (UnsupportedOperationException ignore) {}
        try {
            boolean lockState = in.getLockingKeyState(KeyEvent.VK_NUM_LOCK);
            out.stringKey("numLock");
            out.booleanValue(lockState);
        } catch (UnsupportedOperationException ignore) {}
        try {
            boolean lockState = in.getLockingKeyState(KeyEvent.VK_SCROLL_LOCK);
            out.stringKey("scrollLock");
            out.booleanValue(lockState);
        } catch (UnsupportedOperationException ignore) {}
        try {
            boolean lockState = in.getLockingKeyState(KeyEvent.VK_KANA_LOCK);
            out.stringKey("kanaLock");
            out.booleanValue(lockState);
        } catch (UnsupportedOperationException ignore) {}
        {
            out.stringKey("alwaysOnTopSupported");
            out.booleanValue(in.isAlwaysOnTopSupported());
            out.stringKey("modalNoExcludeSupported");
            out.booleanValue(in.isModalExclusionTypeSupported(Dialog.ModalExclusionType.NO_EXCLUDE));
            out.stringKey("modalApplicationExcludeSupported");
            out.booleanValue(in.isModalExclusionTypeSupported(Dialog.ModalExclusionType.APPLICATION_EXCLUDE));
            out.stringKey("modalToolkitExcludeSupported");
            out.booleanValue(in.isModalExclusionTypeSupported(Dialog.ModalExclusionType.TOOLKIT_EXCLUDE));
        }
        out.closeObject();
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        transform(Toolkit.getDefaultToolkit(), jh, null);
    }
}
