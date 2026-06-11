package client.gui;

import java.util.ListResourceBundle;

public class AppTexts_ru extends ListResourceBundle {
    protected Object[][] getContents() {
        return new Object[][]{
                {"app.title", "Клиент организаций"}, {"auth.title", "Авторизация"},
                {"auth.username", "Пользователь"}, {"auth.password", "Пароль"},
                {"auth.login", "Войти"}, {"auth.register", "Регистрация"},
                {"main.user", "Пользователь"}, {"main.filter", "Фильтр"},
                {"main.add", "Добавить"}, {"main.addIfMin", "Добавить если минимум"},
                {"main.edit", "Изменить"}, {"main.delete", "Удалить"},
                {"main.clear", "Очистить мои"}, {"main.removeFirst", "Удалить первый"},
                {"main.removeLower", "Удалить меньшие"}, {"main.script", "Выполнить скрипт"},
                {"main.info", "Информация"}, {"main.help", "Помощь"},
                {"main.exit", "Выход"}, {"main.theme", "Тёмная тема"},
                {"main.language", "Язык"}, {"main.total", "Сумма оборота"},
                {"table.id", "ID"}, {"table.name", "Название"}, {"table.x", "X"}, {"table.y", "Y"},
                {"table.created", "Создано"}, {"table.turnover", "Годовой оборот"},
                {"table.type", "Тип"}, {"table.street", "Улица"}, {"table.zip", "Индекс"},
                {"table.owner", "Владелец"}, {"dialog.ok", "ОК"}, {"dialog.cancel", "Отмена"},
                {"dialog.confirmClear", "Удалить все ваши организации?"},
                {"dialog.details", "Детали организации"}, {"dialog.form", "Организация"},
                {"status.ready", "Готово"}, {"error.select", "Сначала выберите организацию"},
                {"error.owner", "Можно изменять только свои организации"}
        };
    }
}
