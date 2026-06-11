package client.gui;

import java.util.ListResourceBundle;

public class AppTexts_hu extends ListResourceBundle {
    protected Object[][] getContents() {
        return new Object[][]{
                {"app.title", "Szervezeti kliens"}, {"auth.title", "Hitelesítés"},
                {"auth.username", "Felhasználó"}, {"auth.password", "Jelszó"},
                {"auth.login", "Belépés"}, {"auth.register", "Regisztráció"},
                {"main.user", "Felhasználó"}, {"main.filter", "Szűrő"},
                {"main.add", "Hozzáadás"}, {"main.addIfMin", "Hozzáadás ha minimális"},
                {"main.edit", "Szerkesztés"}, {"main.delete", "Törlés"},
                {"main.clear", "Sajátok törlése"}, {"main.removeFirst", "Első törlése"},
                {"main.removeLower", "Kisebbek törlése"}, {"main.script", "Szkript futtatása"},
                {"main.info", "Információ"}, {"main.help", "Súgó"},
                {"main.exit", "Kilépés"}, {"main.theme", "Sötét téma"},
                {"main.language", "Nyelv"}, {"main.total", "Éves forgalom összege"},
                {"table.id", "ID"}, {"table.name", "Név"}, {"table.x", "X"}, {"table.y", "Y"},
                {"table.created", "Létrehozva"}, {"table.turnover", "Éves forgalom"},
                {"table.type", "Típus"}, {"table.street", "Utca"}, {"table.zip", "Irányítószám"},
                {"table.owner", "Tulajdonos"}, {"dialog.ok", "OK"}, {"dialog.cancel", "Mégse"},
                {"dialog.confirmClear", "Töröljük az összes saját szervezetet?"},
                {"dialog.details", "Szervezet részletei"}, {"dialog.form", "Szervezet"},
                {"status.ready", "Kész"}, {"error.select", "Előbb válasszon szervezetet"},
                {"error.owner", "Csak saját szervezetek módosíthatók"}
        };
    }
}
