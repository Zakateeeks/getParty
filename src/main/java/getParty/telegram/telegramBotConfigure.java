package getParty.telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.function.BiConsumer;

public class telegramBotConfigure extends TelegramLongPollingBot {

    public BiConsumer<Long, String> handler;
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().isCommand()) {
            System.out.println("its command");
            String command = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getFrom().getUserName();

            handler = telegramBotHandlers.commandHandlers.get(command);
            if (handler != null) {
                handler.accept(chatId, userName);
            }
        }
    }
    @Override
    public String getBotUsername() {

        return "getParty_bot";
    }

    @Override
    public String getBotToken() {

        return "6449322906:AAGnDwAJx75skLFyu4hdQVXabN1pWMPst2o";
    }
}
