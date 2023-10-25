import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Класс предназначен для обработки команд
 *
 * @author Свечников Дмитрий и Цымбал Александр
 */
public class telegramBotHandlers extends telegramBotConfigure {

    public Map<String, BiConsumer<Long, String>> commandHandlers = new HashMap<>();
    telegramBotDatabase db = new telegramBotDatabase();
    Connection conn = db.connectToDatabase("bot_users", "postgres", "1234");

    public telegramBotHandlers() {
        commandHandlers.put("registration", this::textCommand);
        commandHandlers.put("member", this::memberReg);
        commandHandlers.put("organizer", this::organizerReg);
    }


    public void textCommand(Long chatId, String nameCommand) {
        telegramBotCommand newCommand = new telegramBotCommand(nameCommand);
        List<InlineKeyboardButton> inlineButtons = newCommand.getButtonsList();

        SendMessage message = new SendMessage();
                message.enableMarkdown(true);
                message.setChatId(chatId);
                message.setText(newCommand.getCommandText());


        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        if (inlineButtons != null && !inlineButtons.isEmpty()) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.addAll(inlineButtons);
            keyboardRows.add(row);
        }

        inlineKeyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(inlineKeyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void memberReg(Long chatId, String S){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);

        try {
            if (db.searchByChatID(conn, "users", chatId.toString(), "chatid") == "11") {
                message.setText("Введите имя");
                execute(message);
                db.insertRow(conn, "users", "Участник", chatId.toString());
            } else {
                message.setText("Вы уже зарегистрированы");
                execute(message);
                textCommand(chatId, "menu$");
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void organizerReg(Long chatId, String S) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);

        try {
            if (db.searchByChatID(conn, "users", chatId.toString(), "chatid") == "11") {
                message.setText("Введите имя");
                execute(message);
                db.insertRow(conn, "users", "Организатор", chatId.toString());
            } else {
                message.setText("Вы уже зарегистрированы");
                execute(message);
                textCommand(chatId, "menu$");
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
