package client.gui;

import data.Organization;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

@SuppressWarnings("serial")
public final class MainFrame extends JFrame implements Localized {
    private static final int DEFAULT_PORT = 5555;

    private final LocaleManager localeManager = new LocaleManager();
    private final ThemeManager themeManager = new ThemeManager();
    private final OrganizationTableModel tableModel = new OrganizationTableModel(localeManager);
    private final JTable table = new JTable(tableModel);
    private final JTextField filter = new JTextField(20);
    private final JLabel user = new JLabel();
    private final JLabel status = new JLabel();
    private final JLabel total = new JLabel();
    private final JButton add = new JButton();
    private final JButton addIfMin = new JButton();
    private final JButton edit = new JButton();
    private final JButton delete = new JButton();
    private final JButton clear = new JButton();
    private final JButton removeFirst = new JButton();
    private final JButton removeLower = new JButton();
    private final JButton script = new JButton();
    private final JButton info = new JButton();
    private final JButton help = new JButton();
    private final JCheckBoxMenuItem darkTheme = new JCheckBoxMenuItem();
    private final JMenu language = new JMenu();
    private final MainController controller;

    public MainFrame(String host, int port) throws Exception {
        GuiCommandClient client = new GuiCommandClient(host, port);
        LoginDialog loginDialog = new LoginDialog(localeManager, client);
        loginDialog.setVisible(true);
        if (!loginDialog.isAuthorized()) {
            client.close();
            throw new IllegalStateException("Authorization cancelled");
        }
        controller = new MainController(client, organizations -> {
            tableModel.setOrganizations(organizations);
            updateTotal();
            repaint();
        }, status::setText);
        localeManager.addListener(this);
        buildLayout();
        bindActions();
        updateTexts();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1100, 720);
        setLocationRelativeTo(null);
        controller.refresh();
        new Timer(2000, event -> controller.refresh()).start();
    }

    public static void launch(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_PORT;
        SwingUtilities.invokeLater(() -> {
            try {
                new MainFrame(host, port).setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        });
    }

    public void dispose() {
        controller.close();
        super.dispose();
    }

    public void updateTexts() {
        setTitle(localeManager.text("app.title"));
        user.setText(localeManager.text("main.user") + ": " + controller.username());
        add.setText(localeManager.text("main.add"));
        addIfMin.setText(localeManager.text("main.addIfMin"));
        edit.setText(localeManager.text("main.edit"));
        delete.setText(localeManager.text("main.delete"));
        clear.setText(localeManager.text("main.clear"));
        removeFirst.setText(localeManager.text("main.removeFirst"));
        removeLower.setText(localeManager.text("main.removeLower"));
        script.setText(localeManager.text("main.script"));
        info.setText(localeManager.text("main.info"));
        help.setText(localeManager.text("main.help"));
        darkTheme.setText(localeManager.text("main.theme"));
        language.setText(localeManager.text("main.language"));
        status.setText(localeManager.text("status.ready"));
        table.getTableHeader().repaint();
        updateTotal();
    }

    private void buildLayout() {
        setLayout(new BorderLayout(8, 8));
        setJMenuBar(menuBar());
        JPanel top = new JPanel(new BorderLayout(8, 8));
        top.add(user, BorderLayout.WEST);
        JPanel filterPanel = new JPanel(new BorderLayout(4, 4));
        filterPanel.add(new JLabel(localeManager.text("main.filter")), BorderLayout.WEST);
        filterPanel.add(filter, BorderLayout.CENTER);
        top.add(filterPanel, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new GridLayout(0, 1, 4, 4));
        for (JButton button : new JButton[]{add, addIfMin, edit, delete, clear, removeFirst, removeLower, script, info, help}) {
            buttons.add(button);
        }
        VisualizationPanel visualization = new VisualizationPanel(tableModel, localeManager);
        visualization.setPreferredSize(new Dimension(500, 500));
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(table), visualization);
        split.setResizeWeight(0.55);
        add(split, BorderLayout.CENTER);
        add(buttons, BorderLayout.EAST);

        JPanel bottom = new JPanel(new BorderLayout(8, 8));
        bottom.add(total, BorderLayout.WEST);
        bottom.add(status, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    private JMenuBar menuBar() {
        JMenuBar bar = new JMenuBar();
        language.add(localeButton("English (Canada)", Locale.CANADA));
        language.add(localeButton("Русский", Locale.forLanguageTag("ru")));
        language.add(localeButton("Deutsch", Locale.forLanguageTag("de")));
        language.add(localeButton("Magyar", Locale.forLanguageTag("hu")));
        darkTheme.addActionListener(event -> themeManager.setDark(darkTheme.isSelected(), this));
        bar.add(language);
        bar.add(darkTheme);
        return bar;
    }

    private JButton localeButton(String title, Locale locale) {
        JButton button = new JButton(title);
        button.addActionListener(event -> localeManager.setLocale(locale));
        return button;
    }

    private void bindActions() {
        table.getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                tableModel.sortBy(table.columnAtPoint(e.getPoint()));
                updateTotal();
            }
        });
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) editSelected();
            }
        });
        filter.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterChanged(); }
            public void removeUpdate(DocumentEvent e) { filterChanged(); }
            public void changedUpdate(DocumentEvent e) { filterChanged(); }
        });
        add.addActionListener(event -> OrganizationFormDialog.show(this, localeManager, null)
                .ifPresent(organization -> controller.withOrganization("add", organization, null)));
        addIfMin.addActionListener(event -> OrganizationFormDialog.show(this, localeManager, null)
                .ifPresent(organization -> controller.withOrganization("add_if_min", organization, null)));
        edit.addActionListener(event -> editSelected());
        delete.addActionListener(event -> selected().ifPresent(organization -> controller.simple("remove_by_id", null, String.valueOf(organization.getId()))));
        clear.addActionListener(event -> confirmClear());
        removeFirst.addActionListener(event -> controller.simple("remove_first", null));
        removeLower.addActionListener(event -> OrganizationFormDialog.show(this, localeManager, null)
                .ifPresent(organization -> controller.withOrganization("remove_lower", organization, null)));
        script.addActionListener(event -> chooseScript());
        info.addActionListener(event -> controller.commandMessage("info", message -> showMessage(message, localeManager.text("main.info"))));
        help.addActionListener(event -> controller.commandMessage("help", message -> showMessage(message, localeManager.text("main.help"))));
    }

    private void editSelected() {
        selected().ifPresent(organization -> {
            if (!controller.username().equals(organization.getOwnerUsername())) {
                JOptionPane.showMessageDialog(this, localeManager.text("error.owner"));
                return;
            }
            OrganizationFormDialog.show(this, localeManager, organization)
                    .ifPresent(updated -> controller.withOrganization("update", updated, null, String.valueOf(organization.getId())));
        });
    }

    private Optional<Organization> selected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, localeManager.text("error.select"));
            return Optional.empty();
        }
        return Optional.of(tableModel.organizationAt(row));
    }

    private void confirmClear() {
        int result = JOptionPane.showConfirmDialog(this, localeManager.text("dialog.confirmClear"));
        if (result == JOptionPane.OK_OPTION) controller.simple("clear", null);
    }

    private void chooseScript() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            Path path = chooser.getSelectedFile().toPath();
            controller.executeScript(path);
        }
    }

    private void filterChanged() {
        tableModel.setFilter(filter.getText());
        updateTotal();
    }

    private void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateTotal() {
        NumberFormat format = NumberFormat.getNumberInstance(localeManager.locale());
        total.setText(localeManager.text("main.total") + ": " + format.format(tableModel.turnoverSum()));
    }
}
