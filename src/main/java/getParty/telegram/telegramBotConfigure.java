
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


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
            if (fsm.getState().equals("letter")){
                fsm.sendLetter(update.getMessage().getChatId(),update.getMessage().getText());
            }else {
                fsm.selectInfo(update.getMessage().getChatId(), fsm.getState(), update.getMessage().getText());
            }
        }
    }

    public void sendNotification(String chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
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
