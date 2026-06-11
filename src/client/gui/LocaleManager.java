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

    public void addListener(Localized localized) {
        listeners.add(localized);
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
        this.bundle = ResourceBundle.getBundle("client.gui.AppTexts", locale);
        for (Localized localized : listeners) localized.updateTexts();
    }
}
