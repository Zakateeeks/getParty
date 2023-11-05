import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.sql.Connection;

public class telegramBot {
    public static void main(String[] args) {
        telegramBotDatabase db = new telegramBotDatabase();
        Connection conn = db.connectToDatabase("bot_users", "postgres", "1234");
        String usersTableStructure = "empid SERIAL, name VARCHAR(200), role VARCHAR(200), description VARCHAR(200), chatID VARCHAR(200), PRIMARY KEY (empid)";
        String eventTableStructure = "empid SERIAL, organizer VARCHAR(200), eventname VARCHAR(200), eventdesc VARCHAR(200), chatID VARCHAR(200), eventref VARCHAR(200), eventdate VARCHAR(200), id VARCHAR(200), PRIMARY KEY (empid)";

        db.createTable(conn, "users", usersTableStructure);
        db.createTable(conn, "event", eventTableStructure);
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new telegramBotConfigure());

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
