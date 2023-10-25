import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.sql.Connection;

public class telegramBot {
    public static void main(String[] args) {
        telegramBotDatabase db = new telegramBotDatabase();
        Connection conn = db.connectToDatabase("bot_users", "postgres", "1234");
        db.createTable(conn, "users");
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new telegramBotConfigure());

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
