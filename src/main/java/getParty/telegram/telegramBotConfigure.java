import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.function.BiConsumer;

/**
 * Класс отвечает за конфигурацию бота
 *
 * @author Цымбал Александр
 */
public class telegramBotConfigure extends TelegramLongPollingBot {

    public BiConsumer<Long, String> handler;

    /**
     * Метод реагирует на обновления бота
     */
    @Override
    public void onUpdateReceived(Update update) {
        telegramBotHandlers th = new telegramBotHandlers();
        if (update.hasMessage() && update.getMessage().isCommand()) {
            String command = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getFrom().getUserName();
            handler = th.commandHandlers.get(command);
            if (handler != null) {
                handler.accept(chatId, userName);
            }
        }
    }

    /**
     * Метод для получения имени бота
     * @return никнейм бота в телеграм
     */
    @Override
    public String getBotUsername() {

        return "getParty_bot";
    }

    /**
     * Метод для получения токена бота
     * @return токен бота в телеграм
     */
    @Override
    public String getBotToken() {

        return "6449322906:AAGnDwAJx75skLFyu4hdQVXabN1pWMPst2o";
    }
}
