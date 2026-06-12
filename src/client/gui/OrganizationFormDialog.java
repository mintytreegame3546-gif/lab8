package client.gui;

import data.Address;
import data.Coordinates;
import data.Organization;
import data.OrganizationType;

import java.awt.GridLayout;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public final class OrganizationFormDialog {
    private OrganizationFormDialog() {
    }

    public static Optional<Organization> show(JFrame owner, LocaleManager localeManager, Organization current) {
        JTextField name = new JTextField(current == null ? "" : current.getName());
        JTextField x = new JTextField(current == null ? "0" : String.valueOf(current.getCoordinates().getX()));
        JTextField y = new JTextField(current == null ? "0" : String.valueOf(current.getCoordinates().getY()));
        JTextField turnover = new JTextField(current == null ? "0" : String.valueOf(current.getAnnualTurnover()));
        JComboBox<OrganizationType> type = new JComboBox<>(OrganizationType.values());
        if (current != null) type.setSelectedItem(current.getType());
        JTextField street = new JTextField(current == null ? "" : current.getOfficialAddress().getStreet());
        JTextField zip = new JTextField(current == null ? "" : current.getOfficialAddress().getZipCode());

        JPanel panel = new JPanel(new GridLayout(0, 2, 6, 6));
        add(panel, localeManager.text("table.name"), name);
        add(panel, localeManager.text("table.x"), x);
        add(panel, localeManager.text("table.y"), y);
        add(panel, localeManager.text("table.turnover"), turnover);
        add(panel, localeManager.text("table.type"), type);
        add(panel, localeManager.text("table.street"), street);
        add(panel, localeManager.text("table.zip"), zip);

        int result = JOptionPane.showConfirmDialog(owner, panel, localeManager.text("dialog.form"),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return Optional.empty();
        try {
            return Optional.of(new Organization(current == null ? 0 : current.getId(), name.getText().trim(),
                    new Coordinates(Long.parseLong(x.getText().trim()), Double.parseDouble(y.getText().trim())),
                    current == null ? LocalDateTime.now() : current.getCreationDate(),
                    Float.parseFloat(turnover.getText().trim()), (OrganizationType) type.getSelectedItem(),
                    new Address(street.getText().trim(), zip.getText().trim())));
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(owner, localeManager.message(e.getMessage()), localeManager.text("dialog.form"), JOptionPane.ERROR_MESSAGE);
            return Optional.empty();
        }
    }

    private static void add(JPanel panel, String label, java.awt.Component component) {
        panel.add(new JLabel(label));
        panel.add(component);
    }
}
