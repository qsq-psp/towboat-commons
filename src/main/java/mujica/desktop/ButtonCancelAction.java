package mujica.desktop;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

@CodeHistory(date = "2026/10/8")
public class ButtonCancelAction extends AbstractAction {

    public ButtonCancelAction() {
        super("cancel");
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0)); // press escape to interrupt button action
    }

    @Override
    public void actionPerformed(@NotNull ActionEvent actionEvent) {
        final Object source = actionEvent.getSource();
        if (!(source instanceof AbstractButton)) {
            return;
        }
        final ButtonModel model = ((AbstractButton) source).getModel();
        model.setArmed(false);
        model.setPressed(false); // will not trigger action
    }
}
