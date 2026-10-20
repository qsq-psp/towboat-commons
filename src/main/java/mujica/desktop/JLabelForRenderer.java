package mujica.desktop;

import mujica.reflect.modifier.CodeHistory;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;

@CodeHistory(date = "2026/10/7")
public abstract class JLabelForRenderer extends JLabel {

    @Override // overridden for performance
    public void validate() {
        // pass
    }

    @Override // overridden for performance
    public void invalidate() {
        // pass
    }

    @Override // overridden for performance
    public void repaint() {
        // pass
    }

    @Override // overridden for performance
    public void revalidate() {
        // pass
    }

    @Override // overridden for performance
    public void repaint(long notUsed, int x, int y, int width, int height) {
        // pass
    }

    @Override // overridden for performance
    public void repaint(Rectangle rectangle) {
        // pass
    }

    @Override // overridden for performance
    protected void fireVetoableChange(String propertyName, Object oldValue, Object newValue) {
        // pass
    }

    @Override // overridden for performance
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
        // pass
    }

    @Override // overridden for performance
    public void firePropertyChange(String propertyName, int oldValue, int newValue) {
        // pass
    }
}
