package mujica.desktop;

import mujica.reflect.modifier.CodeHistory;

import javax.swing.*;

@CodeHistory(date = "2026/10/17")
public class EveryThingMenuBar extends LazyMenuBar {

    @Override
    public void load() {
        add(buildBaseMenu());
        add(buildDesktopMenu());
        add(buildStyleMenu());
        super.load();
    }

    private JMenu buildBaseMenu() {
        final JMenu menu = new JMenu("Base");
        menu.add(new JMenuItem(new LazyContentPane.OpenAction("mujica.desktop.LocaleContentTreesPane")));
        return menu;
    }

    private JMenu buildDesktopMenu() {
        final JMenu menu = new JMenu("Desktop");
        return menu;
    }

    private JMenu buildStyleMenu() {
        final JMenu menu = new JMenu("Style");
        menu.add(new JMenuItem(new SetLookAndFeelAction("com.sun.java.swing.plaf.windows.WindowsLookAndFeel")));
        menu.add(new JMenuItem(new SetLookAndFeelAction("MetalLookAndFeel (DefaultMetalTheme)", "javax.swing.plaf.metal.MetalLookAndFeel", "javax.swing.plaf.metal.DefaultMetalTheme")));
        menu.add(new JMenuItem(new SetLookAndFeelAction("MetalLookAndFeel (OceanTheme)", "javax.swing.plaf.metal.MetalLookAndFeel", "javax.swing.plaf.metal.OceanTheme")));
        menu.add(new JMenuItem(new SetLookAndFeelAction("javax.swing.plaf.synth.SynthLookAndFeel")));
        menu.add(new JMenuItem(new SetLookAndFeelAction("javax.swing.plaf.nimbus.NimbusLookAndFeel")));
        menu.add(new JMenuItem(new SetLookAndFeelAction("com.sun.java.swing.plaf.motif.MotifLookAndFeel")));
        return menu;
    }
}
