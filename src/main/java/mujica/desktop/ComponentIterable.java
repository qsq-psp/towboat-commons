package mujica.desktop;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

@CodeHistory(date = "2026/10/15")
public class ComponentIterable implements Iterable<Component> {

    @NotNull
    final Container container;

    final int count;

    final boolean reverse, visibleOnly;

    public ComponentIterable(@NotNull Container container, boolean reverse, boolean visibleOnly) {
        super();
        this.container = container;
        this.count = container.getComponentCount();
        this.reverse = reverse;
        this.visibleOnly = visibleOnly;
    }

    @NotNull
    @Override
    public Iterator<Component> iterator() {
        return new Iterator<>() {

            int index = reverse ? count - 1 : 0;

            @Override
            public boolean hasNext() {
                if (reverse) {
                    if (index >= 0) {
                        if (visibleOnly) {
                            do {
                                Component component = container.getComponent(index);
                                if (component.isVisible()) {
                                    return true;
                                }
                                index--;
                            } while (index >= 0);
                        } else {
                            return true;
                        }
                    }
                } else {
                    if (index < count) {
                        if (visibleOnly) {
                            do {
                                Component component = container.getComponent(index);
                                if (component.isVisible()) {
                                    return true;
                                }
                                index++;
                            } while (index < count);
                        } else {
                            return true;
                        }
                    }
                }
                return false;
            }

            @Override
            public Component next() {
                if (reverse) {
                    while (index >= 0) {
                        Component component = container.getComponent(index);
                        if (!visibleOnly || component.isVisible()) {
                            return component;
                        }
                        index--;
                    }
                } else {
                    while (index < count) {
                        Component component = container.getComponent(index);
                        if (!visibleOnly || component.isVisible()) {
                            return component;
                        }
                        index++;
                    }
                }
                throw new NoSuchElementException();
            }
        };
    }
}
