package client.gui;

import data.Organization;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public final class VisualizationPanel extends JPanel {
    private final OrganizationTableModel model;
    private final LocaleManager localeManager;
    private final Map<Long, Float> animation = new HashMap<>();
    private List<Organization> lastOrganizations = List.of();

    public VisualizationPanel(OrganizationTableModel model, LocaleManager localeManager) {
        this.model = model;
        this.localeManager = localeManager;
        setBackground(Color.WHITE);
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                findAt(e.getX(), e.getY()).ifPresent(VisualizationPanel.this::showDetails);
            }
        });
        new Timer(16, event -> tick()).start();
    }

    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        List<Organization> organizations = model.organizations();
        rememberNewObjects(organizations);
        for (Organization organization : organizations) paintOrganization(g, organization);
        g.dispose();
    }

    private void rememberNewObjects(List<Organization> organizations) {
        for (Organization organization : organizations) {
            boolean known = lastOrganizations.stream().anyMatch(old -> old.equals(organization));
            if (!known) animation.put(organization.getId(), 0.2f);
        }
        lastOrganizations = List.copyOf(organizations);
    }

    private void paintOrganization(Graphics2D g, Organization organization) {
        RectangleBox box = boxFor(organization);
        float scale = animation.getOrDefault(organization.getId(), 1.0f);
        int width = Math.max(10, Math.round(box.width * scale));
        int height = Math.max(10, Math.round(box.height * scale));
        int x = box.x + (box.width - width) / 2;
        int y = box.y + (box.height - height) / 2;
        g.setColor(colorFor(organization.getOwnerUsername()));
        g.fillRoundRect(x, y, width, height, 16, 16);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        g.drawRoundRect(x, y, width, height, 16, 16);
        g.drawString(organization.getName(), x + 6, y + Math.max(16, height / 2));
    }

    private Optional<Organization> findAt(int x, int y) {
        return model.organizations().stream().filter(organization -> boxFor(organization).contains(x, y)).findFirst();
    }

    private RectangleBox boxFor(Organization organization) {
        long nativeX = organization.getCoordinates().getX() == null ? 0 : organization.getCoordinates().getX();
        double nativeY = organization.getCoordinates().getY() == null ? 0 : organization.getCoordinates().getY();
        int x = Math.floorMod(nativeX, Math.max(1, getWidth() - 80));
        int y = Math.floorMod(Math.round(nativeY), Math.max(1, getHeight() - 80));
        int size = 30 + Math.min(70, Math.round(organization.getAnnualTurnover() / 1000));
        return new RectangleBox(x, y, size, size);
    }

    private Color colorFor(String owner) {
        int hash = owner == null ? 0 : owner.hashCode();
        return Color.getHSBColor(Math.floorMod(hash, 360) / 360f, 0.55f, 0.95f);
    }

    private void showDetails(Organization organization) {
        JOptionPane.showMessageDialog(this, organization.toString(), localeManager.text("dialog.details"),
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void tick() {
        boolean changed = false;
        for (Map.Entry<Long, Float> entry : new HashMap<>(animation).entrySet()) {
            if (entry.getValue() >= 1.0f) animation.remove(entry.getKey());
            else {
                animation.put(entry.getKey(), Math.min(1.0f, entry.getValue() + 0.05f));
                changed = true;
            }
        }
        if (changed) repaint();
    }

    private record RectangleBox(int x, int y, int width, int height) {
        boolean contains(int px, int py) {
            return px >= x && px <= x + width && py >= y && py <= y + height;
        }
    }
}
