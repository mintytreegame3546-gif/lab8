package client.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public final class ThemeManager {
    private static final Color DEFAULT_BACKGROUND = UIManager.getColor("Panel.background");
    private static final Color DEFAULT_FOREGROUND = UIManager.getColor("Panel.foreground");
    private boolean dark;

    public boolean isDark() {
        return dark;
    }

    public void setDark(boolean dark, Component root) {
        this.dark = dark;
        applyDefaults();
        apply(root);
        SwingUtilities.updateComponentTreeUI(root);
    }

    public void apply(Component component) {
        Color background = background();
        Color foreground = foreground();
        paint(component, background, foreground);
        component.repaint();
    }

    public Color background() {
        return dark ? new Color(35, 39, 47) : DEFAULT_BACKGROUND;
    }

    public Color foreground() {
        return dark ? new Color(230, 230, 230) : DEFAULT_FOREGROUND;
    }

    private void applyDefaults() {
        Color background = background();
        Color foreground = foreground();
        for (String key : new String[]{"Panel", "OptionPane", "Menu", "MenuBar", "MenuItem", "PopupMenu",
                "Button", "Label", "TextField", "PasswordField", "ComboBox", "Table", "TableHeader"}) {
            UIManager.put(key + ".background", background);
            UIManager.put(key + ".foreground", foreground);
        }
        UIManager.put("OptionPane.messageForeground", foreground);
        UIManager.put("TextField.caretForeground", foreground);
        UIManager.put("PasswordField.caretForeground", foreground);
    }

    private void paint(Component component, Color background, Color foreground) {
        if (component instanceof JComponent jComponent) jComponent.setOpaque(true);
        component.setBackground(background);
        component.setForeground(foreground);
        if (component instanceof Container container) {
            for (Component child : container.getComponents()) paint(child, background, foreground);
        }
    }
}
