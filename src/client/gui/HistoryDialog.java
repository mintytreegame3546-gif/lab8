package client.gui;

import data.CommandHistoryRecord;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

@SuppressWarnings("serial")
public final class HistoryDialog extends JDialog implements Localized {
    private final LocaleManager localeManager;
    private final MainController controller;
    private final HistoryTableModel tableModel;
    private final JLabel status = new JLabel();
    private final JButton refresh = new JButton();
    private final JButton close = new JButton();
    private String statusKey;

    private HistoryDialog(Frame owner, LocaleManager localeManager, MainController controller) {
        super(owner);
        this.localeManager = localeManager;
        this.controller = controller;
        this.tableModel = new HistoryTableModel(localeManager);
        localeManager.addListener(this);
        buildLayout();
        bindActions();
        updateTexts();
        setSize(760, 420);
        setLocationRelativeTo(owner);
        loadHistory();
    }

    public static void show(Frame owner, LocaleManager localeManager, MainController controller) {
        new HistoryDialog(owner, localeManager, controller).setVisible(true);
    }

    public void updateTexts() {
        setTitle(localeManager.text("history.title"));
        refresh.setText(localeManager.text("history.refresh"));
        close.setText(localeManager.text("history.close"));
        tableModel.localeChanged();
        if (statusKey != null) status.setText(localeManager.text(statusKey));
    }

    private void buildLayout() {
        setLayout(new BorderLayout(8, 8));
        add(new JScrollPane(new JTable(tableModel)), BorderLayout.CENTER);
        JPanel bottom = new JPanel(new BorderLayout(8, 8));
        JPanel buttons = new JPanel();
        buttons.add(refresh);
        buttons.add(close);
        bottom.add(status, BorderLayout.CENTER);
        bottom.add(buttons, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);
    }

    private void bindActions() {
        refresh.addActionListener(event -> loadHistory());
        close.addActionListener(event -> dispose());
    }

    private void loadHistory() {
        setStatusKey("history.loading");
        refresh.setEnabled(false);
        controller.history(this::showHistory, this::showStatus);
    }

    private void showHistory(List<CommandHistoryRecord> history) {
        tableModel.setHistory(history);
        refresh.setEnabled(true);
        setStatusKey(history.isEmpty() ? "history.empty" : "history.loaded");
    }

    private void showStatus(String message) {
        refresh.setEnabled(true);
        statusKey = null;
        status.setText(message);
    }

    private void setStatusKey(String statusKey) {
        this.statusKey = statusKey;
        status.setText(localeManager.text(statusKey));
    }
}
