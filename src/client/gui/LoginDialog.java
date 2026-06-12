package client.gui;

import network.CommandResponse;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

@SuppressWarnings("serial")
public final class LoginDialog extends JDialog implements Localized {
    private final LocaleManager localeManager;
    private final GuiCommandClient client;
    private final JTextField username = new JTextField(18);
    private final JPasswordField password = new JPasswordField(18);
    private final JLabel usernameLabel = new JLabel();
    private final JLabel passwordLabel = new JLabel();
    private final JLabel languageLabel = new JLabel();
    private final JComboBox<LocaleOption> language = new JComboBox<>(new LocaleOption[]{
            new LocaleOption("English (Canada)", Locale.CANADA),
            new LocaleOption("Русский", Locale.forLanguageTag("ru")),
            new LocaleOption("Deutsch", Locale.forLanguageTag("de")),
            new LocaleOption("Magyar", Locale.forLanguageTag("hu"))
    });
    private final JButton login = new JButton();
    private final JButton register = new JButton();
    private boolean authorized;

    public LoginDialog(LocaleManager localeManager, GuiCommandClient client) {
        this.localeManager = localeManager;
        this.client = client;
        localeManager.addListener(this);
        setModal(true);
        setLayout(new BorderLayout(8, 8));
        JPanel fields = new JPanel(new GridLayout(0, 2, 6, 6));
        fields.add(usernameLabel);
        fields.add(username);
        fields.add(passwordLabel);
        fields.add(password);
        fields.add(languageLabel);
        fields.add(language);
        JPanel buttons = new JPanel();
        buttons.add(login);
        buttons.add(register);
        add(fields, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        language.addActionListener(this::changeLanguage);
        login.addActionListener(authenticateAction("login"));
        register.addActionListener(authenticateAction("register"));
        updateTexts();
        pack();
        setLocationRelativeTo(null);
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void updateTexts() {
        setTitle(localeManager.text("auth.title"));
        usernameLabel.setText(localeManager.text("auth.username"));
        passwordLabel.setText(localeManager.text("auth.password"));
        languageLabel.setText(localeManager.text("main.language"));
        login.setText(localeManager.text("auth.login"));
        register.setText(localeManager.text("auth.register"));
    }

    private void changeLanguage(ActionEvent event) {
        if (event == null) return;
        Object item = language.getSelectedItem();
        if (item instanceof LocaleOption option) localeManager.setLocale(option.locale());
    }

    private ActionListener authenticateAction(String command) {
        return event -> {
            if (event == null) return;
            authenticate(command);
        };
    }

    private void authenticate(String command) {
        login.setEnabled(false);
        register.setEnabled(false);
        new SwingWorker<CommandResponse, Void>() {
            protected CommandResponse doInBackground() throws Exception {
                return client.login(command, username.getText().trim(), new String(password.getPassword()));
            }

            protected void done() {
                login.setEnabled(true);
                register.setEnabled(true);
                try {
                    CommandResponse response = get();
                    if (response.isSuccess()) {
                        authorized = true;
                        dispose();
                    } else JOptionPane.showMessageDialog(LoginDialog.this, localeManager.message(response.getMessage()));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(LoginDialog.this, localeManager.message(e.getMessage()));
                }
            }
        }.execute();
    }

    private record LocaleOption(String label, Locale locale) {
        public String toString() {
            return label;
        }
    }
}
