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
                {"main.refresh", "Frissítés"},
                {"main.history", "Előzmények"},
                {"history.title", "Parancselőzmények"},
                {"history.refresh", "Frissítés"},
                {"history.close", "Bezárás"},
                {"history.loading", "Előzmények betöltése..."},
                {"history.empty", "Nincs parancselőzmény"},
                {"history.loaded", "Előzmények betöltve"},
                {"history.id", "ID"},
                {"history.user", "Felhasználó"},
                {"history.command", "Parancs"},
                {"history.timestamp", "Időpont"},
                {"history.status", "Állapot"},
                {"history.success", "Siker"},
                {"history.failure", "Hiba"},
                {"error.invalidInput", "Érvénytelen bevitel. Ellenőrizze a számmezőket és a kötelező értékeket."},
                {"error.authRequired", "Hitelesítés szükséges. Jelentkezzen be vagy regisztráljon."},
                {"error.invalidCredentials", "Érvénytelen felhasználónév vagy jelszó."},
                {"error.userExists", "A felhasználó már létezik."},
                {"error.loginRequired", "A belépéshez felhasználónév és jelszó szükséges."},
                {"error.registerRequired", "A regisztrációhoz felhasználónév és jelszó szükséges."},
                {"error.unknownCommand", "Ismeretlen parancs."},
                {"error.serverUnavailable", "A szerver átmenetileg nem érhető el."},
                {"error.payloadRequired", "Szervezeti adatok szükségesek."},
                {"status.ready", "Kész"}, {"error.select", "Előbb válasszon szervezetet"},
                {"error.owner", "Csak saját szervezetek módosíthatók"}
        };
    }
}
