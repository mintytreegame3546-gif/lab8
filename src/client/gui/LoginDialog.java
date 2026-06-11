package client.gui;

import network.CommandResponse;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Optional;
import javax.swing.JButton;
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
        JPanel buttons = new JPanel();
        buttons.add(login);
        buttons.add(register);
        add(fields, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        login.addActionListener(event -> authenticate("login"));
        register.addActionListener(event -> authenticate("register"));
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
        login.setText(localeManager.text("auth.login"));
        register.setText(localeManager.text("auth.register"));
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
                    } else JOptionPane.showMessageDialog(LoginDialog.this, response.getMessage());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(LoginDialog.this, e.getMessage());
                }
            }
        }.execute();
    }
}
