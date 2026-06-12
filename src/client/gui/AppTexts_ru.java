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
                {"main.refresh", "Обновить"},
                {"error.invalidInput", "Некорректный ввод. Проверьте числовые поля и обязательные значения."},
                {"error.authRequired", "Требуется авторизация. Войдите или зарегистрируйтесь."},
                {"error.invalidCredentials", "Неверное имя пользователя или пароль."},
                {"error.userExists", "Пользователь уже существует."},
                {"error.loginRequired", "Для входа нужны имя пользователя и пароль."},
                {"error.registerRequired", "Для регистрации нужны имя пользователя и пароль."},
                {"error.unknownCommand", "Неизвестная команда."},
                {"error.serverUnavailable", "Сервер временно недоступен."},
                {"error.payloadRequired", "Требуются данные организации."},
                {"status.ready", "Готово"}, {"error.select", "Сначала выберите организацию"},
                {"error.owner", "Можно изменять только свои организации"}
        };
    }
}
