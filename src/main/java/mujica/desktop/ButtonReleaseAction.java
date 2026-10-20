package mujica.desktop;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferenceCode;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;

@CodeHistory(date = "2026/10/8")
@ReferenceCode(groupId = "JDK", artifactId = "java.base", version = "12", fullyQualifiedName = "javax.swing.plaf.basic.BasicButtonListener.Actions")
public class ButtonReleaseAction extends AbstractAction {

    public ButtonReleaseAction() {
        super("released"); // the name is released, not release, to keep consistent with reference code
    }

    @Override
    public void actionPerformed(@NotNull ActionEvent actionEvent) {
        final Object source = actionEvent.getSource();
        if (!(source instanceof AbstractButton)) {
            return;
        }
        final ButtonModel model = ((AbstractButton) source).getModel();
        model.setPressed(false); // will trigger action if armed
        model.setArmed(false);
    }
}
