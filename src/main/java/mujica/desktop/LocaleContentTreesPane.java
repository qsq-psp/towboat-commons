package mujica.desktop;

import mujica.reflect.modifier.CodeHistory;

import javax.swing.*;
import java.awt.*;

@CodeHistory(date = "2026/10/13")
public class LocaleContentTreesPane extends LazyContentPane {

    public LocaleContentTreesPane() {
        super();
        setName(getClass().getSimpleName());
        setLayout(new BorderLayout());
    }

    @Override
    public void load() {
        final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.setName("tabbedPane");
        for (LocaleContentTreeModel.ContentType type : LocaleContentTreeModel.ContentType.values()) {
            tabbedPane.addTab(type.displayName, new LocaleContentTreeModel.ContentPane(type));
        }
        add(tabbedPane, BorderLayout.CENTER);
        super.load();
    }

    public static void main(String[] args) {
        loadIntoFrameLater(BoxLayoutPlayground::new);
    }
}
