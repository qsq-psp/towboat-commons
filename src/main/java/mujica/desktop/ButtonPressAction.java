package mujica.desktop;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferenceCode;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;

@CodeHistory(date = "2026/10/8")
@ReferenceCode(groupId = "JDK", artifactId = "java.base", version = "12", fullyQualifiedName = "javax.swing.plaf.basic.BasicButtonListener.Actions")
public class ButtonPressAction extends AbstractAction {

    public ButtonPressAction() {
        super("pressed"); // the name is pressed, not press, to keep consistent with reference code
    }

    @Override
    public void actionPerformed(@NotNull ActionEvent actionEvent) {
        final Object source = actionEvent.getSource();
        if (!(source instanceof AbstractButton)) {
            return;
        }
        final ButtonModel model = ((AbstractButton) source).getModel();
        model.setArmed(true);
        model.setPressed(true);
        // different from reference code here: no request focus
    }
}
