package client.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import javax.swing.JComponent;
import javax.swing.UIManager;

public final class ThemeManager {
    private boolean dark;

    public boolean isDark() {
        return dark;
    }

    public void setDark(boolean dark, Component root) {
        this.dark = dark;
        apply(root);
    }

    public void apply(Component component) {
        Color background = dark ? new Color(35, 39, 47) : UIManager.getColor("Panel.background");
        Color foreground = dark ? new Color(230, 230, 230) : UIManager.getColor("Panel.foreground");
        paint(component, background, foreground);
        component.repaint();
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
