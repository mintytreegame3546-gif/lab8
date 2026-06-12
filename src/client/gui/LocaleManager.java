package client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public final class LocaleManager {
    private final List<Localized> listeners = new ArrayList<>();
    private Locale locale = Locale.CANADA;
    private ResourceBundle bundle = ResourceBundle.getBundle("client.gui.AppTexts", locale);

    public String text(String key) {
        return bundle.getString(key);
    }

    public Locale locale() {
        return locale;
    }

    public String message(String message) {
        if (message == null) return "";
        if (message.startsWith("Error: Please enter a valid number")
                || message.startsWith("Error: Please enter a valid ID")
                || message.startsWith("For input string:")) return text("error.invalidInput");
        if (message.startsWith("Error: organization name")
                || message.startsWith("Error: coordinates")
                || message.startsWith("Error: annualTurnover")
                || message.startsWith("Error: officialAddress")) return text("error.invalidInput");
        if (message.startsWith("Error: authorization is required")
                || message.startsWith("Error: authorized username is required")) return text("error.authRequired");
        if (message.startsWith("Error: invalid username or password")) return text("error.invalidCredentials");
        if (message.startsWith("Error: user already exists")) return text("error.userExists");
        if (message.startsWith("Error: login requires")) return text("error.loginRequired");
        if (message.startsWith("Error: register requires")) return text("error.registerRequired");
        if (message.startsWith("Error: Unknown command")) return text("error.unknownCommand");
        if (message.startsWith("Error: you can modify only your own organizations")) return text("error.owner");
        if (message.startsWith("Server is temporarily unavailable")) return text("error.serverUnavailable");
        if (message.startsWith("Error: organization payload is required")) return text("error.payloadRequired");
        return message;
    }

    public void addListener(Localized localized) {
        listeners.add(localized);
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
        this.bundle = ResourceBundle.getBundle("client.gui.AppTexts", locale);
        for (Localized localized : listeners) localized.updateTexts();
    }
}
