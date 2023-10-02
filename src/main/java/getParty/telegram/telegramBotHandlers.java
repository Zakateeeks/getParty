import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Класс предназначен для обработки команд
 *
 * @author Свечников Дмитрий
 */
public class telegramBotHandlers extends telegramBotConfigure{

    public static Map<String, BiConsumer<Long, String>> commandHandlers = new HashMap<>();

    public telegramBotHandlers() {
            commandHandlers.put("/help", this::handleHelpCommand);
            commandHandlers.put("/about", this::handleAboutCommand);
            commandHandlers.put("/dev", this::handleDevCommand);
            commandHandlers.put("/start", this::handleStartCommand);
    }
    private void handleHelpCommand(Long chatId, String userName) {
        String helpText = "Привет, " + userName + "! Это команда /help.";
        sendTextMessage(chatId, helpText);
    }

    private void handleAboutCommand(Long chatId, String userName) {
        String aboutText = "Привет, " + userName + "! Это команда /about.";
        sendTextMessage(chatId, aboutText);
    }

    private void handleDevCommand(Long chatId, String userName) {
        String aboutText = "Привет, " + userName + "! Это команда /dev.";
        sendTextMessage(chatId, aboutText);
    }

    private void handleStartCommand(Long chatId, String userName) {
        String aboutText = "Привет, " + userName + "! Это бот, который в дальнейшем обретёт пользу, но пока можешь просто потыкать на кнопочки.";
        sendTextMessage(chatId, aboutText);
    }

    public void sendTextMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);


        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        String[] commandTexts = {"/help", "/about?", "/dev"};

        for (String commandText : commandTexts) {
            KeyboardRow row = new KeyboardRow();
            row.add(new KeyboardButton(commandText));
            keyboard.add(row);
        }

        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();

        }
    }


}
