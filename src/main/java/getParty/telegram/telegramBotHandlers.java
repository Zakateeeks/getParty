package getParty.telegram;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;

public class telegramBotHandlers extends telegramBotConfigure{

    public static Map<String, BiConsumer<Long, String>> commandHandlers = new HashMap<>();

    public telegramBotHandlers() {
        commandHandlers.put("/help", this::handleHelpCommand);
        commandHandlers.put("/about", this::handleAboutCommand);
        commandHandlers.put("/dev", this::handleDevCommand);
        commandHandlers.put("/zalupa", this::handleZalupaCommand);
    }
    private void handleHelpCommand(Long chatId, String userName) {
        String helpText = "Привет, " + userName + "! Это команда /help.";
        sendTextMessage(chatId, helpText);
    }

    private void handleZalupaCommand(Long chatId, String userName) {
        String helpText = "Слышь " + userName + "! Сам ты залупа ебанная, кончефлыжник подзаборный.";
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

    public void sendTextMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId); // Устанавливаем chatId типа Long
        message.setText(text);

        try {
            execute(message); // Отправляем сообщение
        } catch (TelegramApiException e) {
            e.printStackTrace();
            // Обработка ошибки при отправке сообщения
        }
    }
}
