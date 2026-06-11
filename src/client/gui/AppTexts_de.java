package client.gui;

import java.util.ListResourceBundle;

public class AppTexts_de extends ListResourceBundle {
    protected Object[][] getContents() {
        return new Object[][]{
                {"app.title", "Organisationsclient"}, {"auth.title", "Autorisierung"},
                {"auth.username", "Benutzer"}, {"auth.password", "Passwort"},
                {"auth.login", "Anmelden"}, {"auth.register", "Registrieren"},
                {"main.user", "Benutzer"}, {"main.filter", "Filter"},
                {"main.add", "Hinzufügen"}, {"main.addIfMin", "Hinzufügen wenn minimal"},
                {"main.edit", "Bearbeiten"}, {"main.delete", "Löschen"},
                {"main.clear", "Meine löschen"}, {"main.removeFirst", "Erstes löschen"},
                {"main.removeLower", "Kleinere löschen"}, {"main.script", "Skript ausführen"},
                {"main.info", "Info"}, {"main.help", "Hilfe"},
                {"main.exit", "Beenden"}, {"main.theme", "Dunkles Design"},
                {"main.language", "Sprache"}, {"main.total", "Jahresumsatz gesamt"},
                {"table.id", "ID"}, {"table.name", "Name"}, {"table.x", "X"}, {"table.y", "Y"},
                {"table.created", "Erstellt"}, {"table.turnover", "Jahresumsatz"},
                {"table.type", "Typ"}, {"table.street", "Straße"}, {"table.zip", "PLZ"},
                {"table.owner", "Eigentümer"}, {"dialog.ok", "OK"}, {"dialog.cancel", "Abbrechen"},
                {"dialog.confirmClear", "Alle eigenen Organisationen löschen?"},
                {"dialog.details", "Organisationsdetails"}, {"dialog.form", "Organisation"},
                {"status.ready", "Bereit"}, {"error.select", "Bitte zuerst eine Organisation wählen"},
                {"error.owner", "Sie dürfen nur eigene Organisationen ändern"}
        };
    }
}
