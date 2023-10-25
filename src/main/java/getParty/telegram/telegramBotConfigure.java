
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;


/**
 * Класс отвечает за конфигурацию бота
 *
 * @author Цымбал Александр
 */
public class telegramBotConfigure extends TelegramLongPollingBot {
    /**
     * Метод реагирует на обновления бота
     */
    @Override
    public void onUpdateReceived(Update update) {
        telegramBotHandlers handler = new telegramBotHandlers();

        if (update.hasMessage() && update.getMessage().isCommand()) {
            String command = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            handler.textCommand(chatId, command);
        }

        else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String callbackData = callbackQuery.getData();
            Long chatId = callbackQuery.getMessage().getChatId();

            handler.commandHandlers.get(callbackData).accept(chatId, callbackData);
        }
        else{
            telegramFSM fsm = new telegramFSM();
            fsm.selectInfo(update.getMessage().getChatId(),fsm.getState(),update.getMessage().getText());
        }
    }

    /**
     * Метод для получения имени бота
     *
     * @return никнейм бота в телеграм
     */
    @Override
    public String getBotUsername() {

        return System.getenv("BOT_NAME");
    }

    /**
     * Метод для получения токена бота
     *
     * @return токен бота в телеграм
     */
    @Override
    public String getBotToken() {

        return System.getenv("BOT_TOKEN");
    }
}
