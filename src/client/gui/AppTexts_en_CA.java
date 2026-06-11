package client.gui;

import java.util.ListResourceBundle;

public class AppTexts_en_CA extends ListResourceBundle {
    protected Object[][] getContents() {
        return new Object[][]{
                {"app.title", "Organization Client"},
                {"auth.title", "Authorization"},
                {"auth.username", "Username"},
                {"auth.password", "Password"},
                {"auth.login", "Log in"},
                {"auth.register", "Register"},
                {"main.user", "User"},
                {"main.filter", "Filter"},
                {"main.add", "Add"},
                {"main.addIfMin", "Add if minimal"},
                {"main.edit", "Edit"},
                {"main.delete", "Delete"},
                {"main.clear", "Clear mine"},
                {"main.removeFirst", "Remove first"},
                {"main.removeLower", "Remove lower"},
                {"main.script", "Execute script"},
                {"main.info", "Info"},
                {"main.help", "Help"},
                {"main.exit", "Exit"},
                {"main.theme", "Dark theme"},
                {"main.language", "Language"},
                {"main.total", "Annual turnover total"},
                {"table.id", "ID"},
                {"table.name", "Name"},
                {"table.x", "X"},
                {"table.y", "Y"},
                {"table.created", "Created"},
                {"table.turnover", "Annual turnover"},
                {"table.type", "Type"},
                {"table.street", "Street"},
                {"table.zip", "Zip code"},
                {"table.owner", "Owner"},
                {"dialog.ok", "OK"},
                {"dialog.cancel", "Cancel"},
                {"dialog.confirmClear", "Delete all organizations that belong to you?"},
                {"dialog.details", "Organization details"},
                {"dialog.form", "Organization"},
                {"status.ready", "Ready"},
                {"error.select", "Select an organization first"},
                {"error.owner", "You can modify only your own organizations"}
        };
    }
}
