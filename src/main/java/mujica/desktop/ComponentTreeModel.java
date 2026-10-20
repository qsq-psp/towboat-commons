package mujica.desktop;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;

@CodeHistory(date = "2020/3/24", project = "coo")
@CodeHistory(date = "2026/10/2")
public class ComponentTreeModel implements TreeModel {

    @NotNull
    final EventListenerList listenerList = new EventListenerList();

    @NotNull
    final Component root;

    public ComponentTreeModel(@NotNull Component root) {
        super();
        this.root = root;
    }

    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public Object getChild(Object parent, int index) {
        if (parent instanceof Container) {
            return ((Container) parent).getComponent(index);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int getChildCount(Object parent) {
        if (parent instanceof Container) {
            return ((Container) parent).getComponentCount();
        } else {
            return 0;
        }
    }

    @Override
    public boolean isLeaf(Object node) {
        return !(node instanceof Container);
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        // pass
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if (parent instanceof Container && child instanceof Component) {
            if (((Component) child).getParent() == parent) {
                Component[] components = ((Container) parent).getComponents();
                int count = components.length;
                for (int index = 0; index < count; index++) {
                    if (components[index] == child) {
                        return index;
                    }
                }
            }
        }
        return -1;
    }

    @Override
    public void addTreeModelListener(TreeModelListener listener) {
        listenerList.add(TreeModelListener.class, listener);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener listener) {
        listenerList.remove(TreeModelListener.class, listener);
    }
}
