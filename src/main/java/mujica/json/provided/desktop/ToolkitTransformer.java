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
    public void transform(@NotNull Toolkit toolkit, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        try {
            Dimension screenSize = toolkit.getScreenSize();
            out.key("screenWidth");
            out.numberValue(screenSize.width);
            out.key("screenHeight");
            out.numberValue(screenSize.height);
        } catch (HeadlessException ignore) {}
        try {
            int screenResolution = toolkit.getScreenResolution();
            out.key("screenResolution");
            out.numberValue(screenResolution);
        } catch (HeadlessException ignore) {}
        try {
            ColorModel colorModel = toolkit.getColorModel();
            out.key("colorModel");
            ColorModelTransformer.INSTANCE.transform(colorModel, out, context);
        } catch (HeadlessException ignore) {}
        try {
            boolean lockState = toolkit.getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
            out.key("capsLock");
            out.booleanValue(lockState);
        } catch (UnsupportedOperationException ignore) {}
        try {
            boolean lockState = toolkit.getLockingKeyState(KeyEvent.VK_NUM_LOCK);
            out.key("numLock");
            out.booleanValue(lockState);
        } catch (UnsupportedOperationException ignore) {}
        try {
            boolean lockState = toolkit.getLockingKeyState(KeyEvent.VK_SCROLL_LOCK);
            out.key("scrollLock");
            out.booleanValue(lockState);
        } catch (UnsupportedOperationException ignore) {}
        try {
            boolean lockState = toolkit.getLockingKeyState(KeyEvent.VK_KANA_LOCK);
            out.key("kanaLock");
            out.booleanValue(lockState);
        } catch (UnsupportedOperationException ignore) {}
        {
            out.key("alwaysOnTopSupported");
            out.booleanValue(toolkit.isAlwaysOnTopSupported());
            out.key("modalNoExcludeSupported");
            out.booleanValue(toolkit.isModalExclusionTypeSupported(Dialog.ModalExclusionType.NO_EXCLUDE));
            out.key("modalApplicationExcludeSupported");
            out.booleanValue(toolkit.isModalExclusionTypeSupported(Dialog.ModalExclusionType.APPLICATION_EXCLUDE));
            out.key("modalToolkitExcludeSupported");
            out.booleanValue(toolkit.isModalExclusionTypeSupported(Dialog.ModalExclusionType.TOOLKIT_EXCLUDE));
        }
        out.closeObject();
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        transform(Toolkit.getDefaultToolkit(), jh, null);
    }
}
